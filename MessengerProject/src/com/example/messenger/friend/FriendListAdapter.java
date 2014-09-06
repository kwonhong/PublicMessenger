package com.example.messenger.friend;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.messengerproject.R;

public class FriendListAdapter extends BaseAdapter {

	private Context context;
	private List<Friend> friendsList;
	
	public FriendListAdapter(Context context, List<Friend> objects)
	{
		this.context = context;
		this.friendsList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null)
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.listview_friend, parent, false);
			
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.textView1);
			holder.txtDescrip = (TextView) convertView.findViewById(R.id.textView2);
			holder.img = (ImageView)convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtName.setText(friendsList.get(position).getName());	
		holder.txtDescrip.setText(friendsList.get(position).getStatus());
		
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView txtName, txtDescrip;
		ImageView img;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friendsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return friendsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
