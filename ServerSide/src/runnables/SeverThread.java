package runnables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import classes.Client;
import classes.MessageExtractor;
import classes.MessengerInfo;

class ServerThread implements Runnable {

	//only one server socket is requied!!
	private ServerSocket serverSocket;
	public static final int SERVERPORT = 50001;
	
	//needs ArrayList to keep track of all the ChatRoomThread
	private MessengerInfo messengerInfo;
	private ArrayList<Client> clients;

	private PrintWriter out;
	private BufferedReader input;
	private boolean isUserFound;
	private Client client;
	
	private MessageExtractor messageExtractor;

	public ServerThread() {
		messengerInfo = new MessengerInfo();
		clients = messengerInfo.getClients();
	}

	public void run() {

		// creating the server socket
		Socket socket = null;
		Client client = null;
		messageExtractor = new MessageExtractor();
		
		try {
			System.out.println("server socket has been created");
			serverSocket = new ServerSocket(SERVERPORT);
			
			//String send = "ACCOUNT#" + username + "#" + password +"#";
			while (true) {
				System.out.println("waiting for the socket");
				socket = serverSocket.accept();
				System.out.println("one socket has been actually connected");
				
				client = new Client(socket);
				client.resetTheThreads(client, clients, messengerInfo);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private int isUserNameExist(String name) {

		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getUsername().equals(name)) {
				return i;
			}
		}
		return -1;
	}

}
		
		
		
/*		
		
		
		
		// listening for the client sockets
		

			try {
				

				
				
				//send all the user list
				if (method.equals("initialize") == true)
					handleInitialization(socket);

				// handle Messages
				else if( method.equals("messaging") == true)
					handleMessageEvent(socket);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	private void createChatRoom() {
		// TODO Auto-generated method stub
		
	}
	}

	private void handleInitialization(Socket socket) {
		
		System.out.println("handleInitialization");
		out.println(clients.size());
		
		//need to make sure that the size doesn't change
		//need to lock certain feature if required
		for (int i=0; i<clients.size(); i++) {
			out.println(clients.get(i).getUsername());
		}
	}

	private void handleMessageEvent(Socket socket) {
		// get username
		try {

			// check if the username exists;
			String username = input.readLine();
			String username2 = input.readLine();
			int index = isUserNameExist(username);

			// if the username already exists.
			if (index != -1) {
				client = clients.get(index);
				client.setSocket(socket);
				client.setUserOn(true);

			}

			// if the username doesn't exists
			else {
				client = new Client(socket, username);
				clients.add(client);
				client.setUserOn(true);
			}

			// looking for a client socket that has the same name;
			for (int i = 0; i < clients.size(); i++) {
				if (clients.get(i).getUsername().equals(username2)) {
					CommunicationThread commThread = new CommunicationThread(
							client, clients.get(i));
					new Thread(commThread).start();
					isUserFound = true;
					updateMessageRunnable runnable = new updateMessageRunnable(
							client);
					new Thread(runnable).start();
					break;
				}
			}

			// if the Other username doesn't exist
			if (isUserFound == false) {
				// making client without socket
				Client client2 = new Client(username2);
				clients.add(client2);
				CommunicationThread commThread = new CommunicationThread(
						client, client2);
				new Thread(commThread).start();

				updateMessageRunnable runnable = new updateMessageRunnable(
						client);
				new Thread(runnable).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}*/
