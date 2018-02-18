package film;

import java.io.Serializable;
import java.util.Date;

public class Film implements Serializable{
	public String projectionId;
	public Date filmTime;
	public String filmName;
	public String filmHall;
	public String fileName = "g:\\db"+projectionId;
}
