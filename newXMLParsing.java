/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.Jenkinster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import static my.Jenkinster.BuildforgeLogic.helper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
/**
 *
 * @author shirsak.s
 */
public class newXMLParsing extends JenkinsterGUI
{
    public static void setXMLDefault()
    {
        /*
            Defaults to jenkins config file
        */
        keepDependencies=false;
        canRoam=true;
        disabled=false;
        blockBuildWhenDownstreamBuilding=false;
        blockBuildWhenUpstreamBuilding=false;
        concurrentBuild=false;        
        completeBuild=true;
        scmAttribute="hudson.scm.NullSCM";
        name="SUCCESS";
        ordinal=0;
        color="BLUE";
        description="";
    }
    public static void createTemp()
    {
        /*
            Create temp files to do work on
        */
        try {    		
    		//create a temp file
    		File temp = File.createTempFile("config", ".xml");
		//Get tempropary file path
    		absolutePath = temp.getAbsolutePath();
                filename = absolutePath.substring(absolutePath.lastIndexOf(File.separator)+1);
                tempFilePath = absolutePath.
    		    substring(0,absolutePath.lastIndexOf(File.separator));
    		
    		//System.out.println("Temp file : " + temp.getAbsolutePath());
    		//System.out.println("Temp file path : " + tempFilePath);
                //System.out.println("Temp file : " + filename);
                realfilepath=new String();
            }
        catch(IOException e)
        {    		
    		e.printStackTrace();    		
    	}
    }
    public static void readCommands() throws ParserConfigurationException, SAXException, IOException
    {
        /*
            Makes debugs to buildforge commands and sets it in compliance with jenkins
            Also takes care of the Platform dependancies
        */
        ArrayList<String> temp;StringBuilder finalcommand;
        for(int i=0;i<newcommands.size();i++)
        {
            temp=new ArrayList<String>();finalcommand=new StringBuilder();
            int length=newcommands.get(i).length();
            StringBuilder command=new StringBuilder();
            command.append(newcommands.get(i));
            String newcommand=command.toString();
            String[] lines = newcommand.split("\\r?\\n");
            for (String line : lines) 
            {
                temp.add(line);
                //System.out.println(" : "+line);
            }
            for(int j=0;j<temp.size();j++)
            {
                try
                {
                if(temp.get(j).contains(".tset env \""))
                {
                    String debugstring=temp.get(j);
                    if(windows.get(i).equals("linux"))
                        debugstring=debugstring.replace(".tset env \"","export ");
                    else
                        debugstring=debugstring.replace(".tset env \"","set ");
                    if(debugstring.charAt(debugstring.length()-1)=='\"')
                        debugstring=debugstring.substring(0, debugstring.length()-1);
                    finalcommand.append(debugstring);
                }                
                else if(temp.get(j).charAt(0)=='.')
                {
                    String debugstring=temp.get(j);
                    if(windows.get(i).equals("linux"))
                        debugstring="#"+debugstring;
                    else
                        debugstring="rem "+debugstring;
                    finalcommand.append(debugstring);
                }
                else
                    finalcommand.append(temp.get(j));
                
                }
                catch(StringIndexOutOfBoundsException e)
                {}
                finalcommand.append(System.getProperty("line.separator"));
            }
            newcommands.set(i, finalcommand.toString());
        }
    }    
    public static void writeXml() throws ParserConfigurationException, FileNotFoundException, TransformerConfigurationException, TransformerException, MalformedURLException, IOException, UnsupportedEncodingException, SAXException
    {
        /*
            Writes the xml to be written to jenkins
        */
        createTemp();
        setXMLDefault();
        description+="Project Pass Chain : ";
        description+=passchain;
        description+="\nProject Fail Chain : ";
        description+=failchain;
        JenkinsLogic.getPluginVersion();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document xmldoc = docBuilder.newDocument();
        Element element;
        Element rootElement=xmldoc.createElement("project");
        Element actionElement=xmldoc.createElement("actions");        
        Element descriptionElement=xmldoc.createElement("description");
        Element keepDependenciesElement=xmldoc.createElement("keepDependencies");
        Element propertiesElement=xmldoc.createElement("properties");
        Element scmElement=xmldoc.createElement("scm");
        Element canRoamElement=xmldoc.createElement("canRoam");
        Element disabledElement=xmldoc.createElement("disabled");
        Element blockBuildWhenDownstreamBuildingElement=xmldoc.createElement("blockBuildWhenDownstreamBuilding");
        Element blockBuildWhenUpstreamBuildingElement=xmldoc.createElement("blockBuildWhenUpstreamBuilding");
        Element triggersElement=xmldoc.createElement("triggers");
        Element concurrentBuildElement=xmldoc.createElement("concurrentBuild");
        Element buildersElement=xmldoc.createElement("builders");
        Element publishersElement=xmldoc.createElement("publishers");
        Element buildWrappersElement=xmldoc.createElement("buildWrappers");
        
        
        
        rootElement.appendChild(actionElement);
        rootElement.appendChild(descriptionElement);
        rootElement.appendChild(keepDependenciesElement);
        rootElement.appendChild(propertiesElement);
        rootElement.appendChild(scmElement);
        rootElement.appendChild(canRoamElement);
        rootElement.appendChild(disabledElement);
        rootElement.appendChild(blockBuildWhenDownstreamBuildingElement);
        rootElement.appendChild(blockBuildWhenUpstreamBuildingElement);
        rootElement.appendChild(triggersElement);
        rootElement.appendChild(concurrentBuildElement);
        rootElement.appendChild(buildersElement);
        rootElement.appendChild(publishersElement);
        rootElement.appendChild(buildWrappersElement);
        xmldoc.appendChild(rootElement);
        /*
        keepDependencies=false;
        canRoam=true;
        disabled=false;
        blockBuildWhenDownstreamBuilding=false;
        blockBuildWhenUpstreamBuilding=false;
        concurrentBuild=false;
        */
        element = (Element) xmldoc.getElementsByTagName("scm").item(0);
        element.setAttribute("class",scmAttribute);
        
        
        Text keepText=xmldoc.createTextNode(String.valueOf(keepDependencies));
        Text canText=xmldoc.createTextNode(String.valueOf(canRoam));
        Text disabledText=xmldoc.createTextNode(String.valueOf(disabled));
        Text builddownText=xmldoc.createTextNode(String.valueOf(blockBuildWhenDownstreamBuilding));
        Text buildupText=xmldoc.createTextNode(String.valueOf(blockBuildWhenUpstreamBuilding));
        Text concurrentText=xmldoc.createTextNode(String.valueOf(concurrentBuild));
        Text descriptionText=xmldoc.createTextNode(String.valueOf(description));
        
        descriptionElement.appendChild(descriptionText);
        keepDependenciesElement.appendChild(keepText);
        canRoamElement.appendChild(canText);
        disabledElement.appendChild(disabledText);
        blockBuildWhenDownstreamBuildingElement.appendChild(builddownText);
        blockBuildWhenUpstreamBuildingElement.appendChild(buildupText);
        concurrentBuildElement.appendChild(concurrentText);
        
        boolean checkelse=false;
        int reali=0;
        for(int i=0;i<newcommands.size();i++)
        {
            boolean tokencheck=false;
            String conditionclass="org.jenkins_ci.plugins.run_condition.core.AlwaysRun";            
            //if(newcondition.get(i)!="null" && newelsecommands.get(i)=="null")
            if(!newcondition.get(i).isEmpty())
            {
                tokencheck=true;
                conditionclass="org.jenkins_ci.plugins.run_condition.core.BooleanCondition";
            }                
                StringBuffer temp=new StringBuffer();
                Element conditionbatchFileElement=xmldoc.createElement("org.jenkinsci.plugins.conditionalbuildstep.singlestep.SingleConditionalBuilder");
                Element conditionElement=xmldoc.createElement("condition");
                Element buildStepElement=xmldoc.createElement("buildStep");
                Element commandElement=xmldoc.createElement("command");
                Element runnerElement=xmldoc.createElement("runner");
                if(!map.isEmpty())
                {
                    for (String name: map.keySet())
                    {
                    String key =name.toString();
                    String value = map.get(name).toString();
                    if(!windows.get(i).equals("linux"))
                        temp.append("set ");
                    else
                        temp.append("export ");
                    temp.append(key);
                    temp.append("=");
                    temp.append(value);
                    temp.append(System.getProperty("line.separator"));
                    }
                }
                if(tokencheck==true && checkelse==false)
                {
                if(!windows.get(i).equals("linux"))
                    temp.append("if (");
                else
                    temp.append("if [");
                temp.append(newcondition.get(i));
                if(!windows.get(i).equals("linux"))
                    temp.append(")\n");
                else
                    temp.append("];then\n");
                }
                else if(tokencheck==true && checkelse==true)
                {
                if(!windows.get(i).equals("linux"))
                    temp.append("if not (");
                else
                    temp.append("if ! [");
                temp.append(newcondition.get(i));
                if(!windows.get(i).equals("linux"))
                    temp.append(")\n");
                else
                    temp.append("];then\n");
                }
                if(checkelse==false)
                    temp.append(newcommands.get(i));
                else if(checkelse==true)
                    temp.append(newelsecommands.get(i));
                Text commandText=xmldoc.createTextNode(String.valueOf(temp));
                commandElement.appendChild(commandText);
                buildStepElement.appendChild(commandElement);
                conditionbatchFileElement.appendChild(conditionElement);
                conditionbatchFileElement.appendChild(buildStepElement);
                conditionbatchFileElement.appendChild(runnerElement);
                
                buildersElement.appendChild(conditionbatchFileElement);
                if(checkelse==false && !stepinlineid.get(i).isEmpty())
                    buildersElement.appendChild(Inline(xmldoc, checkelse, i, reali));
                else if(checkelse==true && !stepelseinlineid.get(i).isEmpty())
                    buildersElement.appendChild(Inline(xmldoc, checkelse, i, reali));
                element = (Element) xmldoc.getElementsByTagName("org.jenkinsci.plugins.conditionalbuildstep.singlestep.SingleConditionalBuilder").item(reali);
                element.setAttribute("plugin","conditional-buildstep"+"@"+plugins.get("conditional-buildstep"));
                element = (Element) xmldoc.getElementsByTagName("condition").item(reali);
                element.setAttribute("class","org.jenkins_ci.plugins.run_condition.core.AlwaysRun");
                element.setAttribute("plugin","run-condition"+"@"+plugins.get("run-condition"));
                element = (Element) xmldoc.getElementsByTagName("buildStep").item(reali);
                element.setAttribute("class","hudson.tasks.BatchFile");
                element = (Element) xmldoc.getElementsByTagName("runner").item(reali);
                element.setAttribute("class","org.jenkins_ci.plugins.run_condition.BuildStepRunner$Fail");
                element.setAttribute("plugin","run-condition"+"@"+plugins.get("run-condition"));
                //conditioni++;
                
                if(!newelsecommands.get(i).isEmpty() && checkelse==false)
                {
                    i--;
                    checkelse=true;
                }
                else
                {
                    checkelse=false;
                }
                reali++;
            }
        /*Element batchFileElement=xmldoc.createElement("hudson.tasks.BatchFile");
        Element commandElement=xmldoc.createElement("command");
        StringBuffer temp=new StringBuffer();
        if(!map.isEmpty())
        {
            for (String name: map.keySet())
                    {
                    String key =name.toString();
                    String value = map.get(name).toString();
                    temp.append("set ");
                    temp.append(key);
                    temp.append("=");
                    temp.append(value);
                    temp.append(System.getProperty("line.separator"));
                    }
        }
        temp.append(newcommands.get(i));
        Text commandText=xmldoc.createTextNode(String.valueOf(temp));
        commandElement.appendChild(commandText);
        batchFileElement.appendChild(commandElement);
        buildersElement.appendChild(batchFileElement);*/
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();            
            
            //for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(xmldoc);

            //write to console or file
            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File(absolutePath));

