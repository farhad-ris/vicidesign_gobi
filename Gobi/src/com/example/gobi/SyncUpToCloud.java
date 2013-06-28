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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class SyncUpToCloud<SyncUpLogin>{
	private ContentResolver resolver;
	private Context context;
	ArrayList<Task> list;
	public static final String TAG = SyncUpToCloud.class.getSimpleName();
	//   private SyncUpLogin sul;
	// private String loginStatus;
	LoginActivity la;

	public SyncUpToCloud(Context context){
		this.context = context;
		resolver = context.getContentResolver();
		//	sul = this.sul;
		//this.loginStatus = loginStatus;

	}


	//TODO: Task is currently hardcoded
	public ArrayList<Task> getScratchData(){
		//get data from scratch table
		Cursor cursor = resolver.query(GobiContentProvider.CONTENT_URI_SCRATCH_TASK, null, null, null, null);
		list = cursorToList(cursor);
		return list;
	}


	private ArrayList<Task> cursorToList(Cursor cursor) {
		if(cursor != null && cursor.moveToFirst()) {
			list = new ArrayList<Task>();
			do {
				list.add(new Task(cursor));
			} while(cursor.moveToNext());
			return list;
		} else {
			return null;
		}
	}


	public void encodeTask(boolean taskAssigneeSet){
		//create instanse of SyncUpTask
		SyncUpTask sut = new SyncUpTask();
		sut.setContext(context);

		//create Http Post
		HttpPost httpPost = new HttpPost("http://vicidesign.net/gobi/taskPrepareAll.php");
		//get data from scratch table
		ArrayList<Task> listToEncode = this.getScratchData();
		//create instanse of Name value pairs that would hold value that will be sent to cloud
		List<NameValuePair> nvp= new ArrayList<NameValuePair>();
		//iterate through arrayList and add values into nvp for each task record
		for(Task task: listToEncode){	
			nvp.add(new BasicNameValuePair("TASKNAME", task.getTaskName()+ ""));
			nvp.add(new BasicNameValuePair("WORKSPACEID", task.getWorkspaceID()+ ""));
			//nvp.add(new BasicNameValuePair("PRIORITY", task.isPriority()+ ""));
			if (task.isPriority()) {
				nvp.add(new BasicNameValuePair("PRIORITY", "1"));
			} else {
				nvp.add(new BasicNameValuePair("PRIORITY", "0"));
			}
			nvp.add(new BasicNameValuePair("USERID",  task.getUserID()+ ""));
			nvp.add(new BasicNameValuePair("DUEDATE", task.getDueDate()+ ""));
			nvp.add(new BasicNameValuePair("DUETIME", task.getDueTime()+ ""));
			nvp.add(new BasicNameValuePair("TIMEFLAG", task.isTimeFlag()+ ""));
			nvp.add(new BasicNameValuePair("STATUS", task.isStatus()+ ""));
			nvp.add(new BasicNameValuePair("GEOLOCATION", task.getGeolocation()+ ""));
			nvp.add(new BasicNameValuePair("TAG", task.getTag()+ ""));
			nvp.add(new BasicNameValuePair("PROJECTID", task.getProjectID()+ ""));
			nvp.add(new BasicNameValuePair("TASKNOTE", task.getTaskNote()+ ""));
			nvp.add(new BasicNameValuePair("LASTUPDATED", task.getLastUpdated()+ ""));
			//nvp.add(new BasicNameValuePair("PRIORITY", "" + (task.isPriority()? 1 : 0)));

		}

		//------------------------WITH ASSIGNEE---------------------------------
		if(taskAssigneeSet){
			Cursor c = resolver.query(GobiContentProvider.CONTENT_URI_SCRATCH_TASKASSIGNEE, null, null, null, null);
			c.moveToFirst();
			int taskAssigneeIndex = c.getColumnIndex(DatabaseHandler.KEY_USEREMAIL);
			String assigneeEmail = c.getString(taskAssigneeIndex);
			nvp.add(new BasicNameValuePair("USEREMAIL", assigneeEmail));
		}
		nvp.add(new BasicNameValuePair("HASASSIGNEE", taskAssigneeSet + ""));

		//-----------------------------ENCODING----------------------------------
		try {
			//encode nvp and send it with http post
			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "unsupported encoding error", e);
		}
		sut.setHttpPost(httpPost);
		sut.execute();
	}


	public void encodeTaskEdit(boolean taskAssigneeSet, boolean isInsertedAssignee){
		//create instanse of SyncUpTask
		SyncUpTaskEdit sute = new SyncUpTaskEdit();
		sute.setContext(context);
		//create Http Post
		//		if(isInsertedAssignee){
		//		}
		HttpPost httpPost = new HttpPost("http://vicidesign.net/gobi/taskEditAction.php");
		//get data from scratch table
		ArrayList<Task> listToEncode = this.getScratchData();
		//create instanse of Name value pairs that would hold value that will be sent to cloud
		List<NameValuePair> nvp= new ArrayList<NameValuePair>();
		//iterate through arrayList and add values into nvp for each task record
		for(Task task: listToEncode){
			nvp.add(new BasicNameValuePair("TASKID", task.getTaskID()+ ""));
			nvp.add(new BasicNameValuePair("TASKNAME", task.getTaskName()+ ""));
			nvp.add(new BasicNameValuePair("WORKSPACEID", task.getWorkspaceID()+ ""));
			if (task.isPriority()) {
				nvp.add(new BasicNameValuePair("PRIORITY", "1"));
			} else {
				nvp.add(new BasicNameValuePair("PRIORITY", "0"));
			}
			nvp.add(new BasicNameValuePair("USERID",  task.getUserID()+ ""));
			nvp.add(new BasicNameValuePair("DUEDATE", task.getDueDate()+ ""));
			nvp.add(new BasicNameValuePair("DUETIME", task.getDueTime()+ ""));
			nvp.add(new BasicNameValuePair("TIMEFLAG", task.isTimeFlag()+ ""));
			nvp.add(new BasicNameValuePair("STATUS", task.isStatus()+ ""));
			nvp.add(new BasicNameValuePair("GEOLOCATION", task.getGeolocation()+ ""));
			nvp.add(new BasicNameValuePair("TAG", task.getTag()+ ""));
			nvp.add(new BasicNameValuePair("PROJECTID", task.getProjectID()+ ""));
			nvp.add(new BasicNameValuePair("TASKNOTE", task.getTaskNote()+ ""));
			nvp.add(new BasicNameValuePair("LASTUPDATED", task.getLastUpdated()+ ""));
			//SEND USER EMAIL FOR ASSIGNEE
		}

		//------------------------WITH ASSIGNEE---------------------------------
		if(taskAssigneeSet){
			Cursor c = resolver.query(GobiContentProvider.CONTENT_URI_SCRATCH_TASKASSIGNEE, null, null, null, null);
			c.moveToFirst();
			int taskAssigneeIndex = c.getColumnIndex(DatabaseHandler.KEY_USEREMAIL);
			String assigneeEmail = c.getString(taskAssigneeIndex);

			//int lastUpdateIndex = c.getColumnIndex(DatabaseHandler.KEY_LASTUPDATED);
			//Timestamp lastUpdated = Timestamp.valueOf(c.getString(lastUpdateIndex));
			//nvp.add(new BasicNameValuePair("", value))
			nvp.add(new BasicNameValuePair("USEREMAIL", assigneeEmail));
			nvp.add(new BasicNameValuePair("ISINSERTED", isInsertedAssignee + ""));
		}
		nvp.add(new BasicNameValuePair("HASASSIGNEE", taskAssigneeSet + ""));

		try {
			//encode nvp and send it with http post
			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "unsupported encoding error", e);
		}
		sute.setHttpPost(httpPost);
		sute.execute();	
	}

	public void deleteTask(int taskID){
		//create instanse of SyncUpTask
		SyncUpTaskDelete sute = new SyncUpTaskDelete();
		sute.setContext(context);
		//create Http Post
		HttpPost httpPost = new HttpPost("http://vicidesign.net/gobi/taskDelete.php");
		//create instanse of Name value pairs that would hold value that will be sent to cloud
		List<NameValuePair> nvp= new ArrayList<NameValuePair>();
		//iterate through arrayList and add values into nvp for each task record
		nvp.add(new BasicNameValuePair("TASKID", taskID+""));		
		try {
			//encode nvp and send it with http post
			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "unsupported encoding error FROM DELETE", e);
		}
		sute.setHttpPost(httpPost);
		sute.execute();
	}

	public void completeTask(int taskID){
		//create instanse of SyncUpTask
		SyncUpTaskDelete sute = new SyncUpTaskDelete();
		sute.setContext(context);
		//create Http Post
		HttpPost httpPost = new HttpPost("http://vicidesign.net/gobi/taskComplete.php");
		//create instanse of Name value pairs that would hold value that will be sent to cloud
		List<NameValuePair> nvp= new ArrayList<NameValuePair>();
		//iterate through arrayList and add values into nvp for each task record
		nvp.add(new BasicNameValuePair("TASKID", taskID+""));		
		try {
			//encode nvp and send it with http post
			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "unsupported encoding error FROM COMPLETE", e);
		}
		sute.setHttpPost(httpPost);
		sute.execute();
	}



	class SyncUpTask extends JSONClass {

		@Override
		public void processJSON(JSONObject json) {
			try {
				if (json.get("Result").equals("Fail")) {

					// if the login is not successful display a messages saying so
					new GobiToast(context, "RECEIVING TASK DIDN'T WORK");

				} else if (json.get("Result").equals("Success")) {
					//TODO: insert task to sqlite
					//new GobiToast(context, "RECEIVING TASK WORKED");
				}else if (json.get("Result").equals(null)) {
					//TODO: insert task to sqlite
					new GobiToast(context, "result is null");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

	class SyncUpTaskEdit extends JSONClassEdit {


		@Override
		public void processJSON(JSONObject json) {
			try {
				if (json.get("Result").equals("Fail")) {

					// if the login is not successful display a messages saying so
					new GobiToast(context, "RECEIVING TASK DIDN'T WORK");

				} else if (json.get("Result").equals("Success")) {
					//TODO: insert task to sqlite
					//new GobiToast(context, "RECEIVING TASK WORKED EDIT");
				}else if (json.get("Result").equals(null)) {
					//TODO: insert task to sqlite
					new GobiToast(context, "result is null");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class SyncUpTaskDelete extends JSONClassDelete {

		@Override
		public void processJSON(JSONObject json) {
			try {
				if (json.get("Result").equals("Fail")) {

					// if the login is not successful display a messages saying so
					new GobiToast(context, "Deleting TASK DIDN'T WORK");
					//TODO: if fail, try again to delete	
				} else if (json.get("Result").equals("Success")) {

					//new GobiToast(context, "RECEIVING TASK WORKED DELETE");
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








