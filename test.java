/**
 * @(#)test.java
 *
 *
 * @author 
 * @version 1.00 2012/5/28
 */


public class test {

   if(eatWord(equals)==true)
   	{       
					System.out.println("CUUREN WOR INDX "+ currentWordIndex);
			       	quantity=-999.0f;
			        compareMode=NONE;
			       
			        
			        if (eatWord(less)) 
			        {
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
			
			        if (eatWord(more)) 
			        {
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
			
			        if (eatWord(after)) 
			        {
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
			        if (time_after!=null) 
			        {
			            System.out.println("\n parsed data 'after' time OK:");
			            System.out.println("  year:  " + time_after.getYear());
			            System.out.println("  month: " + time_after.getMonth());
			            System.out.println("  day:   " + time_after.getDayOfMonth()+"\n\n");
			            
			        }
			        
			
			
			        if (eatWord(before)) 
			        {
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
			        if (time_before!=null) 
			        {
			            System.out.println("parsed 'before' time OK:");
			            System.out.println("  year:  " + time_before.getYear());
			            System.out.println("  month: " + time_before.getMonth());
			            System.out.println("  day:   " + time_before.getDayOfMonth()+"\n\n");
			        }
			
			
			        System.out.println("\n CURRENT ACTION IS LIST AND CONDITION MODE IS 0\n");
			        System.out.println("\nWOPR INDEX DATA "+currentWordIndex);
			        	
			        conditionMode = 0;
			
			        if (searchColumnName==null&&currentWords.length>1) return;
			        
			        
			        if (eatWord(and)) 
			        {   // check for AND condition
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
			        if (eatWord(or)) 
			        {   // check for OR condition
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
			        if (currentWordIndex<currentWords.length) 
			        {
			        	
			        		
			        
			        	System.out.println("In NLP.parse in Mode 0 After removal of equals: \n");
			            System.out.println("CUUREN WOR INDX "+ currentWordIndex);
			        	
			         	searchString=currentWords[currentWordIndex];
			            System.out.println("searchString= " + searchString);
			        }
			        
   	}		        

    
}