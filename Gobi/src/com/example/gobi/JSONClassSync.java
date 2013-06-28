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
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public abstract class JSONClassSync extends AsyncTask<String, Void, String>{

	private ProgressDialog progressDialog;
	protected Context context;
	private HttpPost httpPost;
	protected String taskName;
	private String ret = "";
	ContentResolver resolver;
	public static final String TAG = JSONClassLogin.class.getSimpleName();

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
		resolver = context.getContentResolver();
		try {
			json = new JSONObject(result);
			//-----------------------------INSERT INTO TASK TABLE----------------------------------------
			int lastIndex = result.length()-1;
			String sub = result;
			new ContentValues();
			int j;
			String resultOfResult = "fail";
			JSONObject res = new JSONObject(result);
			JSONArray namesForResult = res.names();
			for (j = 0; j < namesForResult.length(); j++) {
				if (namesForResult.getString(j).equals("Result")) {
					resultOfResult =res.getString("Result");
				}
			}
			Log.i(TAG, resultOfResult);
			if(resultOfResult.equals("Success")){
				if(DatabaseHandler.getInstance(context).emptyForSync()){
					if(!(sub.length()<=28)){
						if(sub.contains("NEWUSER")){
							new GobiToast(context, "You have no new tasks");
						}else{
							sub = result.substring(28, lastIndex);//get rid of {"Result":"Success","tasks":

							json = new JSONObject(result);
							ContentValues values = new ContentValues();
							ContentValues valuesForTA = new ContentValues();
							//process result


							JSONArray tasks = new JSONArray(sub);
							int i1;
							//create  a different for loop
							for (i1 = 0; i1 < tasks.length(); i1++) {
								String taskObject = tasks.get(i1).toString();
								String subsub = taskObject.substring(1, taskObject.length()-1);
								JSONObject task = new JSONObject(subsub);
								int taskID = task.getInt("TASKID");
								//int _id = 0;
								String taskName = task.getString("TASKNAME");
								int workspaceID = task.getInt("WORKSPACEID");
								boolean priority = (task.getInt("PRIORITY") ==1);
								int userID = task.getInt("USERID");
								//String userEmail = task.getString("USEREMAIL");
								Date dueDate = Date.valueOf(task.getString("DUEDATE"));//??????
								boolean timeFlag = (task.getInt("TIMEFLAG") == 1);
								boolean status = (task.getInt("STATUS") ==1);			
								String tag = task.getString("TAG");
								String geolocation = task.getString("GEOLOCATION");
								int projectID = task.getInt("PROJECTID");
								Timestamp lastUpdate = Timestamp.valueOf(task.getString("LASTUPDATE"));
								String taskNote = task.getString("TASKNOTE");
								String dueTime = task.getString("DUETIME");


								values.put(DatabaseHandler.KEY_TASKID, taskID);
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


								resolver.insert(GobiContentProvider.CONTENT_URI_TASK, values);

							}	
							int i;

							//JSONArray ta = new JSONArray(result);
							JSONObject r = new JSONObject(result);
							JSONArray names = r.names();
							for (i = 0; i < names.length(); i++) {
								if (names.getString(i).equals("taskassignee")) {

									JSONArray jsonArray = r.getJSONArray("taskassignee");
									int k;
									for (k = 0; k < jsonArray.length(); k++) {
										//JSONArray j = jsonArray.getJSONArray(k);
										String taskString = jsonArray.get(k).toString();
										String taskClean = taskString.substring(1, taskString.length()-1);
										JSONObject task = new JSONObject(taskClean);
										int assigneeID = task.getInt("ASSIGNEEID");
										String userEmail = task.getString("USEREMAIL");
										int creatorId = task.getInt("CREATORID");
										int taskId = task.getInt("TASKID");
										Timestamp lastUpdate = Timestamp.valueOf(task.getString("LASTUPDATE"));

										valuesForTA.put(DatabaseHandler.KEY_CREATORID, creatorId);
										valuesForTA.put(DatabaseHandler.KEY_USEREMAIL, userEmail);
										valuesForTA.put(DatabaseHandler.KEY_ASSIGNEEID, assigneeID);
										valuesForTA.put(DatabaseHandler.KEY_TASKID, taskId);
										valuesForTA.put(DatabaseHandler.KEY_LASTUPDATED, lastUpdate.toString());
										Uri assign = resolver.insert(GobiContentProvider.CONTENT_URI_TASKASSIGNEE, valuesForTA);
										Log.i(TAG, "insertion of TA at "+assign);
									}
								}

							}
						}

					}new GobiToast(context, "Sync is successful!");
				}
			}else{
				new GobiToast(context, "Can't Sync, please check your internet connection");
			}
			//----------------------------------------------------------------------------------------	

			processJSON(json);


		} catch (JSONException e) {
			e.printStackTrace();
		}

		progressDialog.dismiss();
	}

	public abstract void processJSON(JSONObject json);








}
