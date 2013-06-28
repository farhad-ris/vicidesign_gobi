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
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class SideBarMenuActivity extends Activity {

	ContentResolver resolver;
	protected Context context;

	public void setContext(Context context) {
		this.context = context;
	}

	public void setHttpPost(HttpPost httpPost) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.side_bar_menu);

		LinearLayout logOut = (LinearLayout) findViewById(R.id.logout_linear);
		logOut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder warning = new AlertDialog.Builder(SideBarMenuActivity.this);
				warning.setTitle("Confirmation");
				warning
				.setMessage("Are you sure that you want to log out?")
				.setCancelable(true)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int id) {
						//call a method in database handler
						DatabaseHandler.getInstance(getApplicationContext()).emptyAllTables();
						Intent i = new Intent(SideBarMenuActivity.this, LoginActivity.class);
						SharedPreferences pref = getApplicationContext().getSharedPreferences("login_status", MODE_PRIVATE);
						Editor editor = pref.edit();
						editor.putBoolean("loggedIn", false);
						editor.commit();
						startActivity(i);
					}
				})
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
				warning.show();
			}
		});
		LinearLayout sync = (LinearLayout) findViewById(R.id.sync_linear);
		sync.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				resolver = getContentResolver();
				Cursor c = resolver.query(GobiContentProvider.CONTENT_URI_USER, new String[]{DatabaseHandler.KEY_USEREMAIL}, null, null, null);
				c.moveToFirst();
				int userEmailIndex = c.getColumnIndex(DatabaseHandler.KEY_USEREMAIL);
				String userEmail = c.getString(userEmailIndex);

				Log.i("sidebar", "userEmail is "+userEmail);
				//				if(DatabaseHandler.getInstance(context).emptyForSync()){
				sync(userEmail);
				//				}else{
				//	Log.i("sidebar", "didn't delete");
				//}
			}
		});



	}

	public void sync(String userEmail){
		//create instanse of SyncUpTask
		SyncUpSync sus = new SyncUpSync(getApplicationContext());
		//create Http Post
		HttpPost httpPost = new HttpPost("http://vicidesign.net/gobi/syncAction.php");
		//create instanse of Name value pairs that would hold value that will be sent to cloud
		List<NameValuePair> nvp= new ArrayList<NameValuePair>();
		//iterate through arrayList and add values into nvp for each task record	
		nvp.add(new BasicNameValuePair("EMAIL", userEmail));
		try {
			//encode nvp and send it with http post
			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e){
			Log.e("sync", "unsupported encoding error FROM LOGIN", e);
		}
		sus.setHttpPost(httpPost);
		sus.execute();
	}

	//---------------------------------RECEIVE FROM CLOUD----------------------------------------------
	class SyncUpSync extends JSONClassSync {
		public String loginStatus;

		public SyncUpSync(Context context){
			this.context = context;

		}
		@Override
		public void processJSON(JSONObject json) {
			try {
				if (json.get("Result").equals("Fail")) {				
					// if the login is not successful display a messages saying so
					new GobiToast(context, "Sync is Unsuccessful");					
					//TODO: if fail, try again to delete	
				} else if (json.get("Result").equals("Success")) {			
					new GobiToast(context, "Successful Sync");	
				}else if (json.get("Result").equals(null)) {		
					new GobiToast(context, "result is null");					
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
