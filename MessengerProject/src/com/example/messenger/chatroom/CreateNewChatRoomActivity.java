package com.example.messenger.chatroom;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.messengerproject.R;
import com.example.messengerproject.register.BackgroundService;
import com.example.messengerproject.register.BackgroundService.LocalBinder;
import com.example.messengerproject.service.Client;

public class CreateNewChatRoomActivity extends Activity {


	private ListView listview;
	private Button btnCreateRoom;
	
	private boolean mBound;
	private LocalBinder binder;
	private Client client;
	
	private Button btnCreate;
	private EditText edtChat;
	
	private final static String NETWORK_ERROR = "network error try later...";
	private final static String SERVICE_ERROR = "service is not bounded yet.. try later";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_chat);
		
		//Connecting to the service
		Intent intent = new Intent(this, BackgroundService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		btnCreate = (Button)findViewById(R.id.btnCreate);
		edtChat = (EditText)findViewById(R.id.edtTxtChatRoom);
		
		btnCreate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String chatName = edtChat.getText().toString();
				handleCreatChat(chatName);
				
			}
		});
	}
	
	//CREATECHAT#chattRoom#option#
		private void handleCreatChat(String chatroom) {
			client = binder.getClient();
			if (mBound && client != null) {
				binder.startThreads();
				
				if (client.createNewChat(chatroom)) {
					while (!client.isChatRoomSet()) {
						
					}
					client.setChatRoomSet(false);
					//setNewAdapter();
					
				} else {
					Toast.makeText(this, NETWORK_ERROR, 1000).show();
				}
				
			}
		}
		
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
		
}
