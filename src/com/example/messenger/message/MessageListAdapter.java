package com.example.messenger.message;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.messengerproject.R;

public class MessageListAdapter extends BaseAdapter {
	private Context context;
	private List<Message> messageList;
	private String username;
	
	public MessageListAdapter(Context context, List<Message> messageList, String username)
	{
		this.context = context;
		this.messageList = messageList;
		this.username = username;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null)
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.listview_message, parent, false);
			
			holder = new ViewHolder();
			holder.txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
			holder.img = (ImageView)convertView.findViewById(R.id.imgBox);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Message message = messageList.get(position);
		String user = message.getSender();
		
		if (user.equals(username)) {
			LayoutParams lp = (LayoutParams) holder.txtMessage.getLayoutParams();
			lp.gravity =  Gravity.RIGHT;
			holder.txtMessage.setBackgroundResource(R.drawable.rounded_box);
			holder.txtMessage.setText(message.getMessage());
		} else {
			LayoutParams lp = (LayoutParams) holder.txtMessage.getLayoutParams();
			lp.gravity =  Gravity.RIGHT;
			holder.txtMessage.setBackgroundResource(R.drawable.other_rounded_box);
			holder.txtMessage.setText(message.getMessage()+ "    //" + user);
		}
		
		//String roomId = Integer.toString(messageList.get(position).getRoom_id());
		//holder.txtName.setText(roomId);	
		//holder.txtDescrip.setText(chatList.get(position).getStatus());
		
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView txtMessage;
		ImageView img;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
