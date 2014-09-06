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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.messenger.message.SendingMessageActivity;
import com.example.messengerproject.R;
import com.example.messengerproject.service.Client;
import com.example.messengerproject.service.notifyService;
import com.example.messengerproject.service.notifyService.LocalBinder;

public class ChatRoomActivity extends Activity {

	private ListView listview;
	private Button btnAddChatRoom;

	private boolean mBound;
	private boolean isServiceConnected = false;

	private Client client;
	private ListViewAdapter adapter;

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_chatroom);
//
//		listview = (ListView) findViewById(R.id.list);
//		btnAddChatRoom = (Button) findViewById(R.id.btnAddChatRoom);
//
//		Intent intent = new Intent(this, notifyService.class);
//		isServiceConnected = bindService(intent, mConnection,
//				Context.BIND_AUTO_CREATE);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (isServiceConnected) {
//			unbindService(mConnection);
//			isServiceConnected = false;
//		}
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		if (!isServiceConnected) {
//			Intent intent = new Intent(this, notifyService.class);
//			isServiceConnected = bindService(intent, mConnection,
//					Context.BIND_AUTO_CREATE);
//		}
//	}
//
//	private void handleRest() {
//		btnAddChatRoom.setOnClickListener(addChatRoomListner);
//
//		adapter = new ListViewAdapter(this, client.getChatRoomsList());
//		listview.setAdapter(adapter);
//		listview.setOnItemClickListener(chattingRoomClickListner);
//	}
//
//	OnItemClickListener chattingRoomClickListner = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View view, int position,
//				long arg3) {
//			Intent intent = new Intent(ChatRoomActivity.this,
//					SendingMessageActivity.class);
//			intent.putExtra("chatroom", client.getChatRoomsList().get(position));
//			startActivity(intent);
//		}
//	};
//
//	OnClickListener addChatRoomListner = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//
//		}
//	};
//
//	private ServiceConnection mConnection = new ServiceConnection() {
//
//		@Override
//		public void onServiceConnected(ComponentName className, IBinder service) {
//			// We've bound to LocalService, cast the IBinder and get
//			// LocalService instance
//			LocalBinder binder = (LocalBinder) service;
//			client = binder.getClient();
//			handleRest();
//			mBound = true;
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName arg0) {
//			mBound = false;
//		}
//	};
}
