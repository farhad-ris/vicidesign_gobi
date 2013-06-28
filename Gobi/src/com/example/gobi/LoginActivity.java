package com.example.gobi;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseAnalytics;

public class LoginActivity extends Activity {

	private SyncUpToCloud sutc;
	private EditText userEmail;
	private EditText userPassword;
	private SyncUpLogin sul;
	public static final String TAG = LoginActivity.class.getSimpleName();
	private SharedPreferences pref;

	private boolean loginButtonClicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		pref = getApplicationContext().getSharedPreferences("login_status", MODE_PRIVATE);
		boolean loggedIn = pref.getBoolean("loggedIn", false);
		if (loggedIn) {
			Intent i = new Intent(LoginActivity.this, TabhostLayoutActivity.class);
			startActivity(i);
			finish();
		} else {

			setContentView(R.layout.login);
			sutc = new SyncUpToCloud(getApplicationContext());
			sul = new SyncUpLogin(getApplicationContext());

			//------------------REGISTER-------------------------------------
			Button registerButton = (Button) findViewById(R.id.arrowRegister);
			registerButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
					startActivity(i);

				}
			});

			//--------------------------LOGIN----------------------------------

			userEmail = (EditText) findViewById(R.id.userEmail);
			userEmail.setOnTouchListener(new View.OnTouchListener() {
				int count = 0;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					count++;
					if(count ==1){
						userEmail.getText().clear();
					}
					return false;
				}
			});

			userPassword = (EditText) findViewById(R.id.userPassword);
			userPassword.setOnTouchListener(new View.OnTouchListener() {
				int count = 0;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					count++;
					if(count ==1){
						userPassword.getText().clear();
					}
					return false;

				}
			});
			Button loginButton = (Button) findViewById(R.id.gobiLogin);
			loginButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!loginButtonClicked) {
						loginButtonClicked = true;
						login(userEmail.getText().toString(), userPassword.getText().toString());
					}
				}
			});
		}
	}

	@Override
	public void onBackPressed(){
	}
	//------------------------ENCODE--------------------------------------------------------------
	public void login(String userEmail, String userPassword){
		//create instanse of SyncUpTask
		SyncUpLogin sul = new SyncUpLogin(getApplicationContext());


		//create Http Post
		HttpPost httpPost = new HttpPost("http://vicidesign.net/gobi/loginNew.php");
		//create instanse of Name value pairs that would hold value that will be sent to cloud
		List<NameValuePair> nvp= new ArrayList<NameValuePair>();
		//iterate through arrayList and add values into nvp for each task record	
		nvp.add(new BasicNameValuePair("EMAIL", userEmail));
		nvp.add(new BasicNameValuePair("PASSWORD", userPassword));
		try {
			//encode nvp and send it with http post
			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e){
			Log.e(TAG, "unsupported encoding error FROM LOGIN", e);
		}
		sul.setHttpPost(httpPost);
		sul.execute();
	}
	//---------------------------------RECEIVE FROM CLOUD----------------------------------------------
	class SyncUpLogin extends JSONClassLogin {
		public String loginStatus;

		public SyncUpLogin(Context context){
			this.context = context;

		}
		@Override
		public void processJSON(JSONObject json) {
			try {
				if (json.get("Result").equals("Fail")) {

					// if the login is not successful display a messages saying so
					new GobiToast(context, "Login is Unsuccessful");
					loginStatus = "Fail";
					loginButtonClicked = false;

					//TODO: if fail, try again to delete	
				} else if (json.get("Result").equals("Success")) {

					new GobiToast(context, "Successful Login");
					loginStatus = "Success";

					Editor editor = pref.edit();
					editor.putBoolean("loggedIn", true);
					editor.commit();

					Intent i = new Intent(context, TabhostLayoutActivity.class);
					startActivity(i);
					finish();

				}else if (json.get("Result").equals(null)) {

					new GobiToast(context, "result is null");
					loginStatus = "Fail";
					loginButtonClicked = false;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}



