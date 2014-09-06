package com.example.messengerproject.service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.messenger.message.Message;


public class Client implements Serializable {
	private String friendFlag;
	private String accountFlag;
	private String loginFlag;
	
	private boolean isChatRoomSet;
	private boolean isNetworkOn;
	
	private Socket socket;
	private String username;
	private PrintWriter out;
	private BufferedReader input;
	private Context context;
	
	private List<String> friendList;
	private List<Member> memebrs;
	
	private int enteredChatroom_id;
	private String enteredChatroom_option;
	
	private Queue<Message> messages;
	
	
	private synchronized void connectSocket () throws InterruptedException, ExecutionException {
		//connect the socket
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Socket> chatRoomCallable = new ConnectingSocketCallable();
		Future<Socket> clientFuture = executor.submit(chatRoomCallable);
		socket = clientFuture.get();
	}
	
	public Client(Context context) {
		this.isChatRoomSet = false;
		this.isNetworkOn = false;
		this.friendFlag = null;
		this.loginFlag = null;
		this.setAccountFlag(null);
		this.context = context;
		//this.username = username;
		this.friendList = new ArrayList<String>();
		
		try {
			connectSocket();
			this.input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			this.out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			System.out.println("Error in connecting the socket \\inClient");
		} catch (InterruptedException e) {
			System.out.println("Error in connecting the socket \\inClient");
		} catch (ExecutionException e) {
			System.out.println("Error in connecting the socket \\inClient");
		}
	}
	
	public int login(String username, String password) {
		System.out.println("logging in");
		this.username = username;
		
		String send = "LOGIN#" + username + "#" + password + "#";
		if (sendMessage(send)) {
			while (loginFlag == null) {
				//don't return until freindExistFlag is not null
			}if (loginFlag.equals("SUCCESS")) {
				loginFlag = null;
				return 1;
			} else {
				loginFlag = null;
				return 0;
			}
		} else {
			return -1;
		}
	}
	
	public int createAccount (String send) {
		System.out.println("creating a new account");
		
		if (sendMessage(send)) {
			while (accountFlag == null) {
				//don't return until freindExistFlag is not null
			}
			
			if (accountFlag.equals("SUCCESS")) {
				accountFlag = null;
				return 1;
			} else if (accountFlag.equals("EXIST")) {
				accountFlag = null;
				return 2;
			}
		}
		
		return -1;
	}
	
	public boolean createNewChat (String chat) {
		String send = "CREATECHAT#" + chat +"#PUBLIC#";
		
		if (sendMessage(send)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean createNewFriend(String friend) {
		System.out.println("creating a new friend");
		String send = "FRND#ADD#" + friend + "#";
		
		friendFlag = null;
		if (sendMessage(send)) {
			while (friendFlag == null) {
				//don't return until friendExistFlag is true
			}
			
			if (friendFlag.equals("EXIST")) {
				addFriend(friend);
			} else {
				
			}
			return true;
		} else {
			System.out.println("network problem sir");
			return false;
		}

	}
	
	public void addFriend(String friend) {
		friendList.add(friend);
	}
	
	public void setChatRoomOn(int chatRoomID) {
		System.out.println("setting chat room on !!!!!!");
		enteredChatroom_id = chatRoomID;
		messages = new LinkedList<Message>();
	}
	
	public void unsetChatRoomOn() {
		enteredChatroom_id = -1;
		messages = null;
	}
	
	public Queue<Message> getMessages() {
		return messages;
	}
	
	public synchronized void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		if (input == null) 
			return null;
		
		try {
			return input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean sendMessage(String message) {
		if (out == null)
			return false;
		
		if (haveNetworkConnection()) {
			out.println(message);
			return true;
		}
		return false;
	}
	
	public boolean sendMessage(Message message) {
		if(haveNetworkConnection()) {
			String SendingMessage = encrypteMessage(message);
			out.println(SendingMessage);
			return true;
		}
		
		return false;
	}

	//MESG#message#option#chatroom_id#date#
	private String encrypteMessage(Message message) {
		//message#chatroom#date#
		String mess = message.getMessage();
		int chatRoomID = message.getChatroom_id();
		int date = getCurrentTime();
		String option = message.getOption();
		
		String build = "MESG#" + mess + "#" + option + "#" + chatRoomID + "#" + date + "#";
		return build;
	}
	
	// helper function to deternmine if the internet connection is on
		private boolean haveNetworkConnection() {
			boolean haveConnectedWifi = false;
			boolean haveConnectedMobile = false;

			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			for (NetworkInfo ni : netInfo) {
				if (ni.getTypeName().equalsIgnoreCase("WIFI"))
					if (ni.isConnected())
						haveConnectedWifi = true;
				if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
					if (ni.isConnected())
						haveConnectedMobile = true;
			}
			return haveConnectedWifi || haveConnectedMobile;
		}
		
		private int getCurrentTime() {
			DateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
			Calendar cal = Calendar.getInstance();
			
			String date = dateFormat.format(cal.getTime());
			return Integer.parseInt(date);
		}

		public void addMessageOnQueue(Message mess) {
			System.out.println("putting messages on the queue");
			if (messages != null) {
				int chatID = mess.getChatroom_id();
				if (chatID == enteredChatroom_id) {
					messages.add(mess);
				}
			}
			return;
			
		}

		public List<String> getFriendList() {
			return friendList;
		}

		public void setFriendList(List<String> friendList) {
			this.friendList = friendList;
		}

		public String getFriendFlag() {
			return friendFlag;
		}

		public void setFriendFlag(String friendFlag) {
			this.friendFlag = friendFlag;
		}

		public boolean isNetworkOn() {
			return isNetworkOn;
		}

		public void setNetworkOn(boolean isNetworkOn) {
			this.isNetworkOn = isNetworkOn;
		}

		public String getAccountFlag() {
			return accountFlag;
		}

		public void setAccountFlag(String accountFlag) {
			this.accountFlag = accountFlag;
		}

		public boolean isChatRoomSet() {
			return isChatRoomSet;
		}

		public void setChatRoomSet(boolean isChatRoomSet) {
			this.isChatRoomSet = isChatRoomSet;
		}

		public String getLoginFlag() {
			return loginFlag;
		}

		public void setLoginFlag(String loginFlag) {
			this.loginFlag = loginFlag;
		}

		public List<Member> getMemebrs() {
			return memebrs;
		}

		public void setMemebrs(List<Member> memebrs) {
			this.memebrs = memebrs;
		}



}
