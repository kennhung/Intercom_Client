package org.kenn.intercom;

import java.awt.Toolkit;
import java.io.*;
import java.net.*;
import java.util.*;

import org.json.JSONObject;

public class InformationSocket implements Runnable {
	
	private String addr;
	private int port = 50001;
	private BufferedReader reader;
	private PrintStream writer;
	private Socket socket;
	
	private JSONObject incomming;
	private Thread readerThread;
	
	InformationSocket(String addr) {
		this.addr = addr;
		try {
			socket = new Socket(this.addr,port);
			//connect to server
			
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());  
			reader = new BufferedReader(streamReader);
			//building reader
			
			writer = new PrintStream(socket.getOutputStream());
			//building writer
			
			System.out.println("Connection build successfully");
		} catch (UnknownHostException e) {
			System.out.println("UnknownHost");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed to establish connection");
			e.printStackTrace();
		}
		
		if(socket!=null && socket.isConnected()) {
			readerThread = new Thread(new IncomingReader());  
			readerThread.start();
		}
		
	}
	
	@Override
	public void run() {
		
	}
	
	public int sendCommand(String outgoing) {
		if(socket!=null && socket.isConnected() && (outgoing!="")) {
			try{//send data
		        writer.println(outgoing); 
		        System.out.println(outgoing+" > "+ socket.getRemoteSocketAddress());
		        //flush buffer
		        writer.flush();         
		    }catch(Exception ex ){
		    	System.out.println("Failed to send data");
		    	ex.printStackTrace();
		    	return 2;
		    }  
		}
		else {
			System.out.println("Connection does not exist");
			return 1;
		}
		return 0;
	}
	
	public class IncomingReader implements Runnable{
		public void run(){
			String message;
			try{
				while ((message = reader.readLine()) != null){
					System.out.println("Incoming message from "+socket.getRemoteSocketAddress()+" > "+message);
					Calendar currentT = Calendar.getInstance();
					currentT.setTimeInMillis(System.currentTimeMillis());
					try {
						incomming = new JSONObject(message);
					}
					catch(Exception e) {
						System.err.println("Json Error: " + e.getMessage());
					}
		    	}
			}catch(Exception ex ){System.out.println(ex.getLocalizedMessage());}
		}
	} 

	
}
