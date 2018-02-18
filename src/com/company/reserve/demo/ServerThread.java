package com.company.reserve.demo;


import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import com.company.reserve.demo.file.FilmStorage;
import com.company.reserve.demo.file.ProjectionStorage;
import com.company.reserve.demo.film.Film;
import com.company.reserve.demo.film.RequestFilmAdd;
import com.company.reserve.demo.film.RequestFilmView;
import com.company.reserve.demo.film.ResponseFilm;
import com.company.reserve.demo.projection.RequestProjectionUpdate;
import com.company.reserve.demo.projection.RequestProjectionView;
import com.company.reserve.demo.projection.ResponseProjection;
import com.company.reserve.demo.projection.Seat;




public class ServerThread implements Runnable{
	private Socket socket;
	private Object lockObj;
	
	public ServerThread(Socket sock, Object lock) {
		socket = sock;
		lockObj = lock;
	}

	@Override
	 public void run() {
	      
		
		try {
			
			IRequest req = receive();
			IResponse response = null;
					
			if(req instanceof RequestProjectionUpdate){
				RequestProjectionUpdate requestProjectionUpdate = (RequestProjectionUpdate)req;
				response= processRequestProjectionUpdate(requestProjectionUpdate);
			}
			
			if(req instanceof RequestProjectionView){
				RequestProjectionView requestProjectionView =(RequestProjectionView)req;
				response=processRequestProjectionView(requestProjectionView);
			}
				
			if (req instanceof RequestFilmView) {
				RequestFilmView requestFilmView = (RequestFilmView)req;
				response=processRequestFilmView(requestFilmView);
			}	
			if (req instanceof RequestFilmAdd) {
				RequestFilmAdd requestFilmAdd =(RequestFilmAdd)req;
				response = processRequestFilmAdd(requestFilmAdd);
				
			}
			send(response);

    	    socket.close();
    	    
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
   }
	
	public IRequest receive(){
		
		try{
			
			/*Receive request*/
			
			// prepare streams
			InputStream is = socket.getInputStream();  
			ObjectInputStream ois = new ObjectInputStream(is);
			
			//read
			IRequest req = (IRequest)ois.readObject();
			return req;
			
			
		}catch(Exception e){
			throw new RuntimeException(e);
			
		}
		
		
	}
	public void send(IResponse resp){
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

	public ResponseProjection processRequestProjectionView(RequestProjectionView reqView) {
		synchronized (lockObj){
			ResponseProjection responseProjection = ProjectionStorage.readProjection(reqView.projectionID);
			return responseProjection;
		}
	}
	
	public ResponseProjection processRequestProjectionUpdate(RequestProjectionUpdate req){

		  /* Update database record, if applicable*/
		synchronized (lockObj){
			try{ 
				ResponseProjection resp = ProjectionStorage.readProjection(req.projectionId);
			
							if (req.seatsToBeReserved!=null){				
						    	for(int i = 0; i <req.seatsToBeReserved.length; i++) {
						    		for(int j = 0; j <resp.seats.size(); j++) {
					    	    		Seat resSeat = resp.seats.get(j);
					    	    		
					    	    		if (req.seatsToBeReserved[i].equals(resSeat.seatID)) {
					        	    		if(resSeat.reservedByClient.equals("")){
					        	    			resSeat.reservedByClient =req.clientId;
					        	    			resSeat.reservedOn = new Date();
					        	    		} else {
					        	    			//collision: report error
					        	    			
					        	    			resp = ProjectionStorage.readProjection(req.projectionId);
					        	    			resp.responseStatus = "rejected";
					        	    			return resp;
					        	    		}
					    	    			
					    	    		}
					    	    	}
						    	}
						
						    ProjectionStorage.writeProjection(resp,req.projectionId);
							return resp;
						}else{
							return resp;
						}
			}catch(Exception e){
				throw new RuntimeException();
			}
		}
				
		 	
	}


	
	public static ResponseFilm processRequestFilmView(RequestFilmView requestFilmView) {
		ResponseFilm responseFilm = FilmStorage.readFilm();
		return responseFilm;
	}
	
	public static ResponseFilm processRequestFilmAdd (RequestFilmAdd reqFilm) {
		  /* Update database record, if applicable*/
				try{ 
					ResponseFilm resp = FilmStorage.readFilm();
					
					Film newFilm =new Film();
					newFilm.fileName =reqFilm.fileName;
					newFilm.filmName = reqFilm.filmName;
					newFilm.projectionId=reqFilm.projectionID;
					newFilm.filmTime=reqFilm.filmTime;
					newFilm.filmHall=reqFilm.filmHall;
					
					resp.films.add(newFilm);
							
			    	FilmStorage.writeFilm(resp);
			    	
					
					return resp;							
				}catch(Exception e){
					throw new RuntimeException(e);
				}
		 	}
	
}
