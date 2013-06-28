package com.example.gobi;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddTaskActivity extends Activity {

	public static final int ACTIVITY_DATE = 0;
	public static final int ACTIVITY_ADD = 1;
	public static final int ACTIVITY_ASSIGN = 3;


	public static final int ACTIVITY_PROJECT = 600;

	private static final String TAG = AddTaskActivity.class.getSimpleName();
	private String taskName;
	private EditText addTaskName;
	private String taskNote = "";
	boolean isPriorityPressed;
	boolean isTodoButtonPressed = true;
	boolean isChecklistButtonPressed;
	boolean isProjectButtonPressed;

	Button todoButton;
	Button checklistButton;
	Button projectButton;

	Button priorityButton;

	public Drawable todoIconActive;
	public Drawable todoIconNormal;
	public Drawable checklistIconActive;
	public Drawable checklistIconNormal;
	public Drawable projectIconActive;
	public Drawable projectIconNormal;

	public Drawable priorityIconActive;
	public Drawable priorityIconNormal;

	ContentResolver resolver;
	String objectName = DatabaseHandler.KEY_TASKNAME;

	private boolean dueDateSet;
	private boolean alarmSet;
	private boolean taskAssigneeSet;
	private int dueYear;
	private int dueMonth;
	private int dueDay;
	private int dueHour;
	private int dueMinute;
	private int alarmDelay;
	private int workspaceId;

	private boolean workspaceSetInOrganise = false;
	private boolean projectSetInOrganise = false;

	private int requestCode;
	public static final int ACTIVITY_LOGIN = 500;
	private int projectID;
	private String assignEmail;

	private SyncUpToCloud sutc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_task);

		priorityIconNormal = getResources().getDrawable(R.drawable.ic_action_important);
		priorityIconActive = getResources().getDrawable(R.drawable.ic_action_important_active);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			requestCode = extras.getInt("requestCode");
			isTodoButtonPressed = extras.getBoolean("isTaskListTabSelected");
			isChecklistButtonPressed = extras.getBoolean("isCheckListTabSelected");
			if (requestCode == ACTIVITY_PROJECT) {
				projectID = extras.getInt(DatabaseHandler.KEY_PROJECTID);

			}
			if(requestCode == ActivityCodeConstants.TABHOST_LAYOUT_ACTIVITY) {//I'VE CHANGED THIS TO TABHOSTLAYOUT!
				workspaceId = extras.getInt(DatabaseHandler.KEY_WORKSPACEID);
				isProjectButtonPressed = extras.getBoolean("isProjectTabSelected");

			}
		}
		sutc = new SyncUpToCloud(getApplicationContext());

		todoIconActive = getResources().getDrawable(R.drawable.ic_create_todo_active);
		todoIconNormal = getResources().getDrawable(R.drawable.ic_create_todo);
		checklistIconActive = getResources().getDrawable(R.drawable.ic_create_checklist_active);
		checklistIconNormal = getResources().getDrawable(R.drawable.ic_create_checklist);
		if (requestCode != ACTIVITY_PROJECT) {
			projectIconActive = getResources().getDrawable(R.drawable.ic_create_project_active);
			projectIconNormal = getResources().getDrawable(R.drawable.ic_create_project);
		}


		resolver = getContentResolver();
		addTaskName = (EditText) findViewById(R.id.addTaskText);
		ImageButton addTaskButton = (ImageButton) findViewById(R.id.addTaskButton);
		addTaskName.setFocusable(true);
		addTaskName.requestFocus();

		Button organiseButton = (Button) findViewById(R.id.task_organise);
		organiseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isTodoButtonPressed) {
					new GobiToast(getApplicationContext(), "Not implemented for Todos in this version.");
				} else {
					Intent i = new Intent(AddTaskActivity.this, OrganiseActivity.class);
					if (isProjectButtonPressed) i.putExtra("requestCode", ActivityCodeConstants.EDIT_PROJECT_ACTIVITY);
					startActivityForResult(i, 0);
				}
			}
		});

		todoButton = (Button) findViewById(R.id.create_todo);
		checklistButton = (Button) findViewById(R.id.create_checklist);
		projectButton = (Button) findViewById(R.id.create_project);
		priorityButton = (Button) findViewById(R.id.priority);

		//Here is where I set it Active
		if(isTodoButtonPressed) {
			todoButton.setCompoundDrawablesWithIntrinsicBounds(null, todoIconActive, null, null);
			todoButton.setTextColor(Color.parseColor("#21c4f9"));
			objectName = DatabaseHandler.KEY_TASKNAME;
		} else if(isChecklistButtonPressed) {
			checklistButton.setCompoundDrawablesWithIntrinsicBounds(null, checklistIconActive, null, null);
			checklistButton.setTextColor(Color.parseColor("#21c4f9"));
			objectName = DatabaseHandler.KEY_CHECKLISTNAME;
		} else {
			projectButton.setCompoundDrawablesWithIntrinsicBounds(null, projectIconActive, null, null);
			projectButton.setTextColor(Color.parseColor("#21c4f9"));
			objectName = DatabaseHandler.KEY_PROJECTNAME;
		}


		todoButton.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isTodoButtonPressed == false) {
					todoButton.setCompoundDrawablesWithIntrinsicBounds(null, todoIconActive, null, null);
					todoButton.setTextColor(Color.parseColor("#21c4f9"));
					isTodoButtonPressed = true;
					checklistButton.setCompoundDrawablesWithIntrinsicBounds(null, checklistIconNormal, null, null);
					checklistButton.setTextColor(Color.BLACK);
					projectButton.setCompoundDrawablesWithIntrinsicBounds(null, projectIconNormal, null, null);
					projectButton.setTextColor(Color.BLACK);
					isChecklistButtonPressed = false;
					isProjectButtonPressed = false;
					objectName = DatabaseHandler.KEY_TASKNAME;

				}

			}
		});

		checklistButton.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isChecklistButtonPressed == false) {
					checklistButton.setCompoundDrawablesWithIntrinsicBounds(null, checklistIconActive, null, null);
					checklistButton.setTextColor(Color.parseColor("#21c4f9"));
					isChecklistButtonPressed = true;
					todoButton.setCompoundDrawablesWithIntrinsicBounds(null, todoIconNormal, null, null);
					todoButton.setTextColor(Color.BLACK);
					projectButton.setCompoundDrawablesWithIntrinsicBounds(null, projectIconNormal, null, null);
					projectButton.setTextColor(Color.BLACK);
					isTodoButtonPressed = false;
					isProjectButtonPressed = false;
					objectName = DatabaseHandler.KEY_CHECKLISTNAME;
				}
			}
		});

		if (requestCode == ACTIVITY_PROJECT) {
			projectButton.setVisibility(View.INVISIBLE);
		} else {
			projectButton.setOnClickListener( new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isProjectButtonPressed == false) {
						projectButton.setCompoundDrawablesWithIntrinsicBounds(null, projectIconActive, null, null);
						projectButton.setTextColor(Color.parseColor("#21c4f9"));
						isProjectButtonPressed = true;
						todoButton.setCompoundDrawablesWithIntrinsicBounds(null, todoIconNormal, null, null);
						todoButton.setTextColor(Color.BLACK);
						checklistButton.setCompoundDrawablesWithIntrinsicBounds(null, checklistIconNormal, null, null);
						checklistButton.setTextColor(Color.BLACK);
						isTodoButtonPressed = false;
						isChecklistButtonPressed = false;
						objectName = DatabaseHandler.KEY_PROJECTNAME;
					}				
				}
			});
		}

		priorityButton.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isPriorityPressed) {
					priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, priorityIconActive, null, null);
					priorityButton.setTextColor(Color.RED);
					isPriorityPressed = true;
					Log.i(TAG, "prioritybutton status "+isPriorityPressed);
				}				

				else if(isPriorityPressed) {
					priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, priorityIconNormal, null, null);
					priorityButton.setTextColor(Color.BLACK);
					isPriorityPressed = false;
					Log.i(TAG, "prioritybutton status "+isPriorityPressed);
				}


			}
		});

		addTaskButton.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = addTaskName.getText().toString();
				if (name.isEmpty()) {
					new GobiToast(getApplicationContext(), "Please insert a name.");
				} else {
					//create ContentValues object that will hold the data that is need to be inserted
					ContentValues values = new ContentValues();
					ContentValues valuesForAssign = new ContentValues();
					getIntent().getExtras();

					//add value by using "put" method. it takes key value pair, where the first arg is key and second value is data that needs to be inserted
					//key needs to be a column name from the DatabaseHandler ex: "DatabaseHandler.KEY_TASKNAME"
					values.put(objectName, name);

					if(workspaceSetInOrganise) {
						values.put(DatabaseHandler.KEY_PROJECTID, 0);
						values.put(DatabaseHandler.KEY_WORKSPACEID,workspaceId);
					} else if (projectSetInOrganise || requestCode == ACTIVITY_PROJECT) {
						values.put(DatabaseHandler.KEY_PROJECTID, projectID);
					} else if (requestCode == ActivityCodeConstants.TABHOST_LAYOUT_ACTIVITY) {
						values.put(DatabaseHandler.KEY_WORKSPACEID,workspaceId);
						if (!isProjectButtonPressed) {
							values.put(DatabaseHandler.KEY_PROJECTID, 0);
						}
					} else {
					}

					values.put(DatabaseHandler.KEY_PRIORITY, (isPriorityPressed) ? 1 : 0);
					int priority = values.getAsInteger(DatabaseHandler.KEY_PRIORITY);
					Log.i(TAG,"priority inserted to the database was "+priority);
					if (dueDateSet) {
						values.put(DatabaseHandler.KEY_DUEDATE, dueYear + "-" + String.format("%02d", dueMonth) + "-" + String.format("%02d", dueDay));
						values.put(DatabaseHandler.KEY_DUETIME, String.format("%02d", dueHour) + ":" + String.format("%02d", dueMinute));
					}

					values.put(DatabaseHandler.KEY_NOTE, taskNote);

					int id = 100; //TODO
					Alarm.cancelAlarm(getApplicationContext(), id); //TODO
					if (alarmSet) {
						values.put(DatabaseHandler.KEY_ALARM, alarmDelay);

						Calendar c = Calendar.getInstance();
						c.set(dueYear, dueMonth, dueDay, dueHour, dueMinute);
						c.add(Calendar.MINUTE, -1*alarmDelay);


						Alarm.setAlarm(getApplicationContext(), id, 
								taskName + " is due in " + alarmDelay + " minutes!",
								c.getTimeInMillis()); //TODO
					}
					//use the ContentResolver variable "resolver" and call the insert method on it.
					//the first arg is the uri of the scratch task table "GobiContentProvider.CONTENT_URI_SCRATCH_TASK"
					//the second arg is the ContentValues variable "values"
					Uri contentURI = chooseContentURI();
					//------------retrieving user id-------------------------------------------------------------
					Cursor c = resolver.query(GobiContentProvider.CONTENT_URI_USER, new String[]{DatabaseHandler.KEY_USERID}, null, null, null);
					c.moveToFirst();
					int userIdIndex = c.getColumnIndex(DatabaseHandler.KEY_USERID);
					int userId = c.getInt(userIdIndex);
					values.put(DatabaseHandler.KEY_USERID, userId);
					//--------------------------------------------------------------------------------------------
					if(taskAssigneeSet){
						valuesForAssign.put(DatabaseHandler.KEY_USEREMAIL, assignEmail);
						valuesForAssign.put(DatabaseHandler.KEY_CREATORID, userId);						
						resolver.insert(GobiContentProvider.CONTENT_URI_SCRATCH_TASKASSIGNEE, valuesForAssign);
					}


					resolver.insert(contentURI, values);

					//insert into task assignee table
					Intent i = new Intent();
					i.putExtra("isTodoButtonPressed", isTodoButtonPressed);
					i.putExtra("isChecklistButtonPressed", isChecklistButtonPressed);
					i.putExtra("isProjectButtonPressed", isProjectButtonPressed);
					setResult(ACTIVITY_ADD, i);
					if(isTodoButtonPressed) {
						sutc.encodeTask(taskAssigneeSet);
					}
					finish();
				}
			}
		});

		Button noteButton = (Button) findViewById(R.id.task_note);
		noteButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Button noteButton = (Button) findViewById(R.id.task_note);
				//noteButton.setTextColor(Color.RED);

				Intent i = new Intent(AddTaskActivity.this, NotesActivity.class);
				i.putExtra(DatabaseHandler.KEY_NOTE, taskNote);
				startActivityForResult(i, ActivityCodeConstants.NOTES_ACTIVITY);
				//finish();

			}
		});

		Button duedateButton = (Button) findViewById(R.id.task_due_date);
		duedateButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Button duedateButton = (Button) findViewById(R.id.task_due_date);
				//duedateButton.setTextColor(Color.RED);

				//Redirect to Activity
				Intent i = new Intent(AddTaskActivity.this, DueDateActivity.class);
				Bundle extras = new Bundle();
				extras.putInt("requestCode", ACTIVITY_ADD);
				i.putExtras(extras);
				startActivityForResult(i, ACTIVITY_DATE);

			}
		});

		Button assignButton = (Button) findViewById(R.id.task_assign);
		assignButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				findViewById(R.id.task_assign);

				if (isTodoButtonPressed) {
					Intent i = new Intent(AddTaskActivity.this, AssignTaskActivity.class);
					Bundle extras = new Bundle();
					extras.putInt("requestCode", ACTIVITY_ADD);
					i.putExtras(extras);
					startActivityForResult(i, ACTIVITY_ASSIGN);
					//startActivity(i);
					//finish();
				} else if (isChecklistButtonPressed){
					new GobiToast(getApplicationContext(), "Not implemented for Checklists in this version.");
				} else if (isProjectButtonPressed){
					new GobiToast(getApplicationContext(), "Not implemented for Projects in this version.");
				} else {
					new GobiToast(getApplicationContext(), "Not implemented in this version.");
				}
			}
		});
	}

	protected Uri chooseContentURI() {
		Uri contentUri = null;
		if(isTodoButtonPressed) {
			contentUri = GobiContentProvider.CONTENT_URI_SCRATCH_TASK;
		} else if(isChecklistButtonPressed) {
			contentUri = GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST;
		} else if(isProjectButtonPressed) {
			contentUri = GobiContentProvider.CONTENT_URI_SCRATCH_PROJECT;
		}
		return contentUri;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_task, menu);
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
				}

				alarmSet = extras.getBoolean("alarmSet");
				if (alarmSet) {
					alarmDelay = extras.getInt(DatabaseHandler.KEY_ALARM);
				}

			} else if(resultCode == ActivityCodeConstants.NOTES_ACTIVITY){
				taskNote = extras.getString(DatabaseHandler.KEY_NOTE);

			} else if(resultCode == ActivityCodeConstants.ORGANISE_ACTIVITY) {
				workspaceSetInOrganise = extras.getBoolean("workspaceChanged");
				projectSetInOrganise = extras.getBoolean("projectChanged");
				workspaceId = extras.getInt(DatabaseHandler.KEY_WORKSPACEID);
				projectID = extras.getInt(ScratchProjectColumns.PROJECT_ID);

			} else if(resultCode == ActivityCodeConstants.ASSIGN_TASK_ACTIVITY){
				taskAssigneeSet = extras.getBoolean("taskAssigneeSet");
				if(taskAssigneeSet){
					assignEmail = extras.getString(DatabaseHandler.KEY_USEREMAIL);
				}

			}
		}
	}
}
