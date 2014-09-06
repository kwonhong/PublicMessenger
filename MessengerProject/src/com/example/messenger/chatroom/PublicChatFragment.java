package com.example.messenger.chatroom;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.messenger.message.SendingMessageActivity;
import com.example.messengerclient.mysql.DBAdapter;
import com.example.messengerproject.R;
import com.example.messengerproject.register.BackgroundService;
import com.example.messengerproject.register.BackgroundService.LocalBinder;
import com.example.messengerproject.service.Client;

public class PublicChatFragment extends Fragment {

	private ListView listview;
	private Button btnCreateRoom;
	private DBAdapter db;
	
	private boolean mBound;
	private LocalBinder binder;
	private Client client;
	private List<ChatRoom> list;
	private List<Client> members;
	private ChatListAdapter adapter;

	private final static String NETWORK_ERROR = "network error try later...";
	private final static String SERVICE_ERROR = "service is not bounded yet.. try later";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_publicchat,
				container, false);
		
		db = new DBAdapter(getActivity());

		//Connecting to the service
		Intent intent = new Intent(getActivity(), BackgroundService.class);
		getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		listview = (ListView) rootView.findViewById(R.id.listView1);
		btnCreateRoom = (Button)rootView.findViewById(R.id.btnCreateRoom);
		
		list = db.getChatrooms("PUBLIC");

		adapter = new ChatListAdapter(getActivity(), list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				informServerEnteringChat(list.get(position));
			}
		});
		
		btnCreateRoom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),CreateNewChatRoomActivity.class);
				startActivity(intent);
			}

		});
		return rootView;

	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!mBound){
			Intent intent = new Intent(getActivity(), BackgroundService.class);
			getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (mBound) {
			getActivity().unbindService(mConnection);
			mBound = false;
		}
	}
	
	
	
	private void getListOfMembers() {
		String chatroom = null;
		
	}

	private void setNewAdapter() {
		List<ChatRoom> list2 = db.getChatrooms("PUBLIC");
		adapter = new ChatListAdapter(getActivity(), list2);
		listview.setAdapter(adapter);
		list = list2;
	}

	private void informServerEnteringChat(ChatRoom chat) {

				client.setChatRoomOn(chat.getRoom_id());
				Intent intent = new Intent(getActivity(),SendingMessageActivity.class);
				intent.putExtra("Room_id", chat.getRoom_id());
				intent.putExtra("Option", "PUBLIC");
				startActivity(intent);

	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {

			binder = (LocalBinder) service;
			client = binder.getClient();
			mBound = true;

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};
}
