
import java.sql.*;

/**
 * NLP - a very simple text scanner.
 */
public class NLQP {
	
	 String [] passwords = {"",""};

    public NLQP(DBInfo dbi) {
        dbinfo=dbi;
    }

    final private int AND=1;
    final private int OR=2;
    String slelected_db="";
    
    int newtable=0;
    String second_table,first_table; 
    String join_data;  
    
       // Semantic action codes:
    public final static int NO_OP=0;
    public final static int LIST=1;
    public final static int DELETE_DATA=2;
    public final static int MODIFY_DATA=3;
    public final static int NONE=0;
    public final static int LESS=1;
    public final static int MORE=2;
    public static int listall=0;
    private String show[] = {"show", "list", "display","retrive","pick","what","name"};
    private String noise1[] = {"are","is","is the","data", "info", "information","any", "all", "everything","the","employee"};
    private String where[] = {"where","whose"};
    private String of[] = {"of","from"};
    private String equals[] = {"is","of","equals", "contains","from"};
    private String is[] = {"is"};
    private String equals_is[] = {"equals", "contains", "is"};
    private String after[] = {"after"};
    private String before[] = {"before"};
    private String and[] = {"and"};
    private String or[] = {"or"};
    private String less[] = {"less", "smaller"};
    private String more[] = {"more", "greater", "larger"};
    private String than[] = {"than"};

    private int currentAction=NO_OP;
    private String displayColumnName = null;
    private String searchColumnName = null;
    private String searchString=null;
    private String searchColumnName2 = null;
    private String searchString2=null;
    private String searchColumnName3 = null;
    private String searchString3=null;
    private int conditionMode = 0;
    private String tableName="";

    private SmartDate time_after = null;
    private SmartDate time_before= null;

    private float quantity=-999.0f;
    private String quan;
    private int compareMode=NONE;

    private String currentWords[];
    private int currentWordIndex=0;
    
    
    private DBInfo dbinfo;
    private String [] temp_col_names = new String[11];
    private int num_temp_col_names = 0;

    public int isstar(){
    	return listall;
    }
    
