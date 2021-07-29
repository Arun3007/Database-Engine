

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NLQPMain extends JFrame implements ActionListener{

   Container cp;
   JLabel l1,l1_1,l2,l3,l4,l5,l6;
   JTextField inputText;
   Choice c1;
   JTextArea txt1,outputText;
   JScrollPane jsp1,jsp2;
   List list1;
   JButton b1,b2,b3,open_button;
   Font f1=new Font("Algerian",Font.BOLD,25);
   Font f2=new Font("Berlin Sans FB Demi",Font.BOLD,14);
    JLabel lbg;
    ImageIcon ic;
   
   String username;
   FileWriter fstream ;
   BufferedWriter out;
   String conn_string;
   
   JLabel lblbanner;
   
    String [] databases={"jdbc:mysql://localhost:3306/company","jdbc:mysql://localhost:3306/medical"};
    String [] databases_selection={"IT COMPANY DATABASE","HOSPITAL DATABASE"};
    String [] userNames = {"root","root"};
    String [] passwords = {"",""};
    String [] tableLists;// = {"employee;department;deptlocations;workson;project;dependent"};

    NLQEngine engine = null;

    public NLQPMain(String uname) 
    {
           this.username=uname;
           setSize(590,750);
           setTitle("Natural Langauge To Database Interface ");
           setLocation(400,80); 
           	setResizable(false);

          cp=getContentPane();
          cp.setLayout(null);

         lblbanner=new JLabel("");
         ImageIcon ic1=new ImageIcon("Sunset.jpg");
         
         lblbanner.setIcon(ic1);
         lblbanner.setBounds(0,0,800,600);

          l1=new JLabel("User Natural Langauge To SQL ");
         cp.add(l1);
         l1.setFont(f1);
 
         l1_1=new JLabel("Generating Trasitional Logical Query");
         cp.add(l1_1);
          l1_1.setFont(f1);
          
         l2=new JLabel("Select Database:");
         cp.add(l2);
         l2.setFont(f2);

         list1=new List(3,false);
         cp.add(list1);
         for(int i=0;i<databases.length;i++)
               list1.add(databases_selection[i]);
        list1.addMouseListener(new MouseSelect1());
        
         l3=new JLabel("Select Example:");
         cp.add(l3);
         l3.setFont(f2);
         
         c1=new Choice();
         cp.add(c1);
         c1.addItem("Examples      ");
        for (int i=(examples.length - 1); i>=0; i--) 
        	 c1.insert(examples[i], 1);
         c1.addItemListener(new ChoiceListener());
         
           l4=new JLabel("Write Your Natural Language  :  ");
           cp.add(l4);
           l4.setFont(f2);
         
          inputText=new JTextField(10);
         cp.add(inputText);
	

         l5=new JLabel("Generated Sql Query  :  ");
         cp.add(l5);
        l5.setFont(f2);

         txt1=new JTextArea(50,100);
         jsp1=new JScrollPane(txt1);
         cp.add(jsp1);
       

         l6=new JLabel("Output  :  ");
         cp.add(l6);
        l6.setFont(f2);

         outputText=new JTextArea(250,100);
          jsp2=new JScrollPane(outputText);
         cp.add(jsp2);

         b1=new JButton("DO QUERY");
         cp.add(b1);
         b1.addMouseListener(new MouseQuery());

         

         b3=new JButton("LOGOUT");
         cp.add(b3);
         b3.addActionListener(this);
         
         
        lbg=new JLabel("");
	    ic=new ImageIcon("img.jpg");
	    lbg.setBounds(0,0,590,750);
	    lbg.setIcon(ic);
	    add(lbg);
         
         

         l1.setBounds(80,10,500,40);//banner 1
         l1_1.setBounds(20,60,550,40);//banner 2
        // cp.add(lblbanner);
        
        	
         	
         l2.setBounds(80,130,150,30);//label of database
         list1.setBounds(80,160,450,100); //list database
         l3.setBounds(80,270,150,30);//example label
         c1.setBounds(80,300,450,30);//examples
         l4.setBounds(80,330,230,30);//query label		
         inputText.setBounds(80,360,450,30); //input of query
         l5.setBounds(80,430,260,30);//generate sql query label
         jsp1.setBounds(80,460,450,60);      
         l6.setBounds(80,530,230,30);//result label
         jsp2.setBounds(80,560,450,110);
         
         b1.setBounds(430,400,100,30);
        
         
         b3.setBounds(430,680,100,30);    
       
        
        
       
       
          setVisible(true);
		  
		  
          setDefaultCloseOperation(3);	
     }
     
     public boolean choiceChanged = false;
     
      class ChoiceListener implements ItemListener {
        public void itemStateChanged(ItemEvent ie) {
            System.out.println("choice menu: " + ie.paramString());
            System.out.println("choice menu: " + (String)ie.getItem());
            String sel = (String)ie.getItem();
            if (sel.equals("Examples"))  return;
            inText(sel, true);
            if (choiceChanged==false) {
                c1.remove(0);
                choiceChanged=true;
            }
            System.out.println("choice menu: <returning>");
        }
    }
    
    
    
   public static void main(String args[])
  {
         UIManager.LookAndFeelInfo info[];
         info=UIManager.getInstalledLookAndFeels();

         String name=info[3].getClassName();

         try
            {
                UIManager.setLookAndFeel(name);
            }
        catch(Exception e)
           {
                    System.out.println(e);
           }       

        NLQPMain n=new NLQPMain("admin");
  }
  
  class MouseQuery extends MouseAdapter implements Serializable {
        // The magic: access the public query method in the
        // containing class:
        synchronized public void mouseReleased(MouseEvent mevt) {
            query();
        }
    }
    
    public void query() {
        txt1.setText("");
        String a_query = inText("", false);//display salary of fraklin
        System.out.println("query(): a_query=" + a_query);
        String sql_query=null;
        
            engine.parse(a_query);
            sql_query = engine.getSQL();
        
        if (sql_query==null) {
            System.out.println("No SQL for " + a_query);
            return;
        }
        txt1.setText(sql_query);//place the sql query obtain in the generate SQL field
		
        try {
            outputText.setText("");
			jsp2.getViewport().setViewPosition(new Point(0,0));
			putText("\nQuery results:\n");
            String data = engine.getRows(sql_query, conn_string, userNames[0], passwords[0]);
            int test=engine.isstar();
           // System.out.println("THE TEST DATA IS>>>>>>>>>>>>"+test);
            if(test==1)
            {
            			
                        String tablename=engine.gettablename();
                    System.out.println("The fected table name is ................."+tablename);
            
             String metadata[]=engine.getColumnNames(tablename, conn_string, userNames[0], passwords[0]);
            //System.out.println("metadata is"+metadata[0]);
            System.out.println("before english data is :"+data);
            
                        //System.out.println("GSGSGGGSGS............................");
                        
            		putText(engine.toEnglishall(data,metadata) + "\n");
            	
            }
            else
             {			
                String output_is=engine.toEnglish(data);
				System.out.println("The final engine.toEnglish output is : "+output_is);
				putText(output_is+ "\n");
			 }	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   void putText(String s) {
        outputText.append(s);
        
    }
    
    
    synchronized public String inText(String new_val, boolean set_flag) {
        if (set_flag) {
            inputText.setText(new_val);
           
            
            return "";
        }
        return inputText.getText();
    }
    
     private void addSynonym(String def_string) {
        int pos = def_string.indexOf("=");
        String description = def_string.substring(0,pos);
        String column = def_string.substring(pos+1);
        if (engine!=null) {
            engine.addSynonym(column, description);
        }
    }
  
  public class MouseSelect1 extends MouseAdapter implements Serializable
{
       public void mouseReleased(MouseEvent me)
            {
                 
                 String selection_data=(String)list1.getSelectedItem();
                 if(selection_data.equalsIgnoreCase("IT COMPANY DATABASE"))
                 {
                 	conn_string="jdbc:mysql://localhost:3306/company";
                 }
                 if(selection_data.equalsIgnoreCase("HOSPITAL DATABASE"))
                 {
                 	conn_string="jdbc:mysql://localhost:3306/medical";
                 }
                 //conn_string=list1.getSelectedItem();
                 int index=list1.getSelectedIndex();
                 System.out.println("INDEX IS "+index);
                 
                 try
                 {
                     Class.forName("com.mysql.jdbc.Driver");
                     String url = conn_string;
					 Connection con =DriverManager.getConnection(url,"root",passwords[0]);
                     DatabaseMetaData db_data=con.getMetaData();
                       
                     ResultSet rs_rows=db_data.getTables(null,null,"%",null);
                     ResultSet rs=db_data.getTables(null,null,"%",null);
                     String table_name="";
                     int num_of_tables=0;
                     while(rs_rows.next())
                       {
                       	 if(table_name.indexOf("MSys")>=0)
                            	continue;
                          num_of_tables++;  	
                       	 
                       }
                       System.out.println("NUM OF TABLES ARE "+num_of_tables);
                      tableLists=new String[2];
                      tableLists[index]="";
                      int i=0; 	
                     while(rs.next())
                       {
                            table_name=rs.getString(3);
                            if(table_name.indexOf("MSys")>=0)
                            	continue;
                            
                            tableLists[index]+=table_name+",";	   
                       }
                    con.close();                                                        
                                     	 
                   tableLists[index]=tableLists[index].substring(0,tableLists[index].length()-1);
                   System.out.println("LIST IS "+tableLists[index]);
                   String data []=tableLists[index].split(",");
                   String newlist="";
                   for(int m=data.length-1;m>=0;m--)
                   {
                   	if(newlist.equals(""))
                   	{
                   		newlist=data[m];
                   	}
                   	else
                   		newlist=newlist+","+data[m];
                   }
                   tableLists[index]=newlist;
                   	
                   	//System.out.println("SDSDSDDSSDD   "+newlist);
                 }
                 catch(Exception e)
                 {
                 	System.out.println(e);
                 }
                 
                 
                engine = new NLQEngine();
                engine.reset_synonyms();
                System.out.println("SELECTED INDEX IS "+list1.getSelectedIndex());
                if(list1.getSelectedIndex()==0)
                {
                   c1.removeAll();
        	        c1.addItem("Examples                                        ");
        	        for (int i=(examples.length - 1); i>=0; i--) 
        	               c1.insert(examples[i], 1);
                   for (int i=0; i<synonyms.length; i++) 
        	          addSynonym(synonyms[i]);//calling to separate the synonynm[]
                }       
        	    else 
        	    {
        	        c1.removeAll();
        	        c1.addItem("Examples                                        ");
        	        for (int i=(examples_medical.length - 1); i>=0; i--) 
        	               c1.insert(examples_medical[i], 1);	
        	    
                   for (int i=0; i<synonyms_medical.length; i++) 
        	          addSynonym(synonyms_medical[i]);//calling to separate the synonynm[]
        	    }
                    System.out.println("............................");
		            System.out.println(databases[index]+" "+userNames[index]+" "+passwords[index]+" "+tableLists[index]);
		            engine.addDB(databases[index], userNames[index], passwords[index], tableLists[index]);
		        
		        engine.initDB();  
                     
               engine.select_db_set(databases[index]);

            }
}            



public void actionPerformed(ActionEvent ae)
       {
            if(ae.getSource()==b3)
            {
                this.setVisible(false);
               NLQPAdmin_Login o=new NLQPAdmin_Login();
            
            }
            
            
        }



String [] examples = {
    	"display salary of franklin",
    	"list information of Salary in employee",
        "list social_security_no where firstname equals ramesh",
        "pick dnumber where deptname equals research",
        "list relationship where dessn equals 123456789",
        "list firstname, middlename and lastname where ssn equals 888665555",
        "retrive dependentname where depend_essn equals 333445555  ",
        "list gender where ssn equals 333445555",
        "list relationship where gender equals F",
        "list dname where dnumber equals 5",
        "what is salary whose firstname is Franklin",
        "what is salary of franklin",
        "what is firstname, middlename and lastname whose ssn is 888665555",
        "what is dnumber where deptname is research",
        "what is projectname whose projectnumber is 10",
        "list fname where salary is less than 40000",
        "list salary where bdate is after April 1, 1941 ",
        "list ssn where bdate is after April 1, 1941",
        "what is ssn whose bdate is after June 1, 1965",
        "list salary where bdate is after April 1, 1941 and fname is john",
        "list salary where bdate is after April 1, 1941 or fname is james",
        "list gender where bdate is after April 1, 1941 or fname is james",
        "list gender where bdate is after April 1, 1941 or fname is jennifier",
        "list gender where bdate is after April 1, 1941 and fname is alicia",
        "list info of projectname whose projectnumber is  greater than 20",
        "what is projectname whose projectnumber is  greater than 20",
        "what is projectname whose projectnumber is smaller than 10",
        "what is wwessn whose hours is more than 10",
        "name the projectlocation whose pnumber is more than 10",
        "name the projectlocation whose pnumber is less than 10",
        "name the projectlocation whose pnumber is 10",
        "name the projectname whose projectlocation is houston",
        "name the projectname whose projectlocation is stafford",
        "name the depname whose rel is spouse",
        "name the depname and dessn whose gender is f"     	
 
        	
        
    };

String [] examples_medical = {
    	"list patient_name where patient_id equals 1003",
    	"list departmentname where dptno equals 01",
        "display patient_name",
        "list name-of-patient where patientgender is M",
        "retrive name-of-patient where patientage is less than 30 and patientgender is M",
        "list name-of-patient where patientage is greater than 30"
            
    };


  //list salary whose deptname is headquartes
  //list salary whose deptname is administrator
  
  
    /**
     * Set up properties for synonyms:
     */
    String [] synonyms = {
        "firstname=fname",
        "lastname=lname",
        "middlename=minit",
        "manager_ssn=Mgrssn",
        "manager ssn=Mgrssn",
        "deptname=dname",
        "department name=dname",
        "social_security_no=ssn",
        "address=address",
        "super_social_no=super_ssn",
        "edeptnumber=dno",
        "department_number=dno",
        "deptlocation=dlocation",
        "wrksprojectno=	pno",
        "projectnumber=pnumber",
        "projectlocation=plocation",
        "projectname=pname",
        "wrks_essn=wessn",
        "depend_essn=dessn",
        "depname=dependentname",
        "sal=salary",
        "income=salary",
        "rel=relationship",
        "birthdate=bdate",
        "dnumber=dno"	
    };

   String [] synonyms_medical={
					"doctor_name=doctorname",
					"doc_name=doctorname",
					"name_of_doctor=doctorname",
					"doctor=doctorname",
					"deptname=departmentname",
					"dept_name=departmentname",
					"dept-name=departmentname",
					"department_name=departmentname",
					"department-name=departmentname",
					"department=departmentname",
					"department_no=dptno",
					"department_num=dptno",
					"department-no=dptno",
					"department-num=dptno",
					"department_number=dptno",
					"departmentno=dptno",
					"departmentnoum=dptno",
					"departmentnumber=dptno",
					"numberofdepartment=dptno",
					"number_of_department=dptno",
					"number-of-department=dptno",
					"docotor_id=doc_id",
					"docotorid=doc_id",
					"docid=doc_id",
					"docotor_serial_num=doc_id",
					"docotor_serial_no=doc_id",
					"docotor_serial_number=doc_id",
					"docotor_serialnum=doc_id",
					"docotor_serialnumber=doc_id",
					"docotorserialnum=doc_id",
					"docotorserialno=doc_id",
					"patientname=name",
					"patient_name=name",
					"patient-name=name",
					"name-of-patient=name",
					"patient_id=patientid",
					"patient-id=patientid",
					"patientnumber=patientid",
					"patient_number=patientid",
					"patient-number=patientid",
					"patient_serial_number=patientid",
					"patient_serialnum=patientid",
					"patient_serialnumber=patientid",
					"patientserialnum=patientid",
					"patientserialno=patientid",
					"patientregisterno=patientid",
					"patientregisternum=patientid",
					"patientregisternumber=patientid",
					"patient_registerno=patientid",
					"patient-registerno=patientid",
					"patient_register_no=patientid",
					"patientage=age",
					"patient-age=age",
					"patient_age=age",
					"patient_sex=sex",
					"patientsex=sex",
					"patient_gender=sex",
					"patientgender=sex",
					"patient-gender=sex",
					"phoneno=phone_number",
					"phonenumber=phone_number",
					"phno=phone_number",
					"ph_no=phone_number",
					"ph.no=phone_number",
					"phone_no=phone_number",
					"phone-no=phone_number",
					"tellephonenumber=phone_number",
					"tellephone_number=phone_number",
					"tellephoneno=phone_number",
					"admitted_date=Date_of_admitted",
					"admitteddate=Date_of_admitted",
					"admitted-date=Date_of_admitted",
					"admitted=Date_of_admitted",
					"hospitalised=Date_of_admitted",
					"hospitalised_date=Date_of_admitted",
					"hospitalised-date=Date_of_admitted",
					"discharge_date=Date_of_discharge",
					"dischargeddate=Date_of_discharge",
					"discharge-date=Date_of_discharge",
					"discharged=Date_of_discharge"
   	
   };

}