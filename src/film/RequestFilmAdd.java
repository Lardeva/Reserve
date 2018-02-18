package film;

import java.io.Serializable;
import java.util.Date;

import com.company.reserve.demo.IRequest;

public class RequestFilmAdd implements Serializable, IRequest{
	public String projectionID;
	public Date filmTime;
	public String filmName;
	public String filmHall;
	public String fileName;
}