     public String gettablename(){
    	return tableName;
    }

    
    public void parse(String s) {

        // parse a new sentence:
        time_after=null;
		
        currentWords = Util.parseStrings(s);
        if (currentWords==null) {
            System.out.println("Error parsing: " + s);
            return;
        }

        // Check for a REFINED QUERY that builds on the
        // last query (in which case, we do not want to
        // blank-out the slots from the last parse.
        //
        // Support:
        //   Mode 0:
        //         new query
        //
        //   Mode 1:
        //         and <column name>   -- adds a display column name
        //
        //   Mode 2:
        //         and <condition>     -- adds a SQL WHERE condition
        //
        listall=0;
        int mode = 0; // 0->new query
        if (currentWords[0].equalsIgnoreCase("and") ||
            currentWords[0].equalsIgnoreCase("add"))
            {
                if (currentWords.length < 3)   mode=1;
                else                           mode=2;
            }


        System.out.println("parse(" + s + "): number of words in sentence=" + currentWords.length);
        currentWordIndex=0;

        if (mode==0) {
            tableName=null;
           // time_after=null;
           // time_before=null;
            num_temp_col_names = 0;
            currentAction=NO_OP;
            displayColumnName = "*";
            searchColumnName = null;
            searchString="*";
            searchColumnName2 = null;
            searchString2=null;
            searchColumnName3 = null;
            searchString3=null;
        }  else if (mode==1) {
            System.out.println("processing 'add/and <column name>'");
            currentWordIndex++;
            String cname = eatColumnName(null);
            if (cname != null) {
                displayColumnName = displayColumnName + ", " + cname;
                return;
            }
        } else if (mode==2) {
            System.out.println("processing 'and/add <condition>'");
            currentWordIndex++;
            String cname1 = eatColumnName(null);
            if (cname1 != null) {
                System.out.println("   cname1=" + cname1);
                // look for a condition:
                if (eatWord(equals_is)) {
                    System.out.println("  equals_is matched **");
                    if (currentWordIndex < currentWords.length) {
                        searchColumnName2 = cname1;
                        searchString2 = currentWords[currentWordIndex];
                        return;
                    }
                }
            }
            return;
        }


       // if (eatWord(show) == false) return;
        eatWord(show);
        System.out.println("CUUREN WOR INDX SECTION 1: "+ currentWordIndex);
        eatWord(noise1); // optional; also check for column names
        eatWord(of);
        displayColumnName = eatColumnName(null);
         System.out.println("CUUREN WOR INDX "+ currentWordIndex);
        System.out.println("In NLP.parse in Mode 0 After first displayColoumnName Function data is :\n");
        	System.out.println("Colounnames are     : "+displayColumnName);
        	System.out.println("Table names are      : "+tableName);
        System.out.println("\n\n");
        // check for more column names of the form:
        //   <cn>, <cn>, and <cn>
        // NOTE: "," chars are already removed.

        eatWord(and);
        String temp = eatColumnName(null);
        if (temp!=null) {
            displayColumnName = displayColumnName + ", " + temp;
            eatWord(and);
            temp = eatColumnName(null);
            if (temp!=null) {
                displayColumnName = displayColumnName + ", " + temp;
            }
        }
        System.out.println("In NLP.parse in Mode 0 After removal of and in displaye coloumn Section data is :\n");
        System.out.println("Colounnames are     : "+displayColumnName);
        	System.out.println("Table names are      : "+tableName);
        System.out.println("\n\n"); 
        	
            currentAction=LIST;
        if (displayColumnName==null)
         { 
           displayColumnName="*";
           listall=1;
         }
        if(eatWord(where)==true||currentWords.length==1)   // WHERE
        {
			        System.out.println("WHERE REMOVAL IF ");
			        
			        searchColumnName = eatColumnName(null);  // displayColumnName);
			        
			        join_data=null;	
			         System.out.println("CUUREN WOR INDX "+ currentWordIndex);
			        System.out.println("In NLP.parse in Mode 0 After removal of where word data is :\n searchColumnName is = " + searchColumnName+"\n\n");
			        if(searchColumnName!=null)
			        {
								        int temp1 = tableName.indexOf(",");
								if(temp1>=1)        
								{	        
								        System.out.println("temp1 data "+temp1);
			        
							        second_table=tableName.substring(temp1+1).trim();
							        first_table=tableName.substring(0,temp1).trim();
							        System.out.println("SECOND TABLE DATA "+second_table);
							        
							        if(!second_table.equals(first_table))
							        {
							        	System.out.println("ADD SECOND TABLE DATA "+second_table);
							        	newtable=1;
							        	String cols_tb1 []=DBInterface.getColumnNames(first_table, slelected_db,"root", passwords[0]); 
							        	String cols_tb2 []=DBInterface.getColumnNames(second_table, slelected_db,"root", passwords[0]);
							        	int counter1=0,counter2=0,t1=0,t2=0;
							        	System.out.println("TABLE ONE NAME IS "+tableName);
							        	System.out.println("TABLE ONE COLOUMN COUNT IS "+cols_tb1.length);
							        	System.out.println("TABLE TWO COLOUMN COUNT IS "+cols_tb2.length);
							        	if(cols_tb1.length>cols_tb2.length)
							        	{
							        		counter1= cols_tb2.length;
							        		t1=1;
							        		counter2=cols_tb1.length;
							        	}
							        	else
							        	{
							        		t2=1;
							        		counter1= cols_tb1.length;
							        		counter2=cols_tb2.length;	
							        	}	
							        		
							        	for(int i=0;i<counter1;i++)
							        	{
							        		for(int j=0;j<counter2;j++)
							        		{
							        			if(t1==1)
							        			{
							        				System.out.println("COMPARING "+cols_tb2[i]+" and "+cols_tb1[j]);
							        				if(cols_tb2[i].equals(cols_tb1[j]))
								        			{
								        				join_data=cols_tb1[j];
								        				break;
								        				
								        			}
							        			}
							        			else
							        			{
							        				if(cols_tb1[i].equals(cols_tb2[j]))
								        			{
								        				join_data=cols_tb2[j];
								        				break;
								        			}
							        			}	
							        			
							        		}
							        		if(join_data!=null)
							        			break;
							        	}		
							            
							            System.out.println("JOIN COLOUMN NAME IS "+ join_data);
							           // searchColumnName=second_table+"."+join_data;
							        }
							        else
							        	join_data=null;
							        	
			                   }    	
			         }       
			        
			        
			        
			        System.out.println("\n CURRENT ACTION IS LIST AND COMPARE MODE IS NONE\n");
			        currentAction=LIST;
			        
			        
			       if(eatWord(equals)==true)
			       {       
					System.out.println("CUUREN WOR INDX "+ currentWordIndex);
			       	quantity=-999.0f;
			        compareMode=NONE;
			       
			        
			        if (eatWord(less)) {
			        	System.out.println("CUUREN WOR INDX "+ currentWordIndex);//6
			            eatWord(than);  // skip 'than'
			            System.out.println("CUUREN WOR INDX "+ currentWordIndex);//7
			            quan = currentWords[currentWordIndex];
			            System.out.println("quan "+ quan);
			            try {
			                 Float f = new Float(quan);
			               	 quantity = f.floatValue();
			                 compareMode=LESS;
			                 currentWordIndex++;
			                System.out.println("CUUREN WOR INDX "+ currentWordIndex);
			                System.out.println("In NLP.parse in Mode 0 After removal of less than data is : \n");
			                System.out.println("less than data is : " + quantity);
			                System.out.println("\n CURRENT ACTION IS LIST AND COMPARE MODE IS LESS\n");
			                System.out.println("\n\n");
			            } catch (Exception e) { }
			        }
			
			        if (eatWord(more)) {
			            eatWord(than);  // skip 'than'
			            
			            String quan = currentWords[currentWordIndex];
			            try {
			                Float f = new Float(quan);
			                quantity = f.floatValue();
			                compareMode=MORE;
			                currentWordIndex++;
			                System.out.println("In NLP.parse in Mode 0 After removal of less than data is : \n");
			                System.out.println("more than data is : " + quantity);
			                System.out.println("\n CURRENT ACTION IS LIST AND COMPARE MODE IS MORE\n");
			                System.out.println("\n\n");
			            } catch (Exception e) { }
			        }
			
			        if (eatWord(after)) {
			        	System.out.println("CUUREN WOR INDX "+ currentWordIndex);
			        	System.out.println("In NLP.parse in Mode 0 After removal of after : \n");
			            if (currentWords.length > currentWordIndex+2) {
			            	System.out.println("In First if : \n");
			                String test = currentWords[currentWordIndex] + " " +
			                    currentWords[currentWordIndex+1] + " " +
			                    currentWords[currentWordIndex+2];
			                System.out.println("test date passed to Smartdate function is : "+test+"\n");    
			                time_after = new SmartDate(test);
			                if (time_after.valid()==false)  time_after=null;
			                else {
			              		     currentWordIndex+=3;
			                         System.out.println("CUUREN WOR INDX "+ currentWordIndex);
			                       }
			            }
			            if (time_after==null & currentWords.length > currentWordIndex+1) {
			            	System.out.println("\n In Second if : \n");
			                String test = currentWords[currentWordIndex] + " " +
			                    currentWords[currentWordIndex+1];
			                System.out.println("test date passed to Smartdate function is : "+test+"\n");    
			                time_after = new SmartDate(test);
			                if (time_after.valid()==false)  time_after=null;
			                else currentWordIndex+=2;
			            }
			            if (time_after==null & currentWords.length > currentWordIndex) {
			            	System.out.println("\n In Third if : \n");
			                String test = currentWords[currentWordIndex];
			                System.out.println("test date passed to Smartdate function is : "+test+"\n");    
			                time_after = new SmartDate(test);
			                if (time_after.valid()==false)  time_after=null;
			                else currentWordIndex+=1;
			            }
			        }
			        if (time_after!=null) {
			            System.out.println("\n parsed data 'after' time OK:");
			            System.out.println("  year:  " + time_after.getYear());
			            System.out.println("  month: " + time_after.getMonth());
			            System.out.println("  day:   " + time_after.getDayOfMonth()+"\n\n");
			            
			        }
			        
			
			
			        if (eatWord(before)) {
			        	System.out.println("In NLP.parse in Mode 0 After removal of before : \n");
			            if (currentWords.length > currentWordIndex+2) {
			            		System.out.println("In First if : \n");
			                String test = currentWords[currentWordIndex] + " " +
			                    currentWords[currentWordIndex+1] + " " +
			                    currentWords[currentWordIndex+2];
			                time_before = new SmartDate(test);
			                System.out.println("test date passed to Smartdate function is : "+test+"\n");   
			                if (time_before.valid()==false)  time_before=null;
			                else currentWordIndex+=3;
			            }
			            if (time_before==null & currentWords.length > currentWordIndex+1) {
			            	System.out.println("In Second if : \n");
			                String test = currentWords[currentWordIndex] + " " +
			                    currentWords[currentWordIndex+1];
			                time_before = new SmartDate(test);
			                System.out.println("test date passed to Smartdate function is : "+test+"\n");   
			                if (time_before.valid()==false)  time_before=null;
			                else currentWordIndex+=2;
			            }
			            if (time_before==null & currentWords.length > currentWordIndex) {
			            	System.out.println("In Third if : \n");
			                String test = currentWords[currentWordIndex];
			                time_before = new SmartDate(test);
			                System.out.println("test date passed to Smartdate function is : "+test+"\n");   
			                if (time_before.valid()==false)  time_before=null;
			                else currentWordIndex+=1;
			            }
			        }
			        if (time_before!=null) {
			            System.out.println("parsed 'before' time OK:");
			            System.out.println("  year:  " + time_before.getYear());
			            System.out.println("  month: " + time_before.getMonth());
			            System.out.println("  day:   " + time_before.getDayOfMonth()+"\n\n");
			        }
			
			
			       System.out.println("\n CURRENT ACTION IS LIST AND CONDITION MODE IS 0\n");
			        System.out.println("\nWOPR INDEX DATA "+currentWordIndex);
			        	
			        conditionMode = 0;
			
			        if (searchColumnName==null&&currentWords.length>1) 
					 return;
			        				  
			        
			        if (eatWord(and)) {   // check for AND condition
			            System.out.println("In NLP.parse in Mode 0 After removal of and in search coloumn section: \n");
			            System.out.println("processing 'and/add <condition>'\n\n");
			            String cname1 = eatColumnName(null);
			            if (cname1 != null) {
			                System.out.println("the search coloumn data is cname1= " + cname1);
			                // look for a condition:
			                if (eatWord(equals_is)) {
			                    System.out.println("  equals_is matched **");
			                    if (currentWordIndex < currentWords.length) {
			                        searchColumnName2 = cname1;
			                        searchString2 = currentWords[currentWordIndex];
			                        conditionMode = AND;
			                        currentWordIndex+=1;
			                    }
			                }
			                System.out.println("searchColumnName2= " + cname1);
			                System.out.println("searchString2= " + searchString2);
			                System.out.println("\n CURRENT ACTION IS LIST AND CONDITION MODE IS AND\n");
			            }
			        }
			        if (eatWord(or)) {   // check for OR condition
			            System.out.println("processing 'and/add <condition>'");
			            String cname1 = eatColumnName(null);
			            if (cname1 != null) {
			                System.out.println("   cname1=" + cname1);
			                // look for a condition:
			                if (eatWord(equals_is)) {
			                    System.out.println("  equals_is matched **");
			                    if (currentWordIndex < currentWords.length) {
			                        searchColumnName2 = cname1;
			                        searchString2 = currentWords[currentWordIndex];
			                        conditionMode = OR;
			                        currentWordIndex+=1;
			                    }
			                }
			                System.out.println("searchColumnName2= " + cname1);
			                System.out.println("searchString2= " + searchString2);
			                System.out.println("\n CURRENT ACTION IS LIST AND CONDITION MODE IS OR\n");
			            }
			        }
			        if (currentWordIndex<currentWords.length) {
			        	
			        		
			        
			        	System.out.println("In NLP.parse in Mode 0 After removal of equals: \n");
			            System.out.println("CUUREN WOR INDX "+ currentWordIndex);
			        	
			         	searchString=currentWords[currentWordIndex];
			            System.out.println("searchString= " + searchString);
			       }
			        
			       }
			       else
			       {
			       
			        //System.out.println("\nBEFORE IS OR\n");
			        //System.out.println("\nWOPR INDEX DATA "+currentWordIndex);
			          return;
			       // System.out.println("\nWOPR DATA "+currentWords[currentWordIndex]);
			        
			       }
        }
        else
        {
        	   System.out.println("CAME IN ELSE WHERE and Currrent word is : "+currentWords[currentWordIndex]);
        	   if (eatWord(equals)==false) return;
			        if (currentWordIndex<currentWords.length) {
			        	searchColumnName=displayColumnName;
			        	System.out.println("In NLP.parse in Mode 0 After removal of equals: \n");
			            searchString=currentWords[currentWordIndex];
			            System.out.println("Serach Col name is "+searchColumnName+" searchString= " + searchString);
			            currentAction=LIST;
			        
			            String cols []=DBInterface.getColumnNames(tableName, slelected_db,"root", passwords[0]); 
			            	System.out.println("in coloumn data section "+cols[0]);
			            for(int m=0;m<cols.length;m++)
			            	{
			            		
                                
                                
					            try {
					            	DBInterface.doInit();
			            	        Connection con = DriverManager.getConnection(slelected_db,"root",passwords[0]);
                                    DBInterface.checkConnection(con.getWarnings()); // connection OK? 
					                Statement stmt = con.createStatement();
					                ResultSet rs = stmt.executeQuery("select * from "+tableName+" where "+cols[m]+" = '"+searchString+"'");
					                if(rs.next())
					                {
					                	searchColumnName=cols[m];
					                	break;
					                }
					                
							                 // Close the statement
							            stmt.close();
							
							            // Close the connection
							            con.close();
					                
					            } catch (SQLException se) {
					              System.out.println("inside check section"+se);
					            }
					
					           
 
			            	}	
			            		
			            	System.out.println("After For loop Serach Col name is "+searchColumnName+" searchString= " + searchString);	
			        }
        	
        }
        

    }