            //write data
            transformer.transform(source, console);
            transformer.transform(source, file);
            System.out.println("DONE");
    }
    public static Element Inline(Document xmldoc, boolean check,int i, int reali)
    {
        /*
            Get Inline Chain projects if any
            Adds them to the export queue
        */
        Element parameterElement=xmldoc.createElement("hudson.plugins.parameterizedtrigger.TriggerBuilder");
        Element configsElement=xmldoc.createElement("configs");
        Element blockableElement=xmldoc.createElement("hudson.plugins.parameterizedtrigger.BlockableBuildTriggerConfig");
        Element configs2Element=xmldoc.createElement("configs");
        Element projectsElement=xmldoc.createElement("projects");
        Element conditionElement=xmldoc.createElement("condition");
        Element trigger1Element=xmldoc.createElement("triggerWithNoParameters");
        Element trigger2Element=xmldoc.createElement("triggerFromChildProjects");
        Element blockElement=xmldoc.createElement("block");
        String name[]={"FAILURE","UNSTABLE","FAILURE"};
        int ordinal[]={2,1,2};
        String color[]={"RED","YELLOW","RED"};
        boolean build[]={true,true,true};
        Element buildStepFailureThresholdElement=xmldoc.createElement("buildStepFailureThreshold");
        Element unstableThresholdElement=xmldoc.createElement("unstableThreshold");
        Element failureThresholdElement=xmldoc.createElement("failureThreshold");
        Element buildAllNodesWithLabelElement=xmldoc.createElement("buildAllNodesWithLabel");
        for(int j=0; j<3; j++)
        {
            Text nameText=xmldoc.createTextNode(String.valueOf(name[j]));
            Text ordinalText=xmldoc.createTextNode(String.valueOf(ordinal[j]));
            Text colorText=xmldoc.createTextNode(String.valueOf(color[j]));
            Text buildText=xmldoc.createTextNode(String.valueOf(build[j]));
            Element nameElement=xmldoc.createElement("name");
            Element ordinalElement=xmldoc.createElement("ordinal");
            Element colorElement=xmldoc.createElement("color");
            Element buildElement=xmldoc.createElement("completeBuild");
            nameElement.appendChild(nameText);
            ordinalElement.appendChild(ordinalText);
            colorElement.appendChild(colorText);
            buildElement.appendChild(buildText);
            switch (j)
            {
                case 0:
                {
                    buildStepFailureThresholdElement.appendChild(nameElement);
                    buildStepFailureThresholdElement.appendChild(ordinalElement);
                    buildStepFailureThresholdElement.appendChild(colorElement);
                    buildStepFailureThresholdElement.appendChild(buildText);break;
                }
                case 1:
                {
                    unstableThresholdElement.appendChild(nameElement);
                    unstableThresholdElement.appendChild(ordinalElement);
                    unstableThresholdElement.appendChild(colorElement);
                    unstableThresholdElement.appendChild(buildText);break;
                }
                case 2:
                {
                    failureThresholdElement.appendChild(nameElement);
                    failureThresholdElement.appendChild(ordinalElement);
                    failureThresholdElement.appendChild(colorElement);
                    failureThresholdElement.appendChild(buildText);break;
                }
            }
        }
        String temp=new String();
        if(check==false)
            temp=stepinlineid.get(i);
        else if(check==true)
            temp=stepelseinlineid.get(i);
        if(!temp.isEmpty())
        {
            boolean present=helper(temp);
            if (present==false)
                selectedlist.add(temp);
            temp=bfappend+temp;
        }
        Text projectText=xmldoc.createTextNode(String.valueOf(temp));
        projectsElement.appendChild(projectText);
        blockElement.appendChild(buildStepFailureThresholdElement);
        blockElement.appendChild(unstableThresholdElement);
        blockElement.appendChild(failureThresholdElement);
        blockableElement.appendChild(configs2Element);
        blockableElement.appendChild(projectsElement);
        blockableElement.appendChild(conditionElement);
        blockableElement.appendChild(trigger1Element);
        blockableElement.appendChild(trigger2Element);
        blockableElement.appendChild(blockElement);
        blockableElement.appendChild(buildAllNodesWithLabelElement);
        configsElement.appendChild(blockableElement);
        parameterElement.appendChild(configsElement);
        Element element;
        parameterElement.setAttribute("plugin","parameterized-trigger"+"@"+plugins.get("parameterized-trigger"));
        configsElement.setAttribute("class", "empty-list");
        return parameterElement;
    }
    public static String prettyString(String required)
    {
        /*
            Makes all REST requests compliant to the rules of http connections
        */
        required=required.replace(" ", "%20");
        return required;
    }
}