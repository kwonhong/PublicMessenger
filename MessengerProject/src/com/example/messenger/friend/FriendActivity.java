package com.example.messenger.friend;

import java.util.Random;

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
import android.widget.EditText;
import android.widget.ListView;

import com.example.messenger.chatroom.ChatRoomActivity;
import com.example.messenger.chatroom.ListViewAdapter;
import com.example.messengerproject.R;
import com.example.messengerproject.service.Client;
import com.example.messengerproject.service.notifyService;
import com.example.messengerproject.service.notifyService.LocalBinder;

public class FriendActivity extends Activity{
	
	private Button btnSearch;
	private Button btnChat;
	private EditText edtFriend;
	private ListView listview;
	private ListViewAdapter adapter;
	
	private Client client;
	private boolean mBound;
	private boolean isServiceConnected = false; 
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_friend);
//		
//		edtFriend = (EditText)findViewById(R.id.edtFriend);
//		btnSearch = (Button)findViewById(R.id.btnSearch);
//		btnChat = (Button)findViewById(R.id.btnChatRoom);
//		listview = (ListView)findViewById(R.id.listView);
//		
//		
//		Intent intent = new Intent(this, notifyService.class);
//		isServiceConnected = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//	}
//	
//	
//	private void handleRest() {
//		btnSearch.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				String friend = edtFriend.getText().toString();
//				if (client.createNewFriend(friend)) {
//					adapter.notifyDataSetChanged();
//				} else {
//					//fail
//				}
//				
//			}
//		});
//		
//		btnChat.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(FriendActivity.this, ChatRoomActivity.class);
//				startActivity(intent);
//			}
//		});
//		
//		adapter = new ListViewAdapter(FriendActivity.this, client.getFriendList());
//		listview.setAdapter(adapter);
//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long arg3) {
//				
//				String chatRoom = "room"+ new Random().nextInt();
//				client.addChatRoom(chatRoom);
//				
//				//sending to the other users
//				String otherUser = client.getFriendList().get(position);
//				System.out.println("creating a new chat room");
//				if (CreateChatRoom(chatRoom, otherUser)) {
//					
//				}
//				
//				else {
//					
//				}
//				
//			}
//		});
//			
//	}
//	
//	private boolean CreateChatRoom (String chatRoom, String otherUser) {
//		String sendingMessage = "CREATECHAT#" + chatRoom + "#" + otherUser + "#";
//		if (client.sendMessage(sendingMessage)) {
//			return true;
//		}
//		return false;
//	}
//	
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
//		if (!isServiceConnected){
//			Intent intent = new Intent(this, notifyService.class);
//			isServiceConnected = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//		}
//	}
//	
//
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
