package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import projection.RequestProjectionView;
import projection.ResponseProjection;



public class ProjectionStorage {
	
	
	public static ResponseProjection readProjection(String  projectionId) {
		String fileName = "g:\\db"+projectionId;
		/* Read database record*/
		try{
			ResponseProjection resp;
			if(new File(fileName).exists()) {
			 	FileInputStream fi = new FileInputStream(fileName);
			    ObjectInputStream dbois = new ObjectInputStream(fi); 
			    resp = (ResponseProjection) dbois.readObject();
			    fi.close();
			} else {
				 resp = ResponseProjection.createEmpty();				 
			}
			
			return resp;
		} catch (Exception e) {
			throw new RuntimeException(e);
	         
		}
	}	
	public static void writeProjection(ResponseProjection resp,String  projectionId) {
		String fileName = "g:\\db"+projectionId;
		try{
			FileOutputStream fo = new FileOutputStream(fileName);
			ObjectOutputStream oosFile = new ObjectOutputStream(fo);
	
			oosFile.writeObject(resp);
			fo.close();
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}	
	}
	

}