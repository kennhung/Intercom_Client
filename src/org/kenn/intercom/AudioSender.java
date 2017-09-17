package org.kenn.intercom;
import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class AudioSender implements Runnable {

	AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, false);
	DatagramPacket dgp;
	TargetDataLine microphone;
	int numBytesRead;
	final int CHUNK_SIZE = 4;
	byte[] data = new byte[4];
	boolean speak = false;

	InetAddress addr = null;
	String addrStr = "";
	int port = 50005;

	AudioSender(String addrStr) {
		this.addrStr = addrStr;
		try {
			microphone = AudioSystem.getTargetDataLine(format);

			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			microphone = (TargetDataLine) AudioSystem.getLine(info);
			microphone.open(format);
			microphone.start();

		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		try {
			addr = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		public void setEnable(boolean b) {
			if(speak&&!b) speak = b;
		}
		
		public boolean isEnable() {
			return speak;
		}

	@Override
	public void run() {
		try (DatagramSocket socket = new DatagramSocket()) {
			System.out.println("Start audio sender");
			while (true) {
				numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
				dgp = new DatagramPacket(data, numBytesRead, addr, port);
				if(speak) {
					socket.send(dgp);
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
