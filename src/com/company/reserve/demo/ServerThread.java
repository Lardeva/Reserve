package com.company.reserve.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.DatabaseMetaData;
import java.util.Date;

import javax.activation.UnsupportedDataTypeException;



public class ServerThread implements Runnable{
	private Socket socket;
	
	public ServerThread(Socket sock) {
		socket = sock;
	}

	@Override
	 public void run() {
	      
		
		try {
			
			Request req = receive();
		
			
			Response response = update(req);
			
			send(response);

    	    socket.close();
    	    
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
   }

	public Request receive(){
		
		try{
			
			/*Receive request*/
			
			// prepare streams
			InputStream is = socket.getInputStream();  
			ObjectInputStream ois = new ObjectInputStream(is);
			
			//read
			Request req = (Request)ois.readObject();
			return req;
			
			
		}catch(Exception e){
			throw new RuntimeException(e);
			
		}
		
		
	}
	public static Response read() {
		/* Read database record*/
		try{
			Response resp;
			if(new File("database").exists()) {
			 	FileInputStream fi = new FileInputStream("database");
			    ObjectInputStream dbois = new ObjectInputStream(fi); 
			    resp = (Response) dbois.readObject();
			    fi.close();
			} else {
				 resp = Response.createEmpty();				 
			}
			
			return resp;
		} catch (Exception e) {
			throw new RuntimeException(e);
	         
		}
		
		
	}
	public static void write(Response resp) {
		try{
			FileOutputStream fo = new FileOutputStream("database");
			ObjectOutputStream oosFile = new ObjectOutputStream(fo);
	
			oosFile.writeObject(resp);
			fo.close();
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}	
	}
	public Response update(Request req){

		  /* Update database record, if applicable*/
				try{ 
					Response resp = read();
				
								if (req.seatsToBeReserved!=null){				
							    	for(int i = 0; i <req.seatsToBeReserved.length; i++) {
							    		for(int j = 0; j <resp.seats.size(); j++) {
						    	    		Seat resSeat = resp.seats.get(j);
						    	    		
						    	    		if (req.seatsToBeReserved[i].equals(resSeat.seatID)) {
						        	    		if(resSeat.reservedByClient==null){
						        	    			resSeat.reservedByClient =req.clientId;
						        	    			resSeat.reservedOn = new Date();
						        	    		} else {
						        	    			//todo: report error
						        	    		}
						    	    			
						    	    		}
						    	    	}
							    	}
							
							    write(resp);
								return resp;
							}else{
								return resp;
							}
				}catch(Exception e){
					throw new RuntimeException();
				}
		 	
	}

	public void send(Response resp){
		try{
			/*SEND RESPONSE*/
    	    
			// prepare streams    	    
    	    OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			
			//write
			oos.writeObject(resp);
					
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
//	public Request cancel(){
//		try{
//			
//			// prepare streams
//			InputStream is = socket.getInputStream();  
//			ObjectInputStream ois = new ObjectInputStream(is);
//			
//			//read
//			Request req = (Request)ois.readObject();
//			return req;
//			
//			
//		}catch(Exception e){
//			throw new RuntimeException(e);
//			
//		}
//	}
	
}
