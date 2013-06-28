package com.example.gobi;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditTaskActivity extends Activity {

	ContentResolver resolver;
	boolean isPressed;
	String taskName;
	String taskNote;
	int priority;
	int columnId;
	int taskId;
	public static final String TAG = EditTaskActivity.class.getSimpleName();
	private static final int ACTIVITY_EDIT = 2;
	public static final int ACTIVITY_DATE = 0;

	public static final int ACTIVITY_PROJECT = 600;
	public static final int ACTIVITY_ASSIGN = 3;

	private boolean dueDateSet;
	private boolean alarmSet;
	private String dueDate;
	private String dueTime;


	private int dueYear;
	private int dueMonth;
	private int dueDay;
	private int dueHour;
	private int dueMinute;
	private int alarmDelay;

	private int requestCode;
	private int projectID;
	private int workspaceID;
	private boolean projectChangedInOrganise = false;
	private boolean workspaceChangedInOrganise = false;

	private String assignEmail;
	private boolean taskAssigneeSet;
	private boolean isInsertedAssignee;

	private SyncUpToCloud sutc;
	private String assigneeId;
	private String userIdOfLogin;
	private boolean hasAssignee = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // to get rid of title bar
		// NEEDS TO BE SET BEFORE setContentView
		setContentView(R.layout.edit_task);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			requestCode = extras.getInt("requestCode");
			if (requestCode == ACTIVITY_PROJECT) {
				projectID = extras.getInt(DatabaseHandler.KEY_PROJECTID);
			}
		}

		sutc = new SyncUpToCloud(getApplicationContext());

		resolver = getContentResolver();
		taskId = extras.getInt(DatabaseHandler.KEY_TASKID);
		workspaceID = extras.getInt(DatabaseHandler.KEY_WORKSPACEID);
		projectID = extras.getInt(DatabaseHandler.KEY_PROJECTID);
		taskName = extras.getString(DatabaseHandler.KEY_TASKNAME);
		taskNote = extras.getString(DatabaseHandler.KEY_NOTE);
		priority = extras.getInt(DatabaseHandler.KEY_PRIORITY);
		columnId = extras.getInt(DatabaseHandler.KEY_COLUMNID);
		dueDate = extras.getString(DatabaseHandler.KEY_DUEDATE);
		dueTime = extras.getString(DatabaseHandler.KEY_DUETIME);
		assignEmail = extras.getString(DatabaseHandler.KEY_USEREMAIL);


		//1. MAKE A QUERY -> GET ASSIGNEE ID WHERE TASKID = CURRENT TASK ID
		Cursor assigneeIdCursor = resolver.query(GobiContentProvider.CONTENT_URI_TASKASSIGNEE, new String[]{DatabaseHandler.KEY_ASSIGNEEID}, DatabaseHandler.KEY_TASKID+" = "+taskId, null, null);
		boolean moveToFirst = assigneeIdCursor.moveToFirst();
		if(moveToFirst == true) {
			int assigneeIdIndex = assigneeIdCursor.getColumnIndex(DatabaseHandler.KEY_ASSIGNEEID);
			assigneeId = assigneeIdCursor.getString(assigneeIdIndex);
			hasAssignee = true;
		}


		//2. IF(ASSIGNEEID = USERID)--> THEN BLOCK THE ICON
		Cursor c = resolver.query(GobiContentProvider.CONTENT_URI_USER, new String[]{DatabaseHandler.KEY_USERID}, null, null, null);
		c.moveToFirst();
		int userIdLoginIndex = c.getColumnIndex(DatabaseHandler.KEY_USERID);
		userIdOfLogin = c.getString(userIdLoginIndex);

		//3. ELSE --> BE HAPPY

		// ###### CREATE TASK PROJECT CHECKLIST & PRIORITY BUTTONS #####

		Button createTaskButton = (Button) findViewById(R.id.create_todo);
		final Drawable createTaskIconActive = getResources().getDrawable(R.drawable.ic_create_todo_active);

		createTaskButton.setCompoundDrawablesWithIntrinsicBounds(null, createTaskIconActive, null, null);
		createTaskButton.setTextColor(Color.parseColor("#21c4f9"));

		Button createChecklist = (Button) findViewById(R.id.create_checklist);
		createChecklist.setVisibility(View.INVISIBLE);

		Button createProject = (Button) findViewById(R.id.create_project);
		createProject.setVisibility(View.INVISIBLE);

		final Drawable assignIconFalse = getResources().getDrawable(R.drawable.ic_tab_assign_light);



		//				Button createTaskButton = (Button) findViewById(R.id.create_todo);
		//				createTaskButton.setOnClickListener( new OnClickListener() {
		//					
		//					@Override
		//					public void onClick(View v) {
		//						Button createTaskButton = (Button) findViewById(R.id.create_todo);
		//						final Drawable createTaskIconActive = getResources().getDrawable(R.drawable.ic_create_todo_active);
		//						final Drawable createTaskIconNormal = getResources().getDrawable(R.drawable.ic_create_todo);
		//						
		//						createTaskButton.setCompoundDrawablesWithIntrinsicBounds(null, createTaskIconActive, null, null);
		//						createTaskButton.setTextColor(Color.parseColor("#21c4f9"));
		//						
		//					}
		//				});

		//				Button createProject = (Button) findViewById(R.id.create_project);
		//				createProject.setOnClickListener( new OnClickListener() {
		//					
		//					@Override
		//					public void onClick(View v) {
		//						Button createProject = (Button) findViewById(R.id.create_project);
		//						final Drawable createProjectIconActive = getResources().getDrawable(R.drawable.ic_create_project_active);
		//						
		//						createProject.setCompoundDrawablesWithIntrinsicBounds(null, createProjectIconActive, null, null);
		//						createProject.setTextColor(Color.parseColor("#21c4f9"));
		//					}
		//				});

		//				Button createChecklist = (Button) findViewById(R.id.create_checklist);
		//				createChecklist.setOnClickListener( new OnClickListener() {
		//					
		//					@Override
		//					public void onClick(View v) {
		//						Button createChecklist = (Button) findViewById(R.id.create_checklist);
		//						final Drawable createChecklistIconActive = getResources().getDrawable(R.drawable.ic_create_checklist_active);
		//						
		//						createChecklist.setCompoundDrawablesWithIntrinsicBounds(null, createChecklistIconActive, null, null);
		//						createChecklist.setTextColor(Color.parseColor("#21c4f9"));
		//					}
		//				});

		Button priorityButton = (Button) findViewById(R.id.priority);
		priorityButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(priority == 0) {
					Button priorityButton = (Button) findViewById(R.id.priority);
					final Drawable drawableTopRedTodo = getResources().getDrawable(R.drawable.ic_action_important_active);

					priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, drawableTopRedTodo, null, null);
					priorityButton.setTextColor(Color.RED);
					priority = 1;

				}				

				else if(priority == 1) {
					Button priorityButton = (Button) findViewById(R.id.priority);
					final Drawable drawableTopBlueTodo = getResources().getDrawable(R.drawable.ic_action_important);
					priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, drawableTopBlueTodo, null, null);
					priorityButton.setTextColor(Color.BLACK);
					priority = 0;
				}


			}
		});


		// #################### TASK INFO BUTTONS ######################

		Button organiseButton = (Button) findViewById(R.id.task_organise);
		organiseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new GobiToast(getApplicationContext(), "Not implemented for Todos in this version.");
			}
		});

		Button duedateButton = (Button) findViewById(R.id.task_due_date);
		duedateButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Button duedateButton = (Button) findViewById(R.id.task_due_date);
				//duedateButton.setTextColor(Color.RED);

				//Redirect to Activity
				Intent i = new Intent(EditTaskActivity.this, DueDateActivity.class);
				Bundle extras = new Bundle();
				extras.putInt("requestCode", ACTIVITY_EDIT);
				extras.putString(DatabaseHandler.KEY_DUEDATE, dueDate);
				extras.putString(DatabaseHandler.KEY_DUETIME, dueTime);
				i.putExtras(extras);
				startActivityForResult(i, ACTIVITY_DATE);

			}
		});
		if(hasAssignee == true){
			if(!(assigneeId.equals(userIdOfLogin))){
				Button assignButton = (Button) findViewById(R.id.task_assign);
				assignButton.setOnClickListener( new OnClickListener() {

					@Override
					public void onClick(View v) {
						findViewById(R.id.task_assign);
						//assignButton.setTextColor(Color.RED);
						//userId.getBytes();
						Intent i = new Intent(EditTaskActivity.this, AssignTaskActivity.class);
						Bundle extras = new Bundle();
						extras.putInt("requestCode", ACTIVITY_EDIT);
						extras.putInt(DatabaseHandler.KEY_TASKID, taskId);
						extras.putString("userIdOfLogin", userIdOfLogin);
						//FINISH -----> extras.
						i.putExtras(extras);
						startActivityForResult(i, ACTIVITY_ASSIGN);
						//startActivity(i);
						//finish();

					}
				});

			}else{
				Button assignButton = (Button) findViewById(R.id.task_assign);
				assignButton.setCompoundDrawablesWithIntrinsicBounds(null, assignIconFalse, null, null);
			}
		}else{
			Button assignButton = (Button) findViewById(R.id.task_assign);
			assignButton.setOnClickListener( new OnClickListener() {

				@Override
				public void onClick(View v) {
					findViewById(R.id.task_assign);
					//assignButton.setTextColor(Color.RED);
					//userId.getBytes();
					Intent i = new Intent(EditTaskActivity.this, AssignTaskActivity.class);
					Bundle extras = new Bundle();
					extras.putInt("requestCode", ACTIVITY_EDIT);
					extras.putInt(DatabaseHandler.KEY_TASKID, taskId);
					extras.putString("userIdOfLogin", userIdOfLogin);
					//FINISH -----> extras.
					i.putExtras(extras);
					startActivityForResult(i, ACTIVITY_ASSIGN);
					//startActivity(i);
					//finish();

				}
			});
		}


		Button noteButton = (Button) findViewById(R.id.task_note);
		noteButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Button noteButton = (Button) findViewById(R.id.task_note);
				//noteButton.setTextColor(Color.RED);

				Intent i = new Intent(EditTaskActivity.this, NotesActivity.class);
				i.putExtra(DatabaseHandler.KEY_NOTE, taskNote);
				startActivityForResult(i, ActivityCodeConstants.NOTES_ACTIVITY);
				//finish();

			}
		});

		EditText editTaskName = (EditText) findViewById(R.id.editTaskText);
		editTaskName.setText(taskName);

		//				EditText editTaskNote = (EditText) findViewById(R.id.edit_task_notes);
		//				editTaskNote.setText(taskNote);

		priorityButton = (Button) findViewById(R.id.priority);
		if(priority == 1){
			priorityButton.setTextColor(Color.RED);
			final Drawable drawableTopRedTodo = getResources().getDrawable(R.drawable.ic_action_important_active);

			priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, drawableTopRedTodo, null, null);
		}else{
			priorityButton.setTextColor(Color.BLACK);
			final Drawable drawableTopBlackTodo = getResources().getDrawable(R.drawable.ic_action_important);

			priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, drawableTopBlackTodo, null, null);
		}


		ImageButton confirmEditTaskButton = (ImageButton) findViewById(R.id.confirmEditTaskButton);
		confirmEditTaskButton.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = ((EditText) findViewById(R.id.editTaskText)).getText().toString();
				if (name.isEmpty()) {
					new GobiToast(getApplicationContext(), "Please insert a name.");
				} else {
					ContentValues values = new ContentValues();
					ContentValues valuesForAssign = new ContentValues();
					//values.put(DatabaseHandler.KEY_COLUMNID, 
					values.put(DatabaseHandler.KEY_TASKID, taskId);
					values.put(DatabaseHandler.KEY_TASKNAME, name);
					values.put(DatabaseHandler.KEY_PRIORITY, priority);
					values.put(DatabaseHandler.KEY_WORKSPACEID, workspaceID);
					values.put(DatabaseHandler.KEY_PROJECTID, projectID);
					//values.put(DatabaseHandler.KEY_USERID, 
					if (dueDateSet) {
						values.put(DatabaseHandler.KEY_DUEDATE, dueDate);
						values.put(DatabaseHandler.KEY_DUETIME, dueTime);
					}
					//						if (dueDate != "" && dueDate != null) {
					//							values.put(DatabaseHandler.KEY_DUETIME, dueTime);
					//						}
					//values.put(DatabaseHandler.KEY_TIMEFLAG, 
					//values.put(DatabaseHandler.KEY_STATUS, 
					//values.put(DatabaseHandler.KEY_GEOLOCATION, 
					//values.put(DatabaseHandler.KEY_TAG, 
					if(workspaceChangedInOrganise) {
						values.put(DatabaseHandler.KEY_PROJECTID, 0);
						values.put(DatabaseHandler.KEY_WORKSPACEID,workspaceID);
					} else if (projectChangedInOrganise) {
						values.put(DatabaseHandler.KEY_PROJECTID, projectID);
					}
					//values.put(DatabaseHandler.KEY_NOTE, ((EditText) findViewById(R.id.edit_task_notes)).getText().toString());
					values.put(DatabaseHandler.KEY_NOTE, taskNote);
					//values.put(DatabaseHandler.KEY_LASTUPDATED, 
					if (alarmSet) {
						values.put(DatabaseHandler.KEY_ALARM, alarmDelay);
						int id = 100; //TODO
						Alarm.cancelAlarm(getApplicationContext(), id); //TODO

						Calendar c = Calendar.getInstance();
						c.set(dueYear, dueMonth, dueDay, dueHour, dueMinute);
						c.add(Calendar.MINUTE, alarmDelay);

						Alarm.setAlarm(getApplicationContext(), id, 
								taskName + " is due in " + alarmDelay + " minutes!",
								c.getTimeInMillis()); //TODO
					}


					//	Uri taskSelectedUri = ContentUris.withAppendedId(GobiContentProvider.CONTENT_URI_SCRATCH_TASK,columnId);

					//	resolver.update(taskSelectedUri, values, null, null);
					Cursor c = resolver.query(GobiContentProvider.CONTENT_URI_TASK, new String[]{DatabaseHandler.KEY_USERID}, DatabaseHandler.KEY_TASKID+" = "+taskId, null, null);
					c.moveToFirst();
					int userIdIndex = c.getColumnIndex(DatabaseHandler.KEY_USERID);
					String userId = c.getString(userIdIndex);
					//Integer.valueOf(userId);
					values.put(DatabaseHandler.KEY_USERID, Integer.valueOf(userId));
					//--------------------------------------------------------------------------------------------
					if(taskAssigneeSet){
						valuesForAssign.put(DatabaseHandler.KEY_USEREMAIL, assignEmail);
						valuesForAssign.put(DatabaseHandler.KEY_CREATORID, userId);	
						resolver.insert(GobiContentProvider.CONTENT_URI_SCRATCH_TASKASSIGNEE, valuesForAssign);
					}
					resolver.insert(GobiContentProvider.CONTENT_URI_SCRATCH_TASK, values);
					sutc.encodeTaskEdit(taskAssigneeSet, isInsertedAssignee);
					finish();
				}
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_task, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		if (data != null) {

			Bundle extras = data.getExtras();

			if(resultCode == ACTIVITY_DATE) {
				dueDateSet = extras.getBoolean("dueDateSet");
				if (dueDateSet) {
					dueYear = extras.getInt("dueYear");
					dueMonth = extras.getInt("dueMonth");
					dueDay = extras.getInt("dueDay");
					dueHour = extras.getInt("dueHour");
					dueMinute = extras.getInt("dueMinute");
					dueDate = dueYear + "-" + String.format("%02d", dueMonth) + "-" + String.format("%02d", dueDay);
					dueTime = String.format("%02d", dueHour) + ":" + String.format("%02d", dueMinute);
				}

				alarmSet = extras.getBoolean("alarmSet");
				if (alarmSet) {
					alarmDelay = extras.getInt(DatabaseHandler.KEY_ALARM);
				}


			}
			else if(resultCode == ActivityCodeConstants.NOTES_ACTIVITY){
				taskNote = extras.getString(DatabaseHandler.KEY_NOTE);

			} else if(resultCode == ActivityCodeConstants.ORGANISE_ACTIVITY) {
				projectID = extras.getInt(ScratchProjectColumns.PROJECT_ID);
				projectChangedInOrganise = extras.getBoolean("projectChanged");
				workspaceID = extras.getInt(DatabaseHandler.KEY_WORKSPACEID);
				workspaceChangedInOrganise = extras.getBoolean("workspaceChanged");
			}

			else if(resultCode == ActivityCodeConstants.ASSIGN_TASK_ACTIVITY){
				taskAssigneeSet = extras.getBoolean("taskAssigneeSet");
				isInsertedAssignee = extras.getBoolean("isInsertAssignee");
				if(taskAssigneeSet){
					assignEmail = extras.getString(DatabaseHandler.KEY_USEREMAIL);
				}
			}

		}

	}
}


