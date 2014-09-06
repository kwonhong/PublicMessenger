package com.example.messengerproject.service;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

import com.example.messenger.network.NetworkRunnable;

public class notifyService extends Service {
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();
	private Thread messageHandleThread;
	private Thread sendingMessageThread;
	private Client client;

	private static final String TAG_CHAT_NUMBER = "chat_number";
	private static final String TAG_CHATROOM = "chatroom";

	public class LocalBinder extends Binder {
		public Client getClient() {
			return client;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("returning the binder");
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("starting the service");
		handleMessages(intent);
		return Service.START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// informing server that service is dying
		// telling the server not to send more messages
		System.out.println("service is dying");
		System.out.println("service is dying");
		System.out.println("service is dying");

		client.sendMessage("serviceDied");
		//saveChatRooms();
	}

	private void handleMessages(Intent intent) {
		if (isUserCreated(intent)) {
			System.out.println("user has been created correctly");

			// storing/restoring chatroom information
			System.out.println("restoring chatroom information");
			//restoreChatRooms();

			// New Thread to handle messages
			MessageHandleRunnable runnable = new MessageHandleRunnable(this,
					client);
			messageHandleThread = new Thread(runnable);
			messageHandleThread.start();

			// Network thread
			new Thread(new NetworkRunnable(this, client, messageHandleThread))
					.start();

		}

		else {
			System.out.println("user has not been created");
			return;
		}
	}

	/*private void restoreChatRooms() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", 0); // 0 - for private mode

		int size = pref.getInt(TAG_CHAT_NUMBER, -1);
		for (int i = 0; i < size; i++) {
			String index = TAG_CHATROOM + i;
			client.addChatRoom(pref.getString(index, null));
		}
	}

	private void saveChatRooms() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", 0); // 0 - for private mode
		Editor editor = pref.edit();

		List<String> list = client.getChatRoomsList();
		int size = list.size();
		System.out.println("size is this -> " + size);
		editor.putInt(TAG_CHAT_NUMBER, client.getChatRoomsList().size());

		for (int i = 0; i < size; i++) {
			String index = TAG_CHATROOM + i;
			editor.putString(index, list.get(i));
		}

		editor.commit();
	}
*/
	private boolean isUserCreated(Intent intent) {
		// Check if the Internet is available
		if (haveNetworkConnection()) {

			String username = intent.getStringExtra("username");
			System.out.println(username);
			client = new Client(this);
			client.sendMessage(username);
			return true;

		}
		return false;
	}

	// helper function to deternmine if the internet connection is on
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