    public String getSQL() {
    
        if (currentAction==NO_OP) {
            System.out.println("getSQL(): currentAction is NO_OP!");
            return "";
        }
        if (currentAction==LIST) {
            // Start by making sure that the 'tableName' string does not
            // include any tables that are not referenced in column name
            // string:
            int index = displayColumnName.indexOf(".");
            System.out.println("index of ."+index);
            if (index>-1) {
                tableName = displayColumnName.substring(0,index);
                 System.out.println("tablename===>"+tableName);
            }
            if (searchColumnName!=null) {
                index = searchColumnName.indexOf(".");
                if (index>-1&&join_data==null) {
                    searchColumnName = tableName + searchColumnName.substring(index);
                    System.out.println("search colunm name 1===>"+searchColumnName);
                }
            }
            if (searchColumnName2!=null) {
                index = searchColumnName2.indexOf(".");
                if (index>-1) {
                    searchColumnName2 = tableName + searchColumnName2.substring(index);
                     System.out.println("search colunm name 2===>"+searchColumnName2);
                }
            }
            if (searchColumnName3!=null) {
                index = searchColumnName3.indexOf(".");
                if (index>-1) {
                    searchColumnName3 = tableName + searchColumnName3.substring(index);
                    System.out.println("search colunm name 3===>"+searchColumnName3);
                }
            }
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT ");
            if (displayColumnName!=null) sb.append(displayColumnName);
            else                         
            	sb.append("*");
            if (time_after!=null && time_after.valid()) {
                if (searchColumnName!=null) {
                    sb.append(" FROM " + tableName + " WHERE " + searchColumnName +
                              " > '" + time_after.toString() + "'");
                    // Note: for Microsoft Access: both ' should be # in last line
                } else {
                    sb.append(" FROM " + tableName);
                }
            } else if (time_before!=null && time_before.valid()) {
                if (searchColumnName!=null) {
                    sb.append(" FROM " + tableName + " WHERE " + searchColumnName +
                              " < #" + time_before.toString() + "#");
                } else {
                    sb.append(" FROM " + tableName);
                }
            } else if (compareMode!=NONE ) {
                if (compareMode==LESS) {
                    if (searchColumnName!=null) {
                        sb.append(" FROM " + tableName + " WHERE " + searchColumnName +
                                  " < " + quantity);
                    } else {
                        sb.append(" FROM " + tableName);
                    }
                } else { // MORE
                    if (searchColumnName!=null) {
                        sb.append(" FROM " + tableName + " WHERE " + searchColumnName +
                                  " > " + quantity);
                    } else {
                        sb.append(" FROM " + tableName);
                    }
                }
            } else {
                if (searchColumnName!=null)//actual 
                	{
                	if(join_data!=null)
                	{
                			sb.append(" FROM (" + first_table + " INNER JOIN "+second_table+" on "+first_table+"."+join_data+"="+second_table+"."+join_data+") WHERE " + searchColumnName +
                              " = " + quoteLiteral(searchString));
                	}
                	else
                	{
                		sb.append(" FROM " + tableName + " WHERE " + searchColumnName +
                              " = " + quoteLiteral(searchString));
                	}	
                    
                } else {
                    sb.append(" FROM " + tableName);
                }
            }
            if (searchString2!=null && conditionMode==AND) {
                sb.append(" AND " + searchColumnName2 + " = " +
                          quoteLiteral(searchString2));
            }
            if (searchString2!=null && conditionMode==OR) {
                sb.append(" OR " + searchColumnName2 + " = " +
                          quoteLiteral(searchString2));
            }
            sb.append(" ;");
            return new String(sb);
        }
        return "";
    }

