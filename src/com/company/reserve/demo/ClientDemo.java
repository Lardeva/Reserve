package com.company.reserve.demo;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class ClientDemo {
	
	
	public static void main(String[] args) {
		try {
		
			Socket socket ;
			OutputStream os ;
			ObjectOutputStream oos ;
			InputStream is ;
			ObjectInputStream ois ;
			
			
		    /* 3. Send Request View */
			socket = new Socket("127.0.0.1", 1234);
			
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);  
			Request req = new Request();
			oos.writeObject(req);
			
			is = socket.getInputStream();  
    	    ois = new ObjectInputStream(is);  
    	    Response resp = (Response)ois.readObject();  
         
		    socket.close();
		
		   /* 3.1 Visualize Hall View */
			printHall(resp);
			
		   /* 4. Get Operator choice */
			Scanner sc = new Scanner(System.in);
			String inp=sc.nextLine();
			String[] result = inp.split(",");
  	        sc.close();
		     
		   /* 5. Send reserve(update) command */
			Request reqUpd = new Request();

			reqUpd.clientId = "CASHDESK1";		
		    reqUpd.seatsToBeReserved = result;
		     
		     socket = new Socket("127.0.0.1", 1234);
				
			 os = socket.getOutputStream();
			 oos = new ObjectOutputStream(os);  
				
		     oos.writeObject(reqUpd);
		      
 			 is = socket.getInputStream();  
	    	 ois = new ObjectInputStream(is);  
	    	 resp = (Response)ois.readObject();  
	    	 
    	    socket.close();
    	    
   		     /* 6. Visualize Hall View after update*/
    	     printHall(resp);
	    	    
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}
	

	public static void printHall(Response resp ) {
		
		 for(int i=0; i<resp.seats.size(); i++){
			 Seat st = resp.seats.get(i);
			 String text ;
			 if(st.reservedByClient==null){
				 text = st.seatID + "-[" +" " + "] ";
			 }else {
				 text = st.seatID + "-[" + "*" + "] ";
			}
			 					   
			 System.out.print(text);
		 }
		 System.out.println();
		
	}
}
