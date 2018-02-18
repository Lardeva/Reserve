package com.company.reserve.demo;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.company.reserve.demo.film.Film;
import com.company.reserve.demo.film.RequestFilmAdd;
import com.company.reserve.demo.film.RequestFilmView;
import com.company.reserve.demo.film.ResponseFilm;



public class ClientFilm {

	public static void main(String[] args) {
		try {		
			/* Send Request View */	
			RequestFilmView reqFilm = new RequestFilmView();
			
			ResponseFilm responseFilm = (ResponseFilm) sendRequest(reqFilm);
			
			 /* Visualize Film View */
		    
			printFilm(responseFilm);
			
		
			/*Data entry*/
			Scanner scanner = new Scanner(System.in);
			System.out.println("Input name for Film: ");
			String filmName = scanner.nextLine();
			System.out.println("Input date-time (dd.MM.yyyy HH:mm): ");
			String dateStr = scanner.nextLine();
			Date filmDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateStr);
			System.out.println("Input Hall: ");
			String filmHall = scanner.nextLine();
			System.out.println("Input projection id: ");
			String filmId = scanner.nextLine();
			scanner.close();
			/* Send add command */
			RequestFilmAdd reqFilmAdd = new RequestFilmAdd();
	
			reqFilmAdd.projectionID = filmId;		
			reqFilmAdd.filmName = filmName;
			reqFilmAdd.filmTime = filmDate;
			reqFilmAdd.filmHall = filmHall;
			
			responseFilm = (ResponseFilm) sendRequest(reqFilmAdd);
			
			   /* Visualize Film View after add*/
		    printFilm(responseFilm);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	/*   */
	public static IResponse sendRequest(IRequest reqUpd) {
		Socket socket;
		OutputStream os;
		ObjectOutputStream oos;
		InputStream is;
		ObjectInputStream ois;
		try {
			socket = new Socket("127.0.0.1", 1234);
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);

			oos.writeObject(reqUpd);

			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			IResponse resp = (IResponse) ois.readObject();

			socket.close();
			return resp;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
		
	/*  Print Film*/
		
	public static void printFilm(ResponseFilm respFilm) {
		
		 for(int i=0; i<respFilm.films.size(); i++){
			 Film fl = respFilm.films.get(i);
			 System.out.println((i+1)+". " +fl.projectionId + "-" +fl.filmName + "-"+new SimpleDateFormat("dd.MM.yyyy HH:mm").format(fl.filmTime)+ "/ hall "+ fl.filmHall);
			
		 }
	}
}