    public String toEnglish(String [] r, String [] syns,
                            String [] origs, int num,
                            int num_rows_from_database) {
                            	
        int count = r.length / num_rows_from_database;
        System.out.println("The R Length  is ..........................................."+count);
		System.out.println("The Count is ..........................................."+count);
        System.out.println("The Num_of_rows from Database ..........................................."+num_rows_from_database);
        StringBuffer sb = new StringBuffer();
        for (int ii=0; ii<num_rows_from_database; ii++) {
            sb.append("The value of ");
            for (int i=0; i<count; i++) {
                String s = temp_col_names[i];
				System.out.println("the s data is "+s);
                // check for synonym substitution:
                for (int j=0; j<num; j++) {
                    if (s.equalsIgnoreCase(syns[j])) {
                        s = origs[j];
                    }
                }
                sb.append(s + " is " + r[i + (ii * count)]);
                if (i<count - 2) sb.append(", ");
                if (i==count- 2) sb.append(" and ");
            }
            sb.append(".\n");
        }
		String finaldata=new String(sb);
		System.out.println(finaldata);
        return finaldata;
    }

    public String toEnglishall(String [] r, String [] metadata,String [] syns,
                            String [] origs, int num,
                            int num_rows_from_database) {
        System.out.println("the No of Rows are : "+num_rows_from_database);
                            	
       for(int i=0;i<metadata.length;i++)
             System.out.println("metadata is"+metadata[i]);
        int count = (r.length / num_rows_from_database)-1 ;
        System.out.println("COUNT SECTION VALUE "+count);
        StringBuffer sb = new StringBuffer();
        int k=0;
        for (int i=0;i<num_rows_from_database;i++)
        {
        	for (int ii=0; ii<metadata.length; ii++)
            {
	            sb.append("The value of ");
	                         
	             String s=metadata[ii];
	                sb.append(s+ " is " + r[k++]);
	                System.out.println("IS : "+r[ii]);
	                if (ii<count  - 2) sb.append(", ");
	                if (ii==count - 2) sb.append(" and ");
	                sb.append("\n");
            }
            sb.append("\n");
        }
       
        
        return new String(sb);
    }

