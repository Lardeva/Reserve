package film;

import java.io.Serializable;
import java.util.ArrayList;

import com.company.reserve.demo.IResponse;

public class ResponseFilm implements Serializable, IResponse {
	public ArrayList<Film> films =new ArrayList<Film>();
	
	public static ResponseFilm createEmpty() {
		ResponseFilm respFilm = new ResponseFilm();
		return respFilm;
	}
}
