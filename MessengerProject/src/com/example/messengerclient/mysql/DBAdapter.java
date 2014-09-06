package com.example.messengerclient.mysql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.messenger.chatroom.ChatRoom;
import com.example.messenger.message.Message;

public class DBAdapter extends SQLiteOpenHelper {

	// DATABASE
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "messenger.db";
	
	// Common keys
	private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_SENDER = "sender";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_OPTION = "option";
    private static final String KEY_CHAT_ID = "chat_id";
    private static final String KEY_CHATROOM = "chatroom";
    private static final String KEY_ISCHECKED = "is_checked";
    private static final String KEY_FRIEND = "friend";
    // 0 -> NOT CHECKED
    // 1 -> CHECED
	
	// Tables
	private static final String TABLE_MESSAGES = "messages";
	private static final String TABLE_SEND = "send";
	private static final String TABLE_CHATROOM = "chatrooms";
	private static final String TABLE_FRIEND = "friends";
	
	//Table create table
	private static final String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
			+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_SENDER + " TEXT, " + KEY_MESSAGE + " TEXT, " + 
			KEY_CHATROOM + " INTEGER, "  + KEY_OPTION + " TEXT, "+KEY_ISCHECKED + " INTEGER, "+ KEY_CREATED_AT + " INTEGER" + ")";

	private static final String CREATE_CHATROOMS_TABLE = "CREATE TABLE " +  TABLE_CHATROOM + "("
			+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CHATROOM + " TEXT, " + KEY_OPTION + " TEXT, " + 
			KEY_CHAT_ID + " INTEGER" + ")";
	
	private static final String CREATE_FRIEND_TABLE = "CREATE TABLE " +  TABLE_FRIEND + "("
			+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_FRIEND + " TEXT" + ")";
			
	
//	private static final String CREATE_SEND_TABLE = "CREATE TABLE " + TABLE_SEND +  "("
//			+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_MESSAGE + " TEXT, " + KEY_CHATROOM + " TEXT, "
//			+ KEY_CREATED_AT + " INTEGER)";
	
	public DBAdapter(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_MESSAGE_TABLE);
		db.execSQL(CREATE_CHATROOMS_TABLE);
		db.execSQL(CREATE_FRIEND_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
    	db.execSQL("DROP TABLE IF EXISTS " + CREATE_MESSAGE_TABLE);
    	db.execSQL("DROP TABLE IF EXISTS " + CREATE_CHATROOMS_TABLE);
    	db.execSQL("DROP TABLE IF EXISTS " + CREATE_FRIEND_TABLE);
        // create new tables
        onCreate(db);
	}
	
	public long createMessage (Message message) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_CHATROOM, message.getChatroom_id());
		values.put(KEY_MESSAGE, message.getMessage());
		values.put(KEY_SENDER, message.getSender());
		values.put(KEY_OPTION, message.getOption());
		values.put(KEY_ISCHECKED, 0);
		if (message.getDate() == 0)
			values.put(KEY_CREATED_AT, getCurrentTime());
		else 
			values.put(KEY_CREATED_AT, message.getDate());
		
		return db.insert(TABLE_MESSAGES, null, values);
	}
	
	public long createChat (ChatRoom chat) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_CHATROOM, chat.getChatroom());
		values.put(KEY_OPTION, chat.getOption());
		values.put(KEY_CHAT_ID, chat.getRoom_id());
		return db.insert(TABLE_CHATROOM, null, values);
	}
	
//	public long createSendMessage (Message message) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		
//		ContentValues values = new ContentValues();
//		values.put(KEY_CHATROOM, message.get());
//		values.put(KEY_MESSAGE, message.getMessage());
//		values.put(KEY_CREATED_AT, getCurrentTime());
//		
//		return db.insert(TABLE_SEND, null, values);
//	}

//	public Message getSingleSendMessage() {
//		SQLiteDatabase db = this.getReadableDatabase();
//		
//		String selectQuery = "SELECT * FROM " + TABLE_SEND + " LIMIT 1";
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		
//		if (cursor != null && cursor.getCount() > 0 &&  cursor.moveToFirst()) {
//			String message = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE));
//			String chatroom = cursor.getString(cursor.getColumnIndex(KEY_CHATROOM));
//			int time = cursor.getInt(cursor.getColumnIndex(KEY_CREATED_AT));
//			long id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
//			
//			
//			Message mess = new Message(null, message, chatroom, time, id);
//			
//			//closing the cursor
//			if(cursor != null)
//		        cursor.close();
//			
//			return mess;
//		}
//		
//		//closing the cursor
//		if(cursor != null)
//	        cursor.close();
//		return null;
//	}
	
	
	public List<Message> getMessages(int chatRoomID) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_MESSAGES + " WHERE "
				+ KEY_CHATROOM + " = " + chatRoomID + " ORDER BY " + KEY_CREATED_AT + " ASC";
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		List<Message> message = new ArrayList<Message>();
		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
		{
			do 
			{
				int chatroomID = cursor.getInt(cursor.getColumnIndex(KEY_CHATROOM));
				String sender = cursor.getString(cursor.getColumnIndex(KEY_SENDER));
				String mMessage = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE));
				int date = cursor.getInt(cursor.getColumnIndex(KEY_CREATED_AT));
				String option = cursor.getString(cursor.getColumnIndex(KEY_OPTION));
				long messageId = cursor.getLong(cursor.getColumnIndex(KEY_ID));
				//int isChecked = cursor.getInt(cursor.getColumnIndex(KEY_ISCHECKED));
				MessageIsChecked(messageId);
				
				message.add(new Message(sender, mMessage, chatroomID,option, date, messageId));
			} while (cursor.moveToNext());
		}
		
		//closing the cursor
		if(cursor != null)
	        cursor.close();
		
		return message;
	}
	
	public List<ChatRoom> getChatrooms(String option) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_CHATROOM + " WHERE "
				+ KEY_OPTION + " = '" + option + "' ORDER BY " + KEY_ID + " DESC";
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		List<ChatRoom> chatrooms = new ArrayList<ChatRoom>();
		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
		{
			do 
			{
				String chatroom = cursor.getString(cursor.getColumnIndex(KEY_CHATROOM));
				String mOption = cursor.getString(cursor.getColumnIndex(KEY_OPTION));
				int room_id = cursor.getInt(cursor.getColumnIndex(KEY_CHAT_ID));
				
				chatrooms.add(new ChatRoom(room_id, chatroom, mOption));
			} while (cursor.moveToNext());
		}
		
		//closing the cursor
		if(cursor != null)
	        cursor.close();
		
		return chatrooms;
	}
	
	public int MessageIsChecked(long messageId) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_ISCHECKED, 1);
		
		return db.update(TABLE_MESSAGES, values, KEY_ID + " = " + messageId, null);
	}
	
	public void deleteMessage (long messageId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MESSAGES, KEY_ID + " = " + messageId, null);
	}
	
	public void deleteSendMessage (long messageId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SEND, KEY_ID + " = " + messageId, null);
	}
	
	private int getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
		Calendar cal = Calendar.getInstance();
		
		String date = dateFormat.format(cal.getTime());
		return Integer.parseInt(date);
	}
	
}
