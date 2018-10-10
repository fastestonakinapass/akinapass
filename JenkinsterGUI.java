/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.Jenkinster;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger; 
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import com.offbytwo.jenkins.*;
import com.buildforge.services.common.ServiceException;
import com.buildforge.services.client.api.APIClientConnection;
import com.buildforge.services.client.dbo.Step;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author shirsak sahoo
 */
public class JenkinsterGUI extends javax.swing.JFrame {
    /**
     * Creates new form JenkinsterGUI
     */
    public static final byte[] BUFFER = new byte[8192];
    static APIClientConnection conn;
    static JenkinsServer jenkins;
    static String jenkinsuser;
    static String jenkinstoken;
    static String jenkinsurl;
    static String bfappend;
    static String bfusername;
    static char[] bfpassword;
    static String bfurl;
    static int bfport;
    static Boolean checkall=false;
    static Boolean checkallA=false;
    static String currentjob;
    static String uuid;
    static DefaultTableModel model;
    static DefaultTableModel modelA;
    static ArrayList<String> list;
    static ArrayList<String> listA;
    static ArrayList<String> selectedlist=null;
    static ArrayList<String> selectedlistA=null;
    static ArrayList<ArrayList<String>> nameUUID;
    static String snapshot="";
    static String absolutePath;
    static String tempFilePath;
    static String filename;
    static String filepath;
    static String realfilepath;
    static boolean end;
    static ArrayList<ArrayList<String>> commands;
    static String action;
    static String description;
    static boolean keepDependencies;
    static String properties;
    static String scm;
    static String scmAttribute;
    static boolean canRoam;
    static boolean disabled;
    static boolean blockBuildWhenDownstreamBuilding;
    static boolean blockBuildWhenUpstreamBuilding;
    static String triggers;
    static boolean concurrentBuild;
    static boolean completeBuild;
    static String name;
    static int ordinal;
    static String color;
    static String builders;
    static String batchFile;
    static String publishers;
    static String buildWrappers;
    static String[] command;
    static String projectname;
    static HashMap<String,String> map;
    static HashMap<String,String> plugins;
    static List<Step> stepList;
    static ArrayList<String> stepcollector;
    static ArrayList<String> stepselectorid;
    static ArrayList<String> stepselectorenvironment;
    static ArrayList<String> steppasschainid;
    static ArrayList<String> stepinlineid;
    static ArrayList<String> stepelseinlineid;
    static ArrayList<String> stepfailchainid;
    static ArrayList<String> newcommands;
    static ArrayList<String> newelsecommands;
    static ArrayList<String> newcondition;
    static ArrayList<String> stepselectorserver;
    static ArrayList<String> windows;
    static String passchain;
    static String failchain;
    public void setDefault() throws FileNotFoundException, IOException
    {
        /*
            Sets default values to the Textfields of the GUI, as per the last instance of run.
            Click on "Set Default" to reuse the entered parameters.
        */
        StringBuffer finalconfig=new StringBuffer();
        finalconfig.append(jTextField1.getText());
        finalconfig.append("\n");
        finalconfig.append(jTextField2.getText());
        finalconfig.append("\n");
        finalconfig.append(jTextField3.getText());
        finalconfig.append("\n");
        finalconfig.append(jTextField4.getText());
        finalconfig.append("\n");
        finalconfig.append(jTextField5.getText());
        finalconfig.append("\n");
        finalconfig.append(jPasswordField1.getPassword());
        finalconfig.append("\n");
        finalconfig.append(jPasswordField2.getPassword());
        finalconfig.append("\n");
        File file=new File(System.getProperty("java.io.tmpdir")+"UserEntryDefault.txt"); 
        boolean exists = file.exists();
        if(exists==false)
        {
            FileWriter writer = new FileWriter(file);
            writer.write("");
            writer.close();
        }
        FileReader filereader=new FileReader(file);
        BufferedReader br=new BufferedReader(filereader);
        String temp=new String();
        temp=br.readLine();
        if(temp!=null)
        jTextField1.setText(temp);
        temp=br.readLine();
        if(temp!=null)
        jTextField2.setText(temp);
        temp=br.readLine();
        if(temp!=null)
        jTextField3.setText(temp);
        temp=br.readLine();
        if(temp!=null)
        jTextField4.setText(temp);
        temp=br.readLine();
        if(temp!=null)
        jTextField5.setText(temp);
        temp=br.readLine();
        if(temp!=null)
        jPasswordField1.setText(temp);
        temp=br.readLine();
        if(temp!=null)
        jPasswordField2.setText(temp);
        FileWriter filewriter=new FileWriter(System.getProperty("java.io.tmpdir")+"UserEntryDefault.txt");
        filewriter.write((""));
        filewriter.write(finalconfig.toString());
        filereader.close();
        filewriter.close();
    }
    public String getStatus(JenkinsServer jenkins) throws IOException
    {
        /*
            To get status of the connection to be made to the Jenkins Server.
            Returns error message to the GUI.
        */
        String authStr=jenkinsuser+":"+jenkinstoken;
	String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));        
        String urlstring=jenkinsurl;
	urlstring+="";
	URL url=new URL(urlstring);
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	connection.setRequestMethod("GET");
	connection.setRequestProperty("Authorization", "Basic " + encoding);
        try
        {
	String response=connection.getResponseMessage();        
        int responsecode=connection.getResponseCode();
        System.out.println(response);
        System.out.println(responsecode);
        return response;
        }
        catch(java.net.UnknownHostException e)
        {
            jTextArea1.setText("Enter valid URI: http://example.com/");
            e.printStackTrace();
        }
        catch(java.net.ConnectException e)
        {
            jTextArea1.setText("Switch on Jenkins Engine!");
            e.printStackTrace();
        }
        return null;
    }
    public JenkinsterGUI() {
        initComponents();
    }    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton5 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Jenkinstein");
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Buildforge"));
        jPanel1.setPreferredSize(new java.awt.Dimension(480, 500));

        jLabel1.setText("Username");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Password");

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Buildforge URL");

        jTextField2.setToolTipText("http://buildforgeurl/");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        jTextField3.setToolTipText("default: 3966");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel4.setText("Port Number");

        jButton1.setText("List Projects");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Export Selected Projects");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Id", "Projects"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jRadioButton1.setText("Select All Projects");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                    .addGap(43, 43, 43)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                        .addComponent(jPasswordField1)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField2)
                                        .addComponent(jTextField3))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))
                            .addComponent(jRadioButton1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(13, 13, 13)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Jenkins"));
        jPanel2.setPreferredSize(new java.awt.Dimension(500, 600));

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel5.setText("Username");

        jPasswordField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField2ActionPerformed(evt);
            }
        });

        jLabel7.setText("Jenkins URL");

        jLabel6.setText("API Token");

        jTextField5.setToolTipText("http://jenkinsurl:8080/");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jButton3.setText("List Projects");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Items"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable2);

        jButton6.setText("SetDefault");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton4.setText("Refresh List");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField6.setText("default : \"Buildforge_Project\"");
        jTextField6.setToolTipText("set -1 to disable appending");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel8.setText("Append");

        jButton7.setText("Delete Selected");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField6)
                            .addComponent(jTextField5)
                            .addComponent(jTextField4)
                            .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setPreferredSize(new java.awt.Dimension(1000, 200));
        jScrollPane1.setViewportView(jTextArea1);

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton5.setText("Close");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jProgressBar1.setForeground(new java.awt.Color(0, 204, 255));
        jProgressBar1.setPreferredSize(new java.awt.Dimension(148, 16));
        jProgressBar1.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(503, 503, 503)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 11, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed
    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed
    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed
    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed
    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed
    private void jPasswordField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField2ActionPerformed
    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jenkinsuser=jTextField4.getText();
        jenkinstoken=String.valueOf(jPasswordField2.getPassword());
        jenkinsurl=jTextField5.getText();
        URI jenkinsuri=null;
        JenkinsServer jenkins=null;
        try
        {
        jenkinsuri=new URI(jenkinsurl);
        }
        catch(URISyntaxException e)
        {
            System.out.println("Enter valid URI: "+e);
            jTextArea1.setText("Enter valid URI: http://example.com/");
        }
        try
        {
        jenkins = new JenkinsServer(jenkinsuri,jenkinsuser,jenkinstoken);
        }
        catch(NullPointerException e)
        {
            jTextArea1.setText("Enter valid URI: http://example.com/");
        }
        try 
        {
            if(getStatus(jenkins)==null)
                {}
            else
                jTextArea1.setText(getStatus(jenkins));
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(JenkinsterGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
	if(jenkins.isRunning()==true)
        {
            try
            {
                JenkinsLogic.listJobs(jenkins);
                modelA=(DefaultTableModel) jTable2.getModel();
                JenkinsLogic.showListedJobs();
                jTextArea1.setText("Connected to Jenkins Server");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try
        {
                conn= BuildforgeLogic.apiConnect(jTextField2.getText(), Integer.parseInt(jTextField3.getText()), jTextField1.getText(), String.valueOf(jPasswordField1.getPassword()));
		BuildforgeLogic.listProjects(conn);
                jTable1.setModel(model);
                BuildforgeLogic.showListedProjects();
                jTextArea1.setText("Connected to Buildforge Services Layer");
		//apiDisconnect(conn);
        }        
        catch (IOException e)
	{
                System.out.println("IO Exception : " +e);
                try
                {
                if(e.getCause().getClass().equals(java.nio.channels.UnresolvedAddressException.class))                    
                    jTextArea1.setText("Cannot resolve address: example.com");
                }
                catch(java.lang.NullPointerException y)
                {
                    jTextArea1.setText("Cannot resolve address: example.com");
                }
                try
                {
                if(e.getCause().getClass().equals(java.net.ConnectException.class))    
                    jTextArea1.setText("Switch on Buildforge Engine!");
                }
                catch(java.lang.NullPointerException y)
                {
                    jTextArea1.setText("Switch on Buildforge Engine!");
                }
		System.out.println("IO Exception : " +e);
	}
	catch (ServiceException e)
	{
		System.out.println("Service Exception : " +e);
                jTextArea1.setText("Incorrect Buildforge Username or Password");
	}
    }//GEN-LAST:event_jButton1ActionPerformed
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            // TODO add your handling code here:
            setDefault();
        } catch (IOException ex) {
            Logger.getLogger(JenkinsterGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:        
        jButton3ActionPerformed(evt);
    }//GEN-LAST:event_jButton4ActionPerformed
    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        checkall=!checkall;
        jButton1ActionPerformed(evt);
    }//GEN-LAST:event_jRadioButton1ActionPerformed
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:                    
                    jProgressBar1.setValue(0);
                    jButton5.setEnabled(false);
                    jButton5.setBackground(SystemColor.inactiveCaption);
                    jButton5.repaint();
                    jTextArea1.setText("Exporting Projects...");
                    if(jTextField6.getText().equals("default : \"Buildforge_Project\""))
                        bfappend="Buildforge_";
                    else if(jTextField6.getText().equals("-1"))
                        bfappend=new String();
                    else
                        bfappend=jTextField6.getText().trim()+"_";
                    bfusername=jTextField1.getText();
                    bfpassword=jPasswordField1.getPassword();
                    jenkinsuser=jTextField4.getText();
                    jenkinstoken=String.valueOf(jPasswordField2.getPassword());
                    jenkinsurl=jTextField5.getText();
                    BuildforgeLogic.setDateTime();
                    jButton3ActionPerformed(evt);
                    BuildforgeLogic.getSelected(conn);
                    try {
                        int progress=0;
                        for(int j=0; j<selectedlist.size(); j++)
                        {
                            JenkinsLogic.getPluginVersion();
                            BuildforgeLogic.importProjects(conn, j);
                            newXMLParsing.readCommands();
                            newXMLParsing.writeXml();
                            JenkinsLogic.exportJobs(jenkins);
                        }
                    } catch (IOException | ServiceException ex) {
                    Logger.getLogger(JenkinsterGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                    Logger.getLogger(JenkinsterGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    jButton3ActionPerformed(evt);
        jButton5.setEnabled(true);
        jButton5.setBackground(UIManager.getColor("Button.background"));
        jButton5.repaint();
        jProgressBar1.setValue(100);
        jTextArea1.setText("Success!");
    }//GEN-LAST:event_jButton2ActionPerformed
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        jProgressBar1.setValue(0);
        JenkinsLogic.getSelected();
        try {
            for(int j=0;j<selectedlistA.size();j++)
            JenkinsLogic.doDelete(j);
        } catch (MalformedURLException ex) {
            Logger.getLogger(JenkinsterGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsterGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        jProgressBar1.setValue(100);
        jTextArea1.setText("Successfully Deleted");
    }//GEN-LAST:event_jButton7ActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JenkinsterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JenkinsterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JenkinsterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JenkinsterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JenkinsterGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}