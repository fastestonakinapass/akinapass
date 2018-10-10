/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.Jenkinster;
import com.buildforge.services.client.api.APIClientConnection;
import static com.buildforge.services.client.dbo.Environment.findByUuid;
import com.buildforge.services.client.dbo.Project;
import com.buildforge.services.client.dbo.Selector;
import com.buildforge.services.client.dbo.SelectorProperty;
import com.buildforge.services.client.dbo.Server;
import com.buildforge.services.client.dbo.ServerTestResult;
import com.buildforge.services.client.dbo.Snapshot;
import com.buildforge.services.client.dbo.SnapshotRequest;
import com.buildforge.services.client.dbo.Step;
import com.buildforge.services.common.ServiceException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author shirsak.s
 */
public class BuildforgeLogic extends JenkinsterGUI
{
    public static void setDateTime()
	{
            /*
                Sets the date and time of clicking export button to the new Snapshot issued to the Buildforge project to be exported
            */
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("_dd_MM_yyyy_HH_mm_ss");  
		LocalDateTime now = LocalDateTime.now();
		snapshot="Export"+dtf.format(now);
	}    
    public static Snapshot createDefaultSnapshot(APIClientConnection connectionObject, String projectname) throws IOException, ServiceException 
    {
        /*
            Creates snapshot for a Buildforge project
        */
	Snapshot snap = Snapshot.createSnapshot(connectionObject, snapshot , "Snapshot for the Export");
	System.out.println("..............Snaphot data..................");
	System.out.println("Name:" + snap.getName());
	//System.out.println("Name:" + snap.getUuid());
        uuid=snap.getName();
	//System.out.println("Name:" + snap.getComment());
	//System.out.println("Created Snapshot");
	return snap;
    }    
    public static APIClientConnection apiConnect(String host, int port, String user, String password) throws IOException, ServiceException
    {
        /*
            Establishes Connection to Buildforge services layer
        */
	APIClientConnection conn = new APIClientConnection(host, port);
	conn.authUser(user, password);
	//System.out.println("Succesfully Connected to BuildForge Services Layer :)");
	return conn;
    }
    public static void apiDisconnect(APIClientConnection obj) throws IOException
        {
            /*
                De-establishes Connection to Buildforge services layer
            */
            obj.close();
            //System.out.println("Connection Closed");
        }	
    public static void listProjects(APIClientConnection obj) throws IOException, ServiceException
	{
            /*
                Gets all Buildforge projects
            */
            //System.out.println("Listing Projects");
            List<Project> projects=Project.findAll(obj);
            list=new ArrayList<>();
            /*for(Project proj: projects)
            {
		list.add(proj.getName());
            }*/
            Map<String, String> names =Project.findAllNamesAndUuids(obj);
            for (Map.Entry<String, String> entry : names.entrySet()) 
            {
		//System.out.println(entry.getKey() + ":" + entry.getValue());
                list.add(entry.getKey());
            }
            model=new DefaultTableModel()
            {
            @Override
            public Class<?> getColumnClass(int column)
            {
                /*        public Class<?> getColumnClass(int column)
                */        switch(column)
            {
                case 0:
                  return Boolean.class;
                case 1:
                  return Integer.class;
                case 2:
                  return String.class;

                default:
                  return String.class;
            }
            }
            };
        }
    public static void showListedProjects()
        {
            /*
                Lists all Buildforge projects to GUI frame
            */
            model.addColumn("Select");
            model.addColumn("Id");
            model.addColumn("Project");
            for(int i=0;i<list.size();i++)
            {
                model.addRow(new Object[0]);
                
                model.setValueAt(checkall,i,0);
                model.setValueAt(i+1,i,1);
                model.setValueAt(list.get(i), i, 2);
            }            
            //list.clear();
            
            //getSelected(conn);
            
            /*model.setRowCount(0);
            Object rowData[]=new Object[3];
            for(int i=0;i<list.size();i++)
                {
                    rowData[0]=i+1;
                    rowData[1]=null;
                    rowData[2]=list.get(i);
                    model.addRow(rowData);
                }*/
	}    
    public static void getSelected(APIClientConnection obj)
        {
            selectedlist=new ArrayList<>();
            for(int i=0;i<list.size();i++)
            {                
                if((boolean)model.getValueAt(i,0)==true)
                {
                    selectedlist.add((String) model.getValueAt(i,2));
                }
            }
            /*for(int i=0;i<selectedlist.size(); i++)
            {
                //System.out.println(selectedlist.get(i));
            }*/
        }    
    public static void importProjects(APIClientConnection obj, int j) throws IOException, ServiceException, Exception
    {
        /*
            Big Fat Import Logic
            
            - attach snapshots to required projects
            - initialise arraylists
            - get and set environment variables
            - add passchain and failchain projects to queue of exports
            - identify platforms of the steps in buildforge
            - get commands and store them stepwise
        */
            passchain=new String();
            failchain=new String();
            projectname=selectedlist.get(j);
            Snapshot s=createDefaultSnapshot(obj, projectname);
            Project project=Project.findByName(obj,projectname);
            String environment=new String();
                    try
                    {
                        project.snapshot(s.getUuid(), new SnapshotRequest(false));
                        String environmentuuid=new String();
                        //System.out.println(environmentid+" : "+environmentuuid);
                        environment=new String();                    
                        environmentuuid=project.getEnvironmentUuid();
                        environment=String.valueOf(findByUuid(obj,environmentuuid));
                    }
                    catch(NullPointerException e)
                    {
                        
                    }
                    catch(com.buildforge.services.common.api.APIException e)
                    {
                        
                    }
                    map=new HashMap<String,String>();
                    StringBuffer getparamName=new StringBuffer();
                    StringBuffer getparamValue=new StringBuffer();
                    if(environment.length()!=0)
                    for(int i=0; i<environment.length()-12; i++)
                    {
                        String check=environment.substring(i,i+9);
                        if(check.equals("paramName"))
                        {
                            int k=i+10;
                            while(environment.charAt(k)!=',')
                            {
                                getparamName.append(environment.charAt(k));
                                k++;
                            } 
                            k+=12;
                            while(environment.charAt(k)!=',')
                            {
                                getparamValue.append(environment.charAt(k));
                                k++;
                            }
                            i=k;
                            map.put(String.valueOf(getparamName),String.valueOf(getparamValue));
                            getparamName=new StringBuffer();
                            getparamValue=new StringBuffer();
                        }
                    }
                    try
                    {
                    //System.out.println("Pass Chain : "+project.getPassChainUuid());
                    //System.out.println("Fail Chain : "+project.getFailChainUuid());
                    //System.out.println("Selector : "  +project.getSelectorUuid());
                    }
                    catch(NullPointerException e){}
                    try
                    {
                    if(project.getPassChainUuid()!=null)
                    {
                        passchain=bfappend+Project.findByUuid(obj,project.getPassChainUuid()).getName().toString();
                        boolean present=helper(Project.findByUuid(obj,project.getPassChainUuid()).getName().toString());
                        if (present==false)
                            selectedlist.add(Project.findByUuid(obj,project.getPassChainUuid()).getName().toString());
                    }
                    if(project.getFailChainUuid()!=null)
                    {
                        failchain=bfappend+Project.findByUuid(obj,project.getFailChainUuid()).getName().toString();
                        boolean present=helper(Project.findByUuid(obj,project.getFailChainUuid()).getName().toString());
                        if (present==false)
                            selectedlist.add(Project.findByUuid(obj,project.getFailChainUuid()).getName().toString());
                    }
                    }
                    catch(NullPointerException e){}
                    List<Step> steps=new ArrayList<>();
                    try
                    {
                    steps=project.getSteps();
                    }
                    catch(NullPointerException e){}
                    stepcollector=new ArrayList<String>();
                    stepselectorid=new ArrayList<String>();
                    stepselectorenvironment=new ArrayList<String>();
                    steppasschainid=new ArrayList<String>();
                    stepfailchainid=new ArrayList<String>();
                    stepinlineid=new ArrayList<String>();
                    stepelseinlineid=new ArrayList<String>();
                    stepselectorserver=new ArrayList<String>();
                    newcommands=new ArrayList<String>();
                    newcondition=new ArrayList<String>();
                    newelsecommands=new ArrayList<String>();
                    windows=new ArrayList<String>();
                    for(Step step : steps)
                    {
                        System.out.println("---------------");                        
                        if(step.getSelectorUuid()==null)
                            windows.add("");
                        else
                        {
                            Selector selector=Selector.findByUuid(obj,step.getSelectorUuid());                        
                            for(SelectorProperty selectorProperty : selector.getProperties())
                            {
                                System.out.println(selectorProperty.getPropertyValue());
                                System.out.println("-----------");
                                Server server=Server.findByName(obj,selectorProperty.getPropertyValue());
                                System.out.println("-----");
                                System.out.println(server);
                                if(server==null)
                                {
                                    windows.add("");break;
                                }
                                if(server.getUuid()==null)
                                {
                                    windows.add("");break;
                                }
                                else
                                {
                                    String serveruuid=server.getUuid();
                                    ServerTestResult serverTestResult=ServerTestResult.findByServerUuid(obj, serveruuid);
                                    String platform=new String();
                                    try
                                    {
                                    platform=serverTestResult.getPlatform();
                                    }
                                    catch(NullPointerException e)
                                    {windows.add("");}
                                    System.out.println(platform+"\n\n");
                                    if(platform.contains("Windows"))
                                        windows.add("");
                                    else
                                        windows.add("linux");
                                }
                            }
                        }
                        stepselectorenvironment.add(step.getSelectorUuid());
                        steppasschainid.add(step.getPassChainUuid());
                        stepfailchainid.add(step.getFailChainUuid());
                        if(step.getInlineChainUuid()!=null)
                        stepinlineid.add(Project.findByUuid(obj, step.getInlineChainUuid()).getName());
                        else
                        stepinlineid.add("");
                        if(step.getElseInlineUuid()!=null)
                        stepelseinlineid.add(Project.findByUuid(obj, step.getElseInlineUuid()).getName());
                        else
                        stepelseinlineid.add("");
                        newcommands.add(step.getCommandText());
                        if(step.getConditionText().isEmpty())
                            newcondition.add(step.getConditionText());
                        else
                            newcondition.add(step.getConditionText());
                        if(step.getElseCommandText().isEmpty())
                            newelsecommands.add(step.getElseCommandText());
                        else
                            newelsecommands.add(step.getElseCommandText());
                    }
                    
                    for(int i=0; i<windows.size(); i++)
                    {
                        System.out.println(windows.get(i));
                    }
/* Commented on 25-sept-2018

                    for(int i=0;i<steps.size();i++)
                    {
                        try
                        {
                        stepselectorid.add(project.getStep(i).getSelectorUuid());
                        
                        System.out.println(project.getStep(i).getSelectorUuid());
                        Selector selector=Selector.findByUuid(obj,project.getStep(i).getSelectorUuid());
                        SelectorProperty selectorproperty=new SelectorProperty(obj,selector);
                        System.out.println(selectorproperty.toString());
                        }
                        catch(APIException e)
                        {
                            
                        }
                        stepselectorenvironment.add(project.getStep(i).getSelectorUuid());
                        //System.out.println(stepselectorenvironment.get(i));
                        steppasschainid.add(project.getStep(i).getPassChainUuid());
                        //System.out.println(steppasschainid.get(i));
                            if(steppasschainid.get(i)!=null)
                            {
                                selectedlist.add((Project.findByUuid(obj, steppasschainid.get(i))).toString());
                            }
                        stepfailchainid.add(project.getStep(i).getFailChainUuid());
                        //System.out.println(stepfailchainid.get(i));
                        newcommands.add(steps.get(i).getCommandText());
                    }
                    
 Commented on 25-sept-2018 */                    
                    //if(project.getPassChainUuid()!=null)
                        //selectedlist.add((Project.findByUuid(obj, project.getPassChainUuid())).toString());
            //XMLParsing.createTemp();
            //String send="-f "+realfilepath+" -n "+projectname+" -U "+jTextField1.getText()+" -P "+jPasswordField1.getPassword()+" -p "+" -i "+uuid;
            //String send="-f C:\\Users\\shirsak.s\\AppData\\Local\\Temp\\config5282222001209240195.xml -n emptyJob -U root -P root -p  -i emptyJobSnapshotExport_06_09_2018_17_11_28";
            /*StringBuilder sb=new StringBuilder();
            sb.append("-f ");
            sb.append(absolutePath);
            sb.append(" -n ");
            sb.append(projectname);            
            sb.append(" -i ");
            sb.append(uuid);
            sb.append(" -U ");
            sb.append(bfusername);
            sb.append(" -P ");
            sb.append(bfpassword);
            sb.append(" -pt ");
            String send=sb.toString();
            System.out.println(send);
            String [] str = send.split(" ");
            BFExporter export=new BFExporter(obj);
            export.realMain(str);*/
    }
    public static boolean helper(String check)
    {
        /*
            Checks for multiples entries of same projects to be exported
        */
        for(int i=0; i<selectedlist.size(); i++)
        {
            if (check.equals(selectedlist.get(i)))
                    return true;
        }
        return false;
    }
}
