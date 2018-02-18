package com.company.reserve.demo;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import file.Seat;
import film.Film;
import film.RequestFilmView;
import film.ResponseFilm;
import projection.RequestProjectionUpdate;
import projection.RequestProjectionView;
import projection.ResponseProjection;


public class ClientDemo {
	
	
	public static void main(String[] args) {
		
		for(int i=0;i<args.length;i++) {
			System.out.println(args[i]);
		}
		try {
			/*1. Send Request View Film*/	
			RequestFilmView reqFilm = new RequestFilmView();
			ResponseFilm responseFilm = (ResponseFilm) sendRequest(reqFilm);
			
			 /*2. Visualize Film View */		    
			printFilm(responseFilm);
			
			/*3. Invoke Data entry*/			
			int numberFilm = dataEntry();
			//scanner.close();
			Film fl = responseFilm.films.get(numberFilm-1);
			
			
		   /* 4. Visualize Hall View */
					
			RequestProjectionView req = initializeViewRequest(fl);
			ResponseProjection resp = (ResponseProjection) sendRequest(req);
			
			String [] seatsToBeReserved;
			do {
				printHall(resp);

				/*4.1 Invoke Scanner for Operator choice */
				seatsToBeReserved = getOperatorChoice();
				
				/*4.2 Validate*/	
			} while (!validateInput(seatsToBeReserved,resp));
				
			   
			/* 5.1 Send reserve(update) command - initialize*/
			RequestProjectionUpdate reqUpd = initializeUpdateReserve(seatsToBeReserved, fl,args[0]);
						
			//* 5.2 Send reserve(update) command -send*/
			ResponseProjection responseProjection = (ResponseProjection) sendRequest(reqUpd);

			/* 6. Visualize Hall View after update */
			printHall(responseProjection);
			if(responseProjection.responsStatus== "rejected"){
				System.out.println("Your order has been declined. The places you are trying to book are already booked. Please reserve other places! ");
			}else {
				System.out.println("Your order is approved. Your booking is successful.");
			}
			
	    	    
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}
	/**/
	public static RequestProjectionView initializeViewRequest(Film fl) {
		
		RequestProjectionView req = new RequestProjectionView();
		req.projectionID = fl.projectionId;
		return req;
	}
	/*  */
	public static RequestProjectionUpdate initializeUpdateReserve(String [] seatsToBeReserved,Film fl,String cashdeskName){
		RequestProjectionUpdate reqUpd = new RequestProjectionUpdate();
		reqUpd.clientId = cashdeskName;
		reqUpd.seatsToBeReserved =  seatsToBeReserved;
		reqUpd.projectionId = fl.projectionId;
		reqUpd.filmTime=fl.filmTime;
		reqUpd.filmHall=fl.filmHall;
		return reqUpd;
	}
	 /*  Send*/
	public static IResponse sendRequest(IRequest req){

		Socket socket ;
		OutputStream os ;
		ObjectOutputStream oos ;
		InputStream is ;
		ObjectInputStream ois ;
		
		try {
		socket = new Socket("127.0.0.1", 1234);		
		os = socket.getOutputStream();
		oos = new ObjectOutputStream(os);  
		
		oos.writeObject(req);	
		
		is = socket.getInputStream();  
	    ois = new ObjectInputStream(is);  
	    IResponse resp = (IResponse)ois.readObject();  
     
	    socket.close();
	    return resp;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
	}
	/*3. Data entry*/
	public static int dataEntry(){	
		Scanner scanner = new Scanner(System.in);
		System.out.println("Input projection number: ");
		String number = scanner.nextLine();
		int  numberFilm = Integer.parseInt(number);
		//scanner.close();
		return numberFilm;
	}
	/* 4.1. Get Operator choice */
	public static String [] getOperatorChoice(){	
		Scanner sc = new Scanner(System.in);
		String inp = sc.nextLine();
		String[] result = inp.split(",");
		//sc.close();
		return result;
	}
	/*4.2 Validate*/
	public static boolean validateInput(String [] choice,ResponseProjection resp ) {

			for (int j = 0; j < choice.length; j++) {
			
				for (int i = 0; i < resp.seats.size(); i++) {
					Seat st = resp.seats.get(i);
					
					if (choice[j].equals(st.seatID)) {
						if (!st.reservedByClient.equals("")) {
							System.out.println("Your order has been declined. The places you are trying to book are already booked. Please reserve other places! ");
							return false;
	
						}
					}
				}
			}
	
			return true;
		
	}
	/*  Print Hall*/
	public static void printHall(ResponseProjection resp) {

		for (int i = 0; i < resp.seats.size(); i++) {
			Seat st = resp.seats.get(i);
			String text;
			if (st.reservedByClient.equals("")) {
				text = st.seatID + "-[" + " " + "] ";
			} else {
				text = st.seatID + "-[" + "*" + "] ";
			}

			System.out.print(text);
		}
		System.out.println();

	}
	/*  Print Film*/
	public static void printFilm(ResponseFilm respFilm) {
		
		 for(int i=0; i<respFilm.films.size(); i++){
			 Film fl = respFilm.films.get(i);
			 System.out.println((i+1)+". " +fl.projectionId + "-" +fl.filmName + "-"+new SimpleDateFormat("dd.MM.yyyy HH:mm").format(fl.filmTime)+"/ hall "+ fl.filmHall);
			 
		 }
}
}
