package org.jitsi.hammer;

import java.io.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CreateStatFile {

	public static void main(String[] args) {
	
		String path="/Users/Ritika/Desktop/JITSI Test Harness Log";   //Modify this according to desired download location
		File file=new File(path);
	    if (!file.exists())
		file.mkdir();
	    System.out.println(file.mkdir());
	    
	    int number = NetworkMonitor.numberOfFakeUsers;
	    System.out.println(number);
	
        //Creates a new file in the folder specified with log details
		Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss");
	    String str = formatter.format(date);
	    file=new File(path + "/"+ str);
	
		try{
		  if(file.createNewFile())
		   {
		        FileWriter fw = new FileWriter(file.getAbsoluteFile());
		       	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		        String logintime = sdf.format(Calendar.getInstance().getTime());
				fw.write("Log-in time:");
				fw.write(logintime);
				fw.write("\nNumber of fake users initialized: "+number);
				fw.close();

		      }} catch (IOException ex) {
		   
		  ex.printStackTrace();
		}
	}
}
