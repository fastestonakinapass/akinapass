/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.Jenkinster;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static my.Jenkinster.JenkinsterGUI.absolutePath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 *
 * @author shirsak.s
 */
public class JenkinsLogic extends JenkinsterGUI
{
    public static void listJobs(JenkinsServer jenkins) throws IOException
	{
            /*
                Gets list of all Jenkins Jobs
            */
                
		Map<String,Job> map = jenkins.getJobs();
                listA=new ArrayList<>();
		for(String s : map.keySet())
			listA.add(s);
        }
    public static void showListedJobs()
        {
            /*
                Displays list of Jenkins Jobs on the GUI frame
            */
                modelA.setRowCount(0);
                Object rowData[]=new Object[2];
                for(int i=0;i<listA.size();i++)
                {
                    rowData[0]=false;
                    rowData[1]=listA.get(i);
                    modelA.addRow(rowData);
                }
                    
	}
    public static void getSelected()
        {
            /*
                Get Selected Jobs to be deleted
            */
            selectedlistA=new ArrayList<>();
            for(int i=0;i<listA.size();i++)
            {                
                if((boolean)modelA.getValueAt(i,0)==true)
                {
                    selectedlistA.add((String) modelA.getValueAt(i,1));
                }
            }
            for(int i=0;i<selectedlistA.size(); i++)
            {
                System.out.println(selectedlistA.get(i));
            }
        }
    public static void doDelete(int x) throws UnsupportedEncodingException, MalformedURLException, IOException
    {
        /*
            Delete Selected Jenkins jobs
        */
        String authStr=jenkinsuser+":"+jenkinstoken;
	String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
        StringBuffer urlstring=new StringBuffer();
        urlstring.append(jenkinsurl.toString());
        urlstring.append("job/");
        String prettyString=newXMLParsing.prettyString(selectedlistA.get(x));
        urlstring.append(prettyString);
        urlstring.append("/doDelete");
	URL url=new URL(urlstring.toString());
        System.out.println(urlstring.toString());
	HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", "Basic " + encoding);
	String head, body;
	head=""; body="";int i;
        StringBuffer response=getCrumb(jenkins);
	for(i=0; i<response.length(); i++)
	{
		if(response.charAt(i)!=':')
			head+=response.charAt(i);
		else
			break;
	}
	for(int j=i+1; j<response.length();j++)
		body+=response.charAt(j);
        //String str="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<mahati>\n</mahati>";
	conn.setRequestProperty(head,body);
	conn.setRequestProperty("Content-Type","text/xml");
	conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        conn.connect();        
        System.out.println(conn.getResponseMessage());
    }
    public static void getPluginVersion() throws UnsupportedEncodingException, MalformedURLException, IOException, SAXException, ParserConfigurationException
    {
        /*
            Gets Plugins and versions currently on jenkins
        */
        String authStr=jenkinsuser+":"+jenkinstoken;
	String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
        
        String urlstring=jenkinsurl;
	urlstring+="/pluginManager/api/xml?depth=1&xpath=/*/*/shortName|/*/*/version&wrapper=plugins";
	URL url=new URL(urlstring);
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	connection.setRequestMethod("GET");
	connection.setRequestProperty("Authorization", "Basic " + encoding);
					
        StringBuffer response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            /*   try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            */      String output;
            response = new StringBuffer();
            while ((output = in.readLine()) != null)
            {
                response.append(output);
                response.append("\n");
            }
        }
        StringBuilder prettyxml=new StringBuilder();
        prettyxml.append("<mahati>\n");
        prettyxml.append(response);
        prettyxml.append("</mahati>\n");
        //System.out.println(prettyxml.toString());
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(prettyxml.toString()));
        Document doc = db.parse(is);
        doc.getDocumentElement().normalize();
        NodeList jenkinslist=doc.getElementsByTagName("shortName");
        plugins=new HashMap<String,String>();
        for(int i=0;i<jenkinslist.getLength();i++)
        {
            //System.out.println(jenkinslist.getLength());
            Node b= jenkinslist.item(i);
            if(b.getNodeType()==Node.ELEMENT_NODE)
            {
                Element element=(Element) b;
                String name=element.getTextContent();
                String version=doc.getElementsByTagName("version").item(i).getTextContent();
                plugins.put(name,version);
            }
        }
        for (String plugin: plugins.keySet())
                    {
                    String key =plugin;
                    String value = plugins.get(plugin);
                    //System.out.println(key+" : "+value);
                    }
        
    }
    public static StringBuffer getCrumb(JenkinsServer jenkins) throws IOException
    {
        /*
            Generates Jenkins Crumb to be sent as a REST request property
        */
        String authStr=jenkinsuser+":"+jenkinstoken;
	String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
        
        String urlstring=jenkinsurl;
	urlstring+="crumbIssuer/api/xml?xpath=concat(//crumbRequestField,\":\",//crumb)";
	URL url=new URL(urlstring);
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	connection.setRequestMethod("GET");
	connection.setRequestProperty("Authorization", "Basic " + encoding);
					
        StringBuffer response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            /*   try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            */      String output;
            response = new StringBuffer();
            while ((output = in.readLine()) != null)
            {
                response.append(output);
            }
        }
        return response;
    }    
    public static void exportJobs(JenkinsServer jenkins) throws IOException
    {
        /*
            Creates empty Jenkins Job and adds our written xml to the Job
        */
        URI jenkinsuri=null;
        try
        {
        jenkinsuri=new URI(jenkinsurl);
        }
        catch(URISyntaxException e)
        {
            System.out.println("Enter valid URI: "+e);
            //jTextArea1.setText("Enter valid URI: http://example.com/");
        }
                jenkins = new JenkinsServer(jenkinsuri,jenkinsuser,jenkinstoken);
		String authStr=jenkinsuser+":"+jenkinstoken;
		String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
        StringBuffer urlstring=new StringBuffer();
        urlstring.append(jenkinsurl);
        urlstring.append("createItem?name=");
        urlstring.append(bfappend);
        projectname=newXMLParsing.prettyString(projectname);
        urlstring.append(projectname);
	URL url=new URL(urlstring.toString());
	HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", "Basic " + encoding);
	String head, body;
	head=""; body="";int i;
        StringBuffer response=getCrumb(jenkins);
	for(i=0; i<response.length(); i++)
	{
		if(response.charAt(i)!=':')
			head+=response.charAt(i);
		else
			break;
	}
	for(int j=i+1; j<response.length();j++)
		body+=response.charAt(j);
	conn.setRequestProperty(head,body);
	conn.setRequestProperty("Content-Type","text/xml");
	conn.setDoOutput(true);
	try(FileInputStream inputStream = new FileInputStream(new File(absolutePath)))
	{
            try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream()))
            {
                int c;
                while ((c = inputStream.read(BUFFER, 0, BUFFER.length)) > 0)
                {
                    wr.write(BUFFER, 0, c);
                }
            }
            InputStream content = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader (content));
            String line;
            while ((line = in.readLine()) != null)
            {
                System.out.println(line);
            }
	}
    }    
}