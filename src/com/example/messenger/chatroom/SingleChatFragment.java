package com.example.messenger.chatroom;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.messenger.message.SendingMessageActivity;
import com.example.messengerproject.R;

public class SingleChatFragment extends Fragment {

	private ListView listview;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_singlechat, container,
				false);

		listview = (ListView) rootView.findViewById(R.id.listView1);

		List<ChatRoom> list = new ArrayList<ChatRoom>();

		ChatListAdapter adapter = new ChatListAdapter(getActivity(), list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(getActivity(), SendingMessageActivity.class);
				startActivity(intent);
			}
		});

		return rootView;

	}
}
