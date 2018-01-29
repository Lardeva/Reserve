package com.company.reserve.demo;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Request implements Serializable {
	
	public String[] seatsToBeReserved;
	public String clientId;
	public String filmName;
	public Date filmTime;
	
	
	}

