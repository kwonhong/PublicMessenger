package com.example.messengerproject.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class ConnectingSocketCallable implements Callable<Socket>{

	private static final int SERVERPORT = 50001;
	private static final String SERVER_IP = "172.30.56.27";
	private Socket socket;

	@Override
	public Socket call() throws Exception {
		try {

			InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
			socket = new Socket(serverAddr, SERVERPORT);
			
			return socket;

		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
		
	}

}
