package com.example.messenger.chatroom;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.messengerproject.R;

public class ChatListAdapter extends BaseAdapter {
	private Context context;
	private List<ChatRoom> chatList;
	
	public ChatListAdapter(Context context, List<ChatRoom> chatList)
	{
		this.context = context;
		this.chatList = chatList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null)
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.listview_chatroom, parent, false);
			
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
		
		String roomId = chatList.get(position).getChatroom();
		holder.txtName.setText(roomId);	
		//holder.txtDescrip.setText(chatList.get(position).getStatus());
		
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
		return chatList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return chatList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
