package com.company.reserve.demo;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerDemo {

	 public static void main(String args[]) throws Exception { 
	      ServerSocket ssock = new ServerSocket(1234);
	      Object lock = new Object(); 
	      System.out.println("Listening");
	      
	      while (true) {
	         Socket sock = ssock.accept();
	         System.out.println("Connected");
	         new Thread(new ServerThread(sock,lock)).start();
	      }
	   }
	 

}
