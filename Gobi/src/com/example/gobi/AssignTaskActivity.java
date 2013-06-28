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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AssignTaskActivity extends Activity {

	EditText assignEmail;
	public static final int ACTIVITY_ADD = 1;
	public static final int ACTIVITY_EDIT = 2;
	private ImageButton editDoneButton;
	ContentResolver resolver;
	private String userEmail;
	private String userIdOfLogin;
	private String userEmailLogin;
	private static final String TAG = AssignTaskActivity.class.getSimpleName();
	private Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// NEEDS TO BE SET BEFORE setContentView
		setContentView(R.layout.assign_task);
		resolver = getContentResolver();
		i = getIntent();


		TextView assignHeader = (TextView) findViewById(R.id.taskinfo_title);
		assignHeader.setText(R.string.assign_title_taskinfo);

		//LayoutInflater memberInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addListenerOnButton();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		userIdOfLogin = extras.getString("userIdOfLogin");

		Cursor cursor = resolver.query(GobiContentProvider.CONTENT_URI_USER, new String[]{DatabaseHandler.KEY_USEREMAIL},null, null, null);
		cursor.moveToFirst();
		int userEmailLoginIndex = cursor.getColumnIndex(DatabaseHandler.KEY_USEREMAIL);
		userEmailLogin = cursor.getString(userEmailLoginIndex);

		assignEmail = (EditText) findViewById(R.id.assignEmail);
		int requestCode = extras.getInt("requestCode");

		if(requestCode == ACTIVITY_ADD) {
			i.putExtra("isInsertAssignee", true);
			assignEmail.setOnTouchListener(new View.OnTouchListener() {
				//----------------------on touch listener-------------------------------------
				int count = 0;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					count++;
					if(count ==1){
						assignEmail.getText().clear();
					}
					return false;
				}
			});
		}
		else if(requestCode == ACTIVITY_EDIT) {
			int taskId = extras.getInt(DatabaseHandler.KEY_TASKID);
			Cursor c = resolver.query(GobiContentProvider.CONTENT_URI_TASKASSIGNEE, new String[]{DatabaseHandler.KEY_USEREMAIL}, 
					DatabaseHandler.KEY_TASKID + " = " + taskId, null, null);

			if(c.moveToFirst()==false){
				i.putExtra("isInsertAssignee", true);
				assignEmail.setOnTouchListener(new View.OnTouchListener() {
					//----------------------on touch listener-------------------------------------
					int count = 0;
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						count++;
						if(count ==1){
							assignEmail.getText().clear();
						}
						return false;
					}
				});
			}else{
				i.putExtra("isInsertAssignee", true);
				int userEmailIndex = c.getColumnIndex(DatabaseHandler.KEY_USEREMAIL);
				userEmail = c.getString(userEmailIndex);
				assignEmail.setText(userEmail);
			}
		}


	}    	
	//---------------------------------------------------ENCODE--------------------------------------------------------------
	public void encode(String assignEmail){
		//create instanse of SyncUpTask
		SyncUpAssign sua = new SyncUpAssign(getApplicationContext());

		//create Http Post
		HttpPost httpPost = new HttpPost("http://vicidesign.net/gobi/taskEmailVerification.php");
		//create instanse of Name value pairs that would hold value that will be sent to cloud
		List<NameValuePair> nvp= new ArrayList<NameValuePair>();
		//iterate through arrayList and add values into nvp for each task record	
		nvp.add(new BasicNameValuePair("USEREMAIL", assignEmail));

		try {
			//encode nvp and send it with http post
			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e){
			Log.e(TAG, "unsupported encoding error FROM ASSIGN", e);
		}
		sua.setHttpPost(httpPost);
		sua.execute();
	}

	//---------------------------------RECEIVE FROM CLOUD----------------------------------------------
	class SyncUpAssign extends JSONClassAssign {
		public boolean emailVerSuccess = false;

		public SyncUpAssign(Context context){
			this.context = context;

		}
		@Override
		public void processJSON(JSONObject json) {
			try {
				if (json.get("Result").equals("Fail")) {
					emailVerSuccess = false;

				} else if (json.get("Result").equals("Success")) {
					emailVerSuccess = true;
				}else if (json.get("Result").equals(null)) {
					emailVerSuccess = false;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			//-----------------------INTENTS---------------------------------------------------------

			if(emailVerSuccess){
				String assignEmailString = assignEmail.getText().toString();

				i = getIntent();
				i.putExtra("taskAssigneeSet", true);
				i.putExtra(DatabaseHandler.KEY_USEREMAIL, assignEmailString);
				setResult(ActivityCodeConstants.ASSIGN_TASK_ACTIVITY, i);
				finish();
			}else{
				new GobiToast(context, "User doesn't exist");
			}
		}

	}
	public void addListenerOnButton() {


		editDoneButton = (ImageButton) findViewById(R.id.editDoneButton);

		editDoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				String assignEmailString = assignEmail.getText().toString();
				if(assignEmailString.equals(userEmailLogin)){
					new GobiToast(getApplicationContext(), "can't assign a task to yourself!");
				}else{
					encode(assignEmailString);
				}
			}

		});



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.assign_task, menu);
		return true;
	}

}
