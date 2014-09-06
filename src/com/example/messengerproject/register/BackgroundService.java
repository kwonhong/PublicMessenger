package com.example.messengerproject.register;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.example.messenger.network.NetworkRunnable;
import com.example.messengerproject.service.Client;
import com.example.messengerproject.service.MessageHandleRunnable;

public class BackgroundService extends Service {

	private final IBinder mBinder = new LocalBinder();
	private Thread messageHandleThread;
	private Thread networkHandleThread;
	private Client client;
	private boolean isClientConnected;

	//Whenever other activity binds to the service, they get access to this class
	public class LocalBinder extends Binder {
		public Client getClient() {
			return client;
		}
		
		public void startThreads() {
			createThreads();
		}
		
		public void connectSocket() {
			connectClient();
		}
		
		public void disconnectSocket() {
			disconnectClient();
		}
	}

	//Whenever binding/starting the service, create the "client" object
	//Check if the client object is "null"
	@Override
	public IBinder onBind(Intent arg0) {
		System.out.println("returning the binder");
		if (client == null && connectClient()) {
			isClientConnected = true;
		} else  {
			isClientConnected = false;
		}
		
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (client == null && connectClient()) {
			isClientConnected = true;
		} else {
			isClientConnected = false;
		}

		System.out.println("starting the service");
		return Service.START_NOT_STICKY;
	}



	//When the service is dying, deallocate the "client" object
	//Send message to the server side
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "SERVICE IS DYING", 1000).show();
		
		if (isClientConnected) {
			client.sendMessage("EXIT#");
			client = null;
		} else {
			
		}
	}



	//Threads to receive message, check the network availability
	private void createThreads() {
		//Check if the threads are running
		
		if (messageHandleThread == null || !messageHandleThread.isAlive()) {
			MessageHandleRunnable runnable = new MessageHandleRunnable(this,client);
			messageHandleThread = new Thread(runnable);
			messageHandleThread.start();
		}

		// Network thread
		if (networkHandleThread == null  || !networkHandleThread.isAlive()) {
			networkHandleThread =  new Thread(new NetworkRunnable(this, client, messageHandleThread));
			networkHandleThread.start();
		}
	}

	//making a new client -> connecting the sokcet
	private boolean connectClient() {
		// Check if the Internet is available
		if (haveNetworkConnection()) {
			client = new Client(this);
			isClientConnected = true;
			return true;
		}
		return false;
	}

	private boolean disconnectClient() {
		client.closeSocket();
		client = null;
		isClientConnected= false;
		return true;
	}


	//Checking the network connection
	private boolean haveNetworkConnection() {
			boolean haveConnectedWifi = false;
			boolean haveConnectedMobile = false;

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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


}