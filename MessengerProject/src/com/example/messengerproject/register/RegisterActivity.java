package com.example.messengerproject.register;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messengerproject.MainActivity;
import com.example.messengerproject.R;
import com.example.messengerproject.register.BackgroundService.LocalBinder;
import com.example.messengerproject.service.Client;
import com.example.messengerproject.slidingmenu.SlidingActivity;

public class RegisterActivity extends ActionBarActivity {
	
	private EditText usernameEditText;
	private EditText passwordEditText;
	private EditText passwordRetypeEditText;
	private Button registerBtn;
	
	private Client client;
	private boolean mBound;
	
	private ProgressDialog pDialog;
	private LocalBinder binder;

	private final static String NETWORK_ERROR = "network error try later...";
	private final static String SERVICE_ERROR = "service is not bounded yet.. try later";
	private final static String DUPLICATE_ERROR = "username exist already";
	private final static String WRONG_PASSWORD = "wrong password...";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		usernameEditText = (EditText) findViewById(R.id.editText_username);
		passwordEditText = (EditText) findViewById(R.id.editText_password);
		passwordRetypeEditText = (EditText) findViewById(R.id.editText_passwordRetype);
		registerBtn = (Button)findViewById(R.id.btnRegister);
		
		//start the service
		Intent intent = new Intent(RegisterActivity.this, BackgroundService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		
		registerBtn.setOnClickListener(registerListener);
	}

	OnClickListener registerListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			client = binder.getClient();
			if (mBound && client != null) {
				String username = usernameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				makePDialog(RegisterActivity.this, "Creating An Account..");
				
				binder.startThreads();
				String send = combineMessage(username, password);
				int result = client.createAccount(send);
				if (result == 1) {
					//succeeded making an account
					pDialog.dismiss();
					saveAccountInShared(username, password);
					login(username, password);
				} else if (result == -1) {
					pDialog.dismiss();
					Toast.makeText(RegisterActivity.this, NETWORK_ERROR, 1000).show();
				} else {
					pDialog.dismiss();
					Toast.makeText(RegisterActivity.this, DUPLICATE_ERROR, 1000).show();
				}
				
			} else if (client == null && mBound) {
				Toast.makeText(RegisterActivity.this, NETWORK_ERROR, 1000).show();
			} else{
				Toast.makeText(RegisterActivity.this, SERVICE_ERROR, 1000).show();
			}
		}
	};
	
	
	private void login(String username, String password) {
		client = binder.getClient();
		binder.startThreads();
		int result = client.login(username, password);
		if (result == 1) {
			Intent intent = new Intent(RegisterActivity.this, SlidingActivity.class);
			startActivity(intent);
		} else if  (result == 0) {
			Toast.makeText(RegisterActivity.this, WRONG_PASSWORD, 1000).show();
		} else {
			Toast.makeText(RegisterActivity.this, NETWORK_ERROR, 1000).show();
		}
	}


	private String combineMessage(String username, String password) {
		String send = "ACCOUNT#" + username + "#" + password +"#";
		return send;
	}

	private void makePDialog(Context context, String message) {
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(message);
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}
	
	//save the account to the device database
	private void saveAccountInShared (String username, String password) {
	
			final String TAG_USERNAME = "username";
			final String TAG_PASSWORD = "password";
		
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
			Editor editor = pref.edit();

			editor.putString(TAG_USERNAME, username);
			editor.putString(TAG_PASSWORD, password);
			editor.commit();
	}
	

	//need to check if the binder is null -> "null" means the network error happened
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
	
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//start the service
		Intent intent = new Intent(RegisterActivity.this, BackgroundService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	

	
	

	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_activity_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*int id = item.getItemId();
		if (id == R.id.action_submit) {
			return true;
		} else if (id == R.id.action_cancel) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}


}
