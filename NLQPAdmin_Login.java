/**
 * @(#)NLQPAdmin_Login.java
 *
 *
 * @author 
 * @version 1.00 2012/4/26
 */

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.image.*;
//import java.text.*;
//import java.util.*;
import javax.swing.*;
import java.sql.*;

public class NLQPAdmin_Login extends JFrame implements ActionListener
{
    Container cp;
    JLabel l1,l2,ladmin,luname,lpass;
    JTextField txt1,txt2;
    JButton q_button;
    JLabel lbg;
    ImageIcon ic;
    public NLQPAdmin_Login() 
    {
    	cp=getContentPane();
    	cp.setLayout(null);
    	
    	setSize(600, 340);
    	setTitle("Natural Language Query Processing");
    	setLocation(290,180);
    	
    	
    	
    	l1 = new JLabel("User Natural Langauge To SQL");
    	l1.setFont(new Font("Algerian", Font.BOLD, 25));
    	cp.add(l1);
        l1.setBounds(80, 20, 700, 34);
        
        l2 = new JLabel("Generating Trasitional Logical Query");
    	l2.setFont(new Font("Algerian", Font.BOLD, 25));
    	cp.add(l2);
        l2.setBounds(20, 56, 700, 34);
        
        ladmin=new JLabel("NLQP ADMINISTRATOR SECTION");
        ladmin.setFont(new Font("Algerian", Font.BOLD, 20));
    	cp.add(ladmin);
        ladmin.setBounds(130, 110, 700, 34);
        
        luname=new JLabel("ENTER THE USERNAME :  ");
        cp.add(luname);
        luname.setFont(new Font("Times New Roman", Font.BOLD, 14));
        luname.setBounds(130,160,190,30);
        
        txt1=new JTextField(10);
        cp.add(txt1);
        txt1.setBounds(315,160,130,27);
        
        lpass=new JLabel("ENTER THE PASSWORD :  ");
        cp.add(lpass);
        lpass.setFont(new Font("Times New Roman", Font.BOLD, 14));
        lpass.setBounds(130,210,190,30);
        
        txt2=new JPasswordField(10);
        cp.add(txt2);
        txt2.setBounds(315,210,130,27);
        
        q_button=new JButton("SUBMIT");
        cp.add(q_button);
        q_button.setBounds(365,260,80,25);
        q_button.addActionListener(this);
        this.setVisible(true);
        
        lbg=new JLabel("");
	    ic=new ImageIcon("img.jpg");
	    lbg.setBounds(0,0,600,340);
	    lbg.setIcon(ic);
	    add(lbg);
    }
    
    public static  void main(String args[]) 
    {
       
        
             UIManager.LookAndFeelInfo info[];
             info=UIManager.getInstalledLookAndFeels();
             String themename=info[3].getClassName();
             try
             {
             	UIManager.setLookAndFeel(themename);
             }
             catch(Exception e)
             {
             	System.out.println(e);
             }

        NLQPAdmin_Login sc = new NLQPAdmin_Login();
        
        sc.show();
        
    }
    
    public void actionPerformed(ActionEvent ae)
    {
    	if(txt1.getText().equals("admin") && txt2.getText().equals("qwerty"))
		   	  {
		   	  	this.setVisible(false);
		   	  	try
					{
					   NLQPMain n=new NLQPMain("admin");
					}
					catch(Exception e) 
					{
					System.out.println("Error creating the FileInfo panel: " +e);
					e.printStackTrace();
					}
		   	  	
		   	  }
		   	  else
		   	  {
		   	  	JOptionPane.showMessageDialog(null,"INVALID USERNAME OR PASSWORD.."); 
		   	  }

    }
    
    
}