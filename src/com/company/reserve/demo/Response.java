package com.company.reserve.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Response implements Serializable{
	public ArrayList<Seat> seats = new ArrayList<Seat>();
	public String filmName;
	public Date filmTime;
	public String responsStatus;
		
	public static Response createEmpty (){
		Response resp = new Response();
		for (int i = 0; i < 10; i++) {
			
			Seat seat = new Seat();
			seat.seatID = "" +(i+1);
		    resp.seats.add(seat);
		}
		
		return resp;
	}
}
