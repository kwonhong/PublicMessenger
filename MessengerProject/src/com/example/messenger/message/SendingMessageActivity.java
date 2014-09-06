package com.example.messenger.message;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messengerclient.mysql.DBAdapter;
import com.example.messengerproject.R;
import com.example.messengerproject.register.BackgroundService;
import com.example.messengerproject.register.BackgroundService.LocalBinder;
import com.example.messengerproject.service.Client;

public class SendingMessageActivity extends Activity {

	private Button btnSend;
	private TextView txtView;
	private EditText edtMessage;
	private Client client;
	private DBAdapter db;
	
	private Thread updatingThread;
	private Handler updatingHandler;
	
	private ListView listview;
	private MessageListAdapter adapter;
	private List<Message> list;
	
	private boolean mBound;
	private LocalBinder binder;
	
	private String option;
	private int Room_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		db = new DBAdapter(this);
		
		Room_id = getIntent().getIntExtra("Room_id", -1);
		option = getIntent().getStringExtra("Option");
		
		listview = (ListView)findViewById(R.id.listView1);
		btnSend = (Button)findViewById(R.id.btnSend);
		edtMessage = (EditText)findViewById(R.id.edtMessage);
		
		//Connecting to the service
		Intent intent = new Intent(this, BackgroundService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String message = edtMessage.getText().toString();
				
				//send message to the server
				SendMessageToServer();
				
				list.add(new Message(client.getUsername(), message, -1, option));
				adapter.notifyDataSetChanged();
			}


		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
		
		if (updatingThread.isAlive()) {
			//interupted the updating thread!
			System.out.println("interupted the updating thread!");
			updatingThread.interrupt();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!mBound){
			Intent intent = new Intent(this, BackgroundService.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}
	}
	
	private void initiateUpdatingThread() {
		client = binder.getClient();
		list = new ArrayList<Message>();
		adapter = new MessageListAdapter(this, list, client.getUsername());

		listview.setAdapter(adapter);
		
		if (client == null) {
			//something is wrong
		} else {
			System.out.println("updating handler is running");
			updatingHandler = new Handler();
			UpdatingMessageRunnable runnable = new UpdatingMessageRunnable
							(SendingMessageActivity.this, adapter, list , Room_id, option, updatingHandler, client);
			updatingThread = new Thread(runnable);
			updatingThread.start();
		}
	}
	
	private void SendMessageToServer() {
		client = binder.getClient();
		if (mBound && client != null) {
			binder.startThreads();
			
			String mess = edtMessage.getText().toString();	
			Message message = new Message(client.getUsername(), mess, Room_id, option);
			db.createMessage(message);
			
			if (client.sendMessage(message)) {
				
			} else {
				
			}
			
		}
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {

			binder = (LocalBinder) service;
			mBound = true;
			initiateUpdatingThread();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};
}
