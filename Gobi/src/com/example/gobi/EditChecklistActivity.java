package com.example.gobi;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditChecklistActivity extends Activity {

	private int columnId;
	//private int checklistId;
	private String checklistName;
	private String checklistNote;
	private boolean dueDateSet;
	private String dueDate;
	private String dueTime;
	private int projectId;
	//private int alarmDelay;
	private int workspaceId;
	private int priority;

	public static final String TAG = EditChecklistActivity.class.getSimpleName();

	Button priorityButton;
	public Drawable priorityIconActive;
	public Drawable priorityIconNormal;
	boolean isPriorityPressed;

	private boolean projectChangedInOrganise = false;
	private boolean workspaceChangedInOrganise = false;

	private EditText checklistTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_checklist);

		priorityIconNormal = getResources().getDrawable(R.drawable.ic_action_important);
		priorityIconActive = getResources().getDrawable(R.drawable.ic_action_important_active);

		priorityButton = (Button) findViewById(R.id.priorityChecklist);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		columnId = extras.getInt(DatabaseHandler.KEY_COLUMNID);
		//checklistId = extras.getInt(DatabaseHandler.KEY_CHECKLISTID);
		checklistName = extras.getString(DatabaseHandler.KEY_CHECKLISTNAME);
		checklistNote = extras.getString(DatabaseHandler.KEY_NOTE);
		dueDate = extras.getString(DatabaseHandler.KEY_DUEDATE);
		dueTime = extras.getString(DatabaseHandler.KEY_DUETIME);
		priority = extras.getInt(DatabaseHandler.KEY_PRIORITY);
		//alarmDelay = extras.getInt(DatabaseHandler.KEY_ALARM);

		checklistTitle = (EditText) findViewById(R.id.addChecklistText);
		checklistTitle.setText(checklistName);

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


				}				

				else if(isPriorityPressed==true) {
					priorityButton.setCompoundDrawablesWithIntrinsicBounds(null, priorityIconNormal, null, null);
					priorityButton.setTextColor(Color.BLACK);
					isPriorityPressed = false;
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
				Intent i = new Intent(EditChecklistActivity.this, DueDateActivity.class);
				Bundle extras = new Bundle();
				extras.putInt("requestCode", DueDateActivity.ACTIVITY_EDIT);
				extras.putString(DatabaseHandler.KEY_DUEDATE, dueDate);
				extras.putString(DatabaseHandler.KEY_DUETIME, dueTime);
				//extras.putInt(DatabaseHandler.KEY_ALARM, alarmDelay);
				i.putExtras(extras);
				startActivityForResult(i, EditTaskActivity.ACTIVITY_DATE);

			}
		});

		Button assignButton = (Button) findViewById(R.id.task_assign);
		assignButton.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				new GobiToast(getApplicationContext(), "Not implemented for Checklists in this version.");
			}
		});

		Button organiseButton = (Button) findViewById(R.id.task_organise);
		organiseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(EditChecklistActivity.this, OrganiseActivity.class);
				startActivityForResult(i, 0);

			}
		});

		Button noteButton = (Button) findViewById(R.id.task_note);
		noteButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(EditChecklistActivity.this, NotesActivity.class);
				i.putExtra(DatabaseHandler.KEY_NOTE, checklistNote);
				startActivityForResult(i, ActivityCodeConstants.NOTES_ACTIVITY);
				//finish();

			}
		});

		ImageButton saveButton = (ImageButton) findViewById(R.id.addChecklistButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = checklistTitle.getText().toString();
				if (name.isEmpty()) {
					new GobiToast(getApplicationContext(), "Please insert a name.");
				} else {
					ContentValues values = new ContentValues();
					values.put(DatabaseHandler.KEY_CHECKLISTNAME, name);
					values.put(DatabaseHandler.KEY_NOTE, checklistNote);
					values.put(DatabaseHandler.KEY_DUEDATE, dueDate);
					values.put(DatabaseHandler.KEY_DUETIME, dueTime);
					values.put(DatabaseHandler.KEY_PRIORITY, (isPriorityPressed)? 1:0);
					if(workspaceChangedInOrganise) {
						values.put(DatabaseHandler.KEY_PROJECTID, 0);
						values.put(DatabaseHandler.KEY_WORKSPACEID, workspaceId);
					} else if (projectChangedInOrganise) {
						values.put(DatabaseHandler.KEY_PROJECTID, projectId);
					}
					//values.put(DatabaseHandler.KEY_ALARM, alarmDelay);
					Uri updateUri = ContentUris.withAppendedId(GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST, columnId);
					getContentResolver().update(updateUri, values, null, null);

					Intent i = getIntent();
					i.putExtra(DatabaseHandler.KEY_CHECKLISTNAME, checklistTitle.getText().toString());
					i.putExtra(DatabaseHandler.KEY_PRIORITY, (isPriorityPressed)? 1:0);
					setResult(ActivityCodeConstants.EDIT_CHECKLIST_ACTIVITY, i);
					finish();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_checklist, menu);
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

				//				alarmSet = extras.getBoolean("alarmSet");
				//				if (alarmSet) {
				//					alarmDelay = extras.getInt(DatabaseHandler.KEY_ALARM);
				//				}

			} else if(resultCode == ActivityCodeConstants.NOTES_ACTIVITY){
				checklistNote = extras.getString(DatabaseHandler.KEY_NOTE);

			} else if(resultCode == ActivityCodeConstants.ORGANISE_ACTIVITY) {
				projectId = extras.getInt(ScratchProjectColumns.PROJECT_ID);
				projectChangedInOrganise = extras.getBoolean("projectChanged");
				workspaceId = extras.getInt(DatabaseHandler.KEY_WORKSPACEID);
				workspaceChangedInOrganise = extras.getBoolean("workspaceChanged");
			}
		}


	}
}
