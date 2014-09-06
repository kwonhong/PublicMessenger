package com.example.messenger.network;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.example.messengerproject.register.BackgroundService;
import com.example.messengerproject.register.RegisterActivity;
import com.example.messengerproject.register.BackgroundService.LocalBinder;
import com.example.messengerproject.service.Client;
import com.example.messengerproject.service.MessageHandleRunnable;

public class NetworkRunnable implements Runnable {

	private Context context;
	private Client client;
	private Thread messageThread;
	
	private LocalBinder binder;
	private boolean mBound;

	public NetworkRunnable(Context context, Client client, Thread messageThread) {
		this.context = context;
		this.client = client;
		this.messageThread = messageThread;
	}

	@Override
	public void run() {

		// start the service
		Intent intent = new Intent(context, BackgroundService.class);
		context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// case1. Network is available -> start HandleMessageRunnable
			if (!haveNetworkConnection()) {
				client.setNetworkOn(false);
				if (messageThread.isAlive())
					messageThread.interrupt();
				
				if (mBound == true && binder.getClient() != null) {
					binder.disconnectSocket();
				}


			}

			// case2. Network is not available -> stop HandleMessageRunnable
			else {
				// System.out.println("Network is available");
				client.setNetworkOn(true);
				if (!messageThread.isAlive()) {
					messageThread = new Thread(new MessageHandleRunnable(
							context, client));
					messageThread.start();
				}
				
				//connect the socket
				if (mBound == true && binder.getClient() == null) {
					binder.connectSocket();
				}

			}

		}
	}

	// need to check if the binder is null -> "null" means the network error happened
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {

			binder = (LocalBinder) service;
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	// helper function to deternmine if the internet connection is on
	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
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
