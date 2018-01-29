package com.company.reserve.demo;

import java.io.Serializable;
import java.util.Date;

public class Seat implements Serializable{
		public String seatID;//row+place
		public String reservedByClient;
		public Date reservedOn;
		
}
