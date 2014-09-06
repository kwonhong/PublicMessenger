package com.example.messenger.friend;

import com.example.messengerproject.R;
import com.example.messengerproject.register.BackgroundService;
import com.example.messengerproject.register.BackgroundService.LocalBinder;
import com.example.messengerproject.service.Client;

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

public class NewFriendFragment extends Fragment {

	private boolean mBound;
	private LocalBinder binder;
	private Client client;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_friends, container,
				false);
		
		// Connecting to the service
		Intent intent = new Intent(getActivity(), BackgroundService.class);
		getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

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

