package org.kenn.intercom;

import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class AudioSender implements Runnable {

	private AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, false);
	private DatagramPacket dgp;
	private TargetDataLine microphone;
	private int numBytesRead;
	private final int CHUNK_SIZE = 4;
	private byte[] data = new byte[4];
	private boolean speak = false;

	private InetAddress addr = null;
	private String addrStr = "";
	private int port = 50005;
	private boolean disconnect;

	AudioSender(String addrStr) {
		disconnect = false;
		this.addrStr = addrStr;
		System.out.println("Set address to "+addrStr+":"+port);
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
			addr = InetAddress.getByName(this.addrStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setEnable(boolean b) {
		if (speak != b)
			speak = b;
	}

	public boolean isEnable() {
		return speak;
	}
	
	public void disconnect() {
		disconnect = true;
		speak = false;
	}
	
	public boolean isDisconnected() {
		return disconnect;
	}

	@Override
	public void run() {
		if(disconnect) return;
		try (DatagramSocket socket = new DatagramSocket()) {
			System.out.println("Start audio sender");
			while (true) {
				numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
				dgp = new DatagramPacket(data, numBytesRead, addr, port);
				if (speak) {
					socket.send(dgp);
				}
				
				if(disconnect) {
					socket.close();
					break;
				}
			}
			microphone.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
