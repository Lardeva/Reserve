package com.company.reserve.demo.projection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.company.reserve.demo.IResponse;

public class ResponseProjection implements Serializable, IResponse{
	public ArrayList<Seat> seats = new ArrayList<Seat>();
	public String filmName;
	public Date filmTime;
	public String responseStatus = "accepted";
	public String filmHall;
	
	public static ResponseProjection createEmpty (){
		ResponseProjection resp = new ResponseProjection();
		for (int i = 0; i < 10; i++) {
			
			Seat seat = new Seat();
			seat.seatID = "" +(i+1);
		    resp.seats.add(seat);
		}
		
		return resp;
	}
}
