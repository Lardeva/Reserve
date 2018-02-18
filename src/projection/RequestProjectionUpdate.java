package projection;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.company.reserve.demo.IRequest;

public class RequestProjectionUpdate implements Serializable, IRequest {
	
	public String[] seatsToBeReserved;
	public String clientId;
	public Date filmTime;
	public String projectionId;
	public String filmHall;
	
	}

