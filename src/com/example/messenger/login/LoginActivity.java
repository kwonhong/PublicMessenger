package com.example.messenger.login;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity{
	Button connectBtn;
	EditText edtUser;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_login);
//		
//		edtUser = (EditText) findViewById(R.id.edtUser);
//		connectBtn = (Button) findViewById(R.id.btnConnect);
//		connectBtn.setOnClickListener(connectBtnListner);
//		
//	}
//	
//	OnClickListener connectBtnListner = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//
//			String username = edtUser.getText().toString();
//			if (username == null) {
//				Toast.makeText(LoginActivity.this, "Enter the username", 1000).show();
//			} else {
//		        
//				//start the service
//				Intent intent = new Intent(LoginActivity.this, notifyService.class);
//		        intent.putExtra("username", username);
//		        startService(intent);
//		        
//		        //move to other activity
//		        intent = new Intent(LoginActivity.this, FriendActivity.class);
//		        startActivity(intent);
//		        
//		        
//			}
//		}
//	};

}
