package com.example.messenger.chatroom;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.messengerproject.R;

public class ListViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mList;
	
	public ListViewAdapter(Context context, List<String> list) {
		this.mContext = context;
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}
	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater)
					mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	//		convertView = inflater.inflate(R.layout.view_list_item, null);
		}
		//
		convertView.setMinimumHeight(100);
		String temp = mList.get(position);
		
	//	TextView txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
	//	txtTitle.setText(temp);
		
		return convertView;
	}

}