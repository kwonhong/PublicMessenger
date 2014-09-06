package com.example.messengerproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messengerproject.register.BackgroundService;
import com.example.messengerproject.register.BackgroundService.LocalBinder;
import com.example.messengerproject.register.RegisterActivity;
import com.example.messengerproject.service.Client;
import com.example.messengerproject.slidingmenu.SlidingActivity;

public class MainActivity extends ActionBarActivity {

	private Button btnRegister;
	private Button btnLogin;
	
	private EditText edtUsername;
	private EditText edtPassword;
	
	private LocalBinder binder;
	private boolean mBound;
	
	private final static String NETWORK_ERROR = "network error try later...";
	private final static String SERVICE_ERROR = "service is not bounded yet.. try later";
	private final static String WRONG_PASSWORD = "wrong password...";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);      
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		
		setContentView(R.layout.activity_main);

		// start the service
		Intent intent = new Intent(MainActivity.this, BackgroundService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		
		edtUsername = (EditText)findViewById(R.id.edtUsername);
		edtPassword = (EditText)findViewById(R.id.edtPassword);
		
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (mBound) {
					login(edtUsername.getText().toString(), edtPassword.getText().toString());

				} else {
					Toast.makeText(MainActivity.this, SERVICE_ERROR, 1000).show();
				}
				
			}
		});

	}
	
	private void login(String username, String password) {
		Client client = binder.getClient();
		binder.startThreads();
		int result = client.login(username, password);
		if (result == 1) {
			Intent intent = new Intent(MainActivity.this, SlidingActivity.class);
			startActivity(intent);
		} else if  (result == 0) {
			Toast.makeText(MainActivity.this, WRONG_PASSWORD, 1000).show();
		} else {
			Toast.makeText(MainActivity.this, NETWORK_ERROR, 1000).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
