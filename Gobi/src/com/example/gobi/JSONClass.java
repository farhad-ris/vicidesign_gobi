package com.example.gobi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Timestamp;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

//JSON abstract class for going to the cloud to get JSON response
public abstract class JSONClass extends AsyncTask<String, Void, String>{

	private ProgressDialog progressDialog;
	protected Context context;
	private HttpPost httpPost;
	protected String taskName;
	private String ret = "";
	ContentResolver resolver;

	public void setContext(Context context) {
		this.context = context;
	}

	public void setHttpPost(HttpPost httpPost) {
		this.httpPost = httpPost;
	}

	@Override    
	protected void onPreExecute() 
	{       
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("... Loading ...");
		// progressDialog.show();
	}

	/**
	 * go to website and get the JSON
	 */
	@Override
	protected String doInBackground(String... arg) {
		String line = "";


		// Create http client
		HttpClient client = new DefaultHttpClient();

		try {
			// Get the response from the website
			HttpResponse response = client.execute(httpPost);

			// Check the status of the response
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) { //OK
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				while ((line = rd.readLine()) != null) {
					ret += line;
				}
			}
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * once it is finished dismiss the dialog and process the JSON
	 */
	@Override
	protected void onPostExecute(String result) {
		JSONObject json;
		boolean hasAssignee = false;
		resolver = context.getContentResolver();
		try {
			// turn the result into a JSONObject
			new GobiToast(context, "SYNCING DOWN");
			json = new JSONObject(result);
			ContentValues values = new ContentValues();
			ContentValues valuesForAssign = new ContentValues();
			//process result
			JSONArray names = json.names();
			int i;
			for (i = 0; i < names.length(); i++) {
				if (!names.getString(i).equals("Result")) {
					//parse "result" into a json object
					JSONObject task = new JSONObject(result);
					String hasAssigneeString = task.getString("HASASSIGNEE");
					hasAssignee = Boolean.valueOf(hasAssigneeString);

					int taskID = task.getInt("TASKID");
					int _id = 0;
					String taskName = task.getString("TASKNAME");
					int workspaceID = task.getInt("WORKSPACEID");
					//					if(task.getInt("PRIORITY")==0) {
					//						boolean priority = false;
					//					}
					boolean priority = (task.getInt("PRIORITY") ==1);
					int userID = task.getInt("USERID");
					Date dueDate = Date.valueOf(task.getString("DUEDATE"));//??????
					boolean timeFlag = (task.getInt("TIMEFLAG") == 1);
					boolean status = (task.getInt("STATUS") ==1);			
					String tag = task.getString("TAG");
					String geolocation = task.getString("GEOLOCATION");
					int projectID = task.getInt("PROJECTID");
					Timestamp lastUpdate = Timestamp.valueOf(task.getString("LASTUPDATE"));
					String taskNote = task.getString("TASKNOTE");
					String dueTime = task.getString("DUETIME");				

					//Create new task

					values.put(DatabaseHandler.KEY_TASKID, taskID);
					values.put(DatabaseHandler.KEY_COLUMNID, _id);
					values.put(DatabaseHandler.KEY_USERID, userID);
					values.put(DatabaseHandler.KEY_DUEDATE, dueDate.toString());
					values.put(DatabaseHandler.KEY_DUETIME, dueTime);
					values.put(DatabaseHandler.KEY_TIMEFLAG, timeFlag);
					values.put(DatabaseHandler.KEY_STATUS, status);
					values.put(DatabaseHandler.KEY_GEOLOCATION, geolocation);
					values.put(DatabaseHandler.KEY_TAG, tag);
					values.put(DatabaseHandler.KEY_PROJECTID, projectID);
					values.put(DatabaseHandler.KEY_LASTUPDATED, lastUpdate.toString());
					values.put(DatabaseHandler.KEY_NOTE, taskNote);
					values.put(DatabaseHandler.KEY_PRIORITY, priority);
					values.put(DatabaseHandler.KEY_WORKSPACEID, workspaceID);
					values.put(DatabaseHandler.KEY_TASKNAME, taskName);

					//--------------TASK ASSIGNEE--------------------------------
					if(hasAssignee){
						String userEmail = task.getString("USEREMAIL");
						int assigneeId = task.getInt("ASSIGNEEID");
						Timestamp lastUpdateAssignee = Timestamp.valueOf(task.getString("LASTUPDATEASSIGNEE"));
						valuesForAssign.put(DatabaseHandler.KEY_LASTUPDATED, lastUpdateAssignee.toString());
						valuesForAssign.put(DatabaseHandler.KEY_CREATORID, userID);
						valuesForAssign.put(DatabaseHandler.KEY_USEREMAIL, userEmail);
						valuesForAssign.put(DatabaseHandler.KEY_ASSIGNEEID, assigneeId);
						valuesForAssign.put(DatabaseHandler.KEY_TASKID, taskID);
					}

				}


			}
			resolver.insert(GobiContentProvider.CONTENT_URI_TASK, values);
			//insert into Task Assignee table
			if(hasAssignee){
				resolver.insert(GobiContentProvider.CONTENT_URI_TASKASSIGNEE, valuesForAssign);
				resolver.delete(GobiContentProvider.CONTENT_URI_SCRATCH_TASKASSIGNEE, DatabaseHandler.KEY_LASTUPDATED+"= (SELECT MAX(" +DatabaseHandler.KEY_LASTUPDATED+") FROM "+DatabaseHandler.TABLE_SCRATCH_TASKASSIGNEE+")", null);
			}
			resolver.delete(GobiContentProvider.CONTENT_URI_SCRATCH_TASK, DatabaseHandler.KEY_LASTUPDATED+"= (SELECT MAX(" +DatabaseHandler.KEY_LASTUPDATED+") FROM "+DatabaseHandler.TABLE_SCRATCH_TASK+")", null);


			processJSON(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		progressDialog.dismiss();
	}

	public abstract void processJSON(JSONObject json);
}
