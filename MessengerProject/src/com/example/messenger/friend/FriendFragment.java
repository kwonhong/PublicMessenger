package com.example.messenger.friend;

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
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.messengerproject.R;
import com.example.messengerproject.register.BackgroundService;
import com.example.messengerproject.register.BackgroundService.LocalBinder;
import com.example.messengerproject.service.Client;

public class FriendFragment extends Fragment {

	private ListView listview;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_friends, container,
				false);

		listview = (ListView) rootView.findViewById(R.id.listView1);

		List<Friend> list = new ArrayList<Friend>();
//		list.add(new Friend("Friend1", "nothing much"));
//		list.add(new Friend("Friend2", "very good"));

		FriendListAdapter adapter = new FriendListAdapter(getActivity(), list);
		listview.setAdapter(adapter);

		return rootView;

	}
	

}