    // String constants need ' ' marks, while numbers can
    // not have surrounding ' ' marks:
    private String quoteLiteral(String s) {
    	
        if (s.startsWith("0")) return s;
        if (s.startsWith("1")) return s;
        if (s.startsWith("2")) return s;
        if (s.startsWith("3")) return s;
        if (s.startsWith("4")) return s;
        if (s.startsWith("5")) return s;
        if (s.startsWith("6")) return s;
        if (s.startsWith("7")) return s;
        if (s.startsWith("8")) return s;
        if (s.startsWith("9")) return s;
           return "'" + s + "'";
    }


 

    private boolean eatWord(String s[]) {
        if (currentWordIndex>=currentWords.length) {
            return false;
        }
        for (int i=0; i<s.length; i++) {
            if (currentWords[currentWordIndex].equalsIgnoreCase(s[i])) {
                currentWordIndex++;
                return true;
            }
        }
        return false;
    }

    private String eatColumnName(String current_column_name) {
        if (currentWordIndex>=currentWords.length) {
            return null;
        }
        // Check for a column name of the form <Table name>.<column name>:
        if (current_column_name!=null) {
            int index = current_column_name.indexOf(".");
            if (index>-1) {
                current_column_name = current_column_name.substring(index+1);
            }
        }
        String ret_col_name=null;
        for (int i=0; i<dbinfo.numTables; i++) {
            for (int j=0; j<dbinfo.columnNames[i].length; j++) {
                if (currentWordIndex>=currentWords.length) break;
                if (currentWords[currentWordIndex].equalsIgnoreCase(dbinfo.columnNames[i][j])) {
                    if (current_column_name!=null &&
                        current_column_name.indexOf(dbinfo.columnNames[i][j]) >-1)
                        continue;
                    currentWordIndex++;

                    temp_col_names[num_temp_col_names++] = dbinfo.columnNames[i][j];

                    if (tableName==null) {
                        tableName=dbinfo.tableNames[i];
                    } else {
                        if (tableName.equalsIgnoreCase(dbinfo.tableNames[i])==false) {
                            tableName=tableName + ", " + dbinfo.tableNames[i];
                        }
                    }
                    if (ret_col_name==null) {
                        ret_col_name=dbinfo.tableNames[i] + "." + dbinfo.columnNames[i][j];
                    } else {
                        if (ret_col_name.equalsIgnoreCase(dbinfo.columnNames[i][j])==false) {
                            ret_col_name=ret_col_name + ", " +
                                dbinfo.tableNames[i] + "." + dbinfo.columnNames[i][j];
                        }
                    }
                }
            }
        }
        return ret_col_name;
    }

    

}
