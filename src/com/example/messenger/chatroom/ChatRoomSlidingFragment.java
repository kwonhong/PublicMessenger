package com.example.messenger.chatroom;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.messengerproject.R;

public class ChatRoomSlidingFragment extends Fragment {

	DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	ViewPager mViewPager;

	public ChatRoomSlidingFragment() {
	
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_friend, container,
				false);

		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
				getActivity().getSupportFragmentManager());
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);

		final ActionBar actionBar = getActivity().getActionBar();

		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a tab listener that is called when the user changes tabs.
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab arg0,
					android.app.FragmentTransaction arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTabSelected(ActionBar.Tab tab,
					android.app.FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab arg0,
					android.app.FragmentTransaction arg1) {
				// TODO Auto-generated method stub
			}
		};


		actionBar.addTab(actionBar.newTab().setText("Single").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Group").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Public").setTabListener(tabListener));
		

		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between pages, select the
						// corresponding tab.
						getActivity().getActionBar().setSelectedNavigationItem(position);
					}
				});

		return rootView;
	}

	public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
		public DemoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			
			Fragment fragment = null;
			if ( i == 0 ) {
				fragment = new SingleChatFragment();
			} else if (i == 1) {
				fragment = new GroupChatFragment();
			} else if (i == 2) {
				fragment = new PublicChatFragment();
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "OBJECT " + (position + 1);
		}
	}

	public static class DemoObjectFragment extends Fragment {
		public static final String ARG_OBJECT = "object";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// The last two arguments ensure LayoutParams are inflated
			// properly.
			View rootView = inflater.inflate(R.layout.fragment_community,
					container, false);
			return rootView;
		}
	}

}
