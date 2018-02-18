package com.company.reserve.demo.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.company.reserve.demo.film.ResponseFilm;

public class FilmStorage {
	private static final String FILM_STORIGE_NAME ="FilmRegister";
	
	public static ResponseFilm readFilm() {
		/* Read File record*/
		try {
			ResponseFilm respFilm;
			if (new File(FILM_STORIGE_NAME).exists()) {
				FileInputStream fi = new FileInputStream(FILM_STORIGE_NAME);
				ObjectInputStream dbios = new ObjectInputStream(fi);
				respFilm = (ResponseFilm) dbios.readObject();
				fi.close();							
			}else {
				respFilm = ResponseFilm.createEmpty();
			}
			return respFilm;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


public static void writeFilm (ResponseFilm respFilm) {
	try {
		
			FileOutputStream fo = new FileOutputStream(FILM_STORIGE_NAME);
			ObjectOutputStream oosFile = new ObjectOutputStream(fo);
			
			oosFile.writeObject(respFilm);
			fo.close();	
			
	}catch (Exception e) {
		throw new RuntimeException(e);
	}
	
}
}