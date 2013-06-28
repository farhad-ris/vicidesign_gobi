package com.example.gobi;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
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

public class EditProjectActivity extends Activity {

	protected static final String TAG = EditProjectActivity.class.getSimpleName();
	private int columnId;
	private String projectName;
	private String projectNote;
	private String dueDate;
	private String dueTime;
	private int alarmDelay;
	private EditText projectTitle;
	private boolean dueDateSet;
	private boolean alarmSet;
	private int workspaceId;
	private boolean workspaceChangedInOrganise = false;

	Button priorityButton;
	public Drawable priorityIconActive;
	public Drawable priorityIconNormal;
	boolean isPriorityPressed;
	private int priority;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_project);

		priorityIconNormal = getResources().getDrawable(R.drawable.ic_action_important);
		priorityIconActive = getResources().getDrawable(R.drawable.ic_action_important_active);

		priorityButton = (Button) findViewById(R.id.priorityProject);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		columnId = extras.getInt(DatabaseHandler.KEY_COLUMNID);
		projectName = extras.getString(DatabaseHandler.KEY_PROJECTNAME);
		projectNote = extras.getString(DatabaseHandler.KEY_NOTE);
		dueDate = extras.getString(DatabaseHandler.KEY_DUEDATE);
		dueTime = extras.getString(DatabaseHandler.KEY_DUETIME);
		priority = extras.getInt(DatabaseHandler.KEY_PRIORITY);
		alarmDelay = extras.getInt(DatabaseHandler.KEY_ALARM);

		//Set the project's current title
		projectTitle = (EditText) findViewById(R.id.addTaskInProjectText);
		projectTitle.setText(projectName);

		Button projectButton = (Button) findViewById(R.id.projectHighlighted);
		projectButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_create_project_active), null, null);
		projectButton.setTextColor(Color.parseColor("#21c4f9"));

		if(priority == 1) {
			priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, priorityIconActive, null, null);
			priorityButton.setTextColor(Color.RED);
			isPriorityPressed = true;
		} else {
			priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, priorityIconNormal, null, null);
			priorityButton.setTextColor(Color.BLACK);
			isPriorityPressed = false;
		}

		priorityButton.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isPriorityPressed == false) {
					priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, priorityIconActive, null, null);
					priorityButton.setTextColor(Color.RED);
					isPriorityPressed = true;
					Log.i(TAG,"isPriority pressed "+isPriorityPressed);
				}				

				else if(isPriorityPressed==true) {
					priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, priorityIconNormal, null, null);
					priorityButton.setTextColor(Color.BLACK);
					isPriorityPressed = false;
					Log.i(TAG,"isPriority pressed "+isPriorityPressed);
				}


			}
		});


		Button duedateButton = (Button) findViewById(R.id.task_due_date);
		duedateButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Button duedateButton = (Button) findViewById(R.id.task_due_date);
				//duedateButton.setTextColor(Color.RED);

				//Redirect to Activity
				Intent i = new Intent(EditProjectActivity.this, DueDateActivity.class);
				Bundle extras = new Bundle();
				extras.putInt("requestCode", DueDateActivity.ACTIVITY_EDIT);
				extras.putString(DatabaseHandler.KEY_DUEDATE, dueDate);
				extras.putString(DatabaseHandler.KEY_DUETIME, dueTime);
				extras.putInt(DatabaseHandler.KEY_ALARM, alarmDelay);
				i.putExtras(extras);
				startActivityForResult(i, EditTaskActivity.ACTIVITY_DATE);

			}
		});

		Button organiseButton = (Button) findViewById(R.id.task_organise);
		organiseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(EditProjectActivity.this, OrganiseActivity.class);
				i.putExtra("requestCode", ActivityCodeConstants.EDIT_PROJECT_ACTIVITY);
				startActivityForResult(i, 0);

			}
		});

		Button assignButton = (Button) findViewById(R.id.task_assign);
		assignButton.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				new GobiToast(getApplicationContext(), "Not implemented for Projects in this version.");
			}
		});

		Button noteButton = (Button) findViewById(R.id.task_note);
		noteButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(EditProjectActivity.this, NotesActivity.class);
				i.putExtra(DatabaseHandler.KEY_NOTE, projectNote);
				startActivityForResult(i, ActivityCodeConstants.NOTES_ACTIVITY);
				//finish();

			}
		});

		ImageButton saveButton = (ImageButton) findViewById(R.id.addTaskInProjectButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = projectTitle.getText().toString();
				if (name.isEmpty()) {
					new GobiToast(getApplicationContext(), "Please insert a name.");
				} else {
					ContentValues values = new ContentValues();
					values.put(DatabaseHandler.KEY_PROJECTNAME, name);
					values.put(DatabaseHandler.KEY_DUEDATE, dueDate);
					values.put(DatabaseHandler.KEY_DUETIME, dueTime);
					values.put(DatabaseHandler.KEY_NOTE, projectNote);
					values.put(DatabaseHandler.KEY_PRIORITY, (isPriorityPressed)? 1:0);

					if (workspaceChangedInOrganise) {
						values.put(DatabaseHandler.KEY_WORKSPACEID, workspaceId);
					}
					Uri updateUri = ContentUris.withAppendedId(GobiContentProvider.CONTENT_URI_SCRATCH_PROJECT, columnId);
					getContentResolver().update(updateUri, values, null, null);

					Intent i = getIntent();
					i.putExtra(DatabaseHandler.KEY_PROJECTNAME, projectTitle.getText().toString());
					i.putExtra(DatabaseHandler.KEY_PRIORITY, (isPriorityPressed)? 1:0);
					Log.i(TAG, "value of priority put in intent "+i.getIntExtra(DatabaseHandler.KEY_PRIORITY, -1));
					setResult(ActivityCodeConstants.EDIT_PROJECT_ACTIVITY, i);
					finish();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_project, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {

			Bundle extras = data.getExtras();

			if(resultCode == EditTaskActivity.ACTIVITY_DATE) {
				dueDateSet = extras.getBoolean("dueDateSet");
				if (dueDateSet) {
					int dueYear = extras.getInt("dueYear");
					int dueMonth = extras.getInt("dueMonth");
					int dueDay = extras.getInt("dueDay");
					int dueHour = extras.getInt("dueHour");
					int dueMinute = extras.getInt("dueMinute");
					dueDate = dueYear + "-" + String.format("%02d", dueMonth) + "-" + String.format("%02d", dueDay);
					dueTime = String.format("%02d", dueHour) + ":" + String.format("%02d", dueMinute);
				}

				alarmSet = extras.getBoolean("alarmSet");
				if (alarmSet) {
					alarmDelay = extras.getInt(DatabaseHandler.KEY_ALARM);
				}

			} else if(resultCode == ActivityCodeConstants.NOTES_ACTIVITY){
				projectNote = extras.getString(DatabaseHandler.KEY_NOTE);

			} else if(resultCode == ActivityCodeConstants.ORGANISE_ACTIVITY) {
				workspaceId = extras.getInt(DatabaseHandler.KEY_WORKSPACEID);
				workspaceChangedInOrganise = extras.getBoolean("workspaceChanged");
			}
		}


	}

}
