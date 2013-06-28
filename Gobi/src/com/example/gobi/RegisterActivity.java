package com.example.gobi;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;


public class RegisterActivity extends Activity {


	private SyncUpToCloud sutc;
	private EditText userName;
	private EditText userEmail;
	private EditText userPassword;
	private EditText confirmUserPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);


		sutc = new SyncUpToCloud(getApplicationContext());

		//------------------------SET ON TOUCH LISTENERS--------------------------------
		userName = (EditText) findViewById(R.id.userName);
		userName.setOnTouchListener(new View.OnTouchListener() {
			int count = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				count++;
				if(count ==1){
					userName.getText().clear();
				}
				return false;
			}
		});
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

		confirmUserPassword = (EditText) findViewById(R.id.confirmUserPassword);
		confirmUserPassword.setOnTouchListener(new View.OnTouchListener() {
			int count = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				count++;
				if(count ==1){
					confirmUserPassword.getText().clear();
				}
				return false;
			}
		});

		//-----------------------REGISTER BUTTON LISTENER------------------------------------
		TextView confirmRegister = (TextView) findViewById(R.id.register);
		confirmRegister.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {

				if(registerValidation(userName.getText().toString(), userEmail.getText().toString(), userPassword.getText().toString(), confirmUserPassword.getText().toString())){
					register(userName.getText().toString(), userEmail.getText().toString(), userPassword.getText().toString());
				}else{
					//Toast.makeText(getApplicationContext(), "Values are either incorrect or already exist", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	private boolean registerValidation(String userName, String userEmail, String userPassword, String confirmUserPassword){
		boolean registerIsValid = true;

		// --------------------------------VALIDATION--------------------------------------------
		if (isEmptyString(userName) || isEmptyString(userEmail) || isEmptyString(userPassword) || isEmptyString(confirmUserPassword)) {
			new GobiToast(this, "Please fill all fields to proceed");
			registerIsValid = false;
		}

		//CHECK EMAIL
		else if(!isValidEmail(userEmail)){
			registerIsValid = false;
			new GobiToast(this, "Please enter valid email");
		}
		//CHECK PASSWORD
		else if(!passwordValidation(userPassword)){
			registerIsValid = false;
			new GobiToast(this, "Password has to be at least 5 characters, " +
					"it has to contain digit and no whitespace is allowed");
		}
		//CHECK CONFIRM PASSWORD
		/*
		 * (?=.*[0-9]) a digit must occur at least once
			(?=.*[a-z]) a lower case letter must occur at least once
			(?=[\\S]+$) no whitespace allowed in the entire string
			.{5,10} at least 5 to 10 characters
		 */
		else if(!(userPassword.equals(confirmUserPassword))){
			registerIsValid = false;
			new GobiToast(this, "Passwords do not match");
		}

		return registerIsValid;
	}


	public static boolean isValidEmail(String userEmail) {
		boolean userEmailOK = true;
		try {
			Pattern patternEmail = Pattern.compile("[a-zA-Z]*[0-9]*@[a-zA-Z]*\\.[a-zA-Z]*");
			userEmailOK = userEmail.matches(patternEmail.toString());
		} catch (Exception ex) {
			userEmailOK = false;
		}	   
		return userEmailOK;
	}

	public static boolean passwordValidation(String userPassword) {
		boolean userPasswordOK = true;
		try{
			String passwordPattern = "((?=.*[0-9])(?=.*[a-z])(?=[\\S]+$).{5,10})";
			userPasswordOK = userPassword.matches(passwordPattern);
		} catch(Exception e){
			userPasswordOK = false;
		}
		return userPasswordOK;
	}

	private boolean isEmptyString(String userData) {
		if (userData.trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	//----------------------------ENCODE----------------------------------------------
	public void register(String userName, String userEmail, String userPassword){
		//create instanse of SyncUpTask
		SyncUpRegister sur = new SyncUpRegister(getApplicationContext());

		//create Http Post
		HttpPost httpPost = new HttpPost("http://vicidesign.net/gobi/registerAction.php");
		//create instanse of Name value pairs that would hold value that will be sent to cloud
		List<NameValuePair> nvp= new ArrayList<NameValuePair>();
		//iterate through arrayList and add values into nvp for each task record
		nvp.add(new BasicNameValuePair("USERNAME", userName));	
		nvp.add(new BasicNameValuePair("EMAIL", userEmail));
		nvp.add(new BasicNameValuePair("PASSWORD", userPassword));
		try {
			//encode nvp and send it with http post
			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e) {
			Log.e("TAG", "unsupported encoding error FROM REGISTER", e);
		}
		sur.setHttpPost(httpPost);
		sur.execute();
	}

	//----------------------------RECEIVE FROM CLOUD---------------------------------------------
	class SyncUpRegister extends JSONClassRegister {
		public boolean isRegisterSuccessfull = false;

		public SyncUpRegister(Context context){
			this.context = context;

		}

		@Override
		public void processJSON(JSONObject json) {
			try {
				if (json.get("Result").equals("Fail")) {

					// if the login is not successful display a messages saying so
					new GobiToast(context, "Register is Unsuccessful");
					isRegisterSuccessfull = false;
					//TODO: if fail, try again to delete	
				} else if (json.get("Result").equals("Success")) {
					new GobiToast(context, "Successful Register");
					isRegisterSuccessfull = true;
				}else if (json.get("Result").equals(null)) {
					new GobiToast(context, "result is null");
					isRegisterSuccessfull = false;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//-----------------------INTENTS---------------------------------------------------------
			if(isRegisterSuccessfull){
				Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(i);
			}else{
				new GobiToast(getApplicationContext(), "Email already exists!");
			}
		}
	}
}



