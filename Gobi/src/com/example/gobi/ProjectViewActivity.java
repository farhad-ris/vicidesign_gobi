package com.example.gobi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProjectViewActivity extends FragmentActivity implements ProjectTabsFragment.OnIconClickListener {

	public static final String TAG = ProjectViewActivity.class.getSimpleName();

	private Fragment taskListFragment;
	private Fragment checklistFragment;
	FragmentManager fm;

	private int columnId;
	private int projectId;
	private String projectName;
	private String projectNote;
	private boolean dueDateSet;
	private String dueDate;
	private String dueTime;
	private boolean alarmSet;
	private int alarmDelay;
	private String workspaceName;
	private int workspaceId;
	private int priority;

	boolean isTaskListTabSelected = true;
	boolean isCheckListTabSelected;

	private TextView projectTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.project_view);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		columnId = extras.getInt(DatabaseHandler.KEY_COLUMNID);
		projectId = extras.getInt(DatabaseHandler.KEY_PROJECTID);
		projectName = extras.getString(DatabaseHandler.KEY_PROJECTNAME);
		projectNote = extras.getString(DatabaseHandler.KEY_NOTE);
		dueDate = extras.getString(DatabaseHandler.KEY_DUEDATE);
		dueTime = extras.getString(DatabaseHandler.KEY_DUETIME);
		workspaceId = extras.getInt(DatabaseHandler.KEY_WORKSPACEID);
		priority = extras.getInt(DatabaseHandler.KEY_PRIORITY);

		TextView projectWorkspace = (TextView) findViewById(R.id.projectWorkspace);
		workspaceName = extras.getString(DatabaseHandler.KEY_WORKSPACENAME);
		if (workspaceName != null) {
			projectWorkspace.setText(workspaceName);
		} else {
			projectWorkspace.setVisibility(View.INVISIBLE);
		}

		projectTitle = (TextView) findViewById(R.id.projectTitle);
		projectTitle.setText(projectName);
		if(priority == 1) {
			projectTitle.setTextColor(Color.RED);
		} else {
			projectTitle.setTextColor(Color.BLACK);
		}
		projectTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProjectViewActivity.this, EditProjectActivity.class);
				Bundle extras = new Bundle();
				extras.putInt(DatabaseHandler.KEY_COLUMNID, columnId);
				extras.putInt(DatabaseHandler.KEY_PROJECTID, projectId);
				extras.putString(DatabaseHandler.KEY_PROJECTNAME, projectName);
				extras.putString(DatabaseHandler.KEY_NOTE, projectNote);
				extras.putString(DatabaseHandler.KEY_DUEDATE, dueDate);
				extras.putString(DatabaseHandler.KEY_DUETIME, dueTime);
				extras.putInt(DatabaseHandler.KEY_PRIORITY, priority);
				intent.putExtras(extras);
				startActivityForResult(intent, ActivityCodeConstants.EDIT_PROJECT_ACTIVITY);
			}
		});

		ImageButton addItemButton = (ImageButton) findViewById(R.id.projectItemButton);
		//button listener (add and edit)
		addItemButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProjectViewActivity.this, AddTaskActivity.class);
				Bundle extras = new Bundle();
				extras.putInt("requestCode", AddTaskActivity.ACTIVITY_PROJECT);
				extras.putBoolean("isTaskListTabSelected", isTaskListTabSelected);
				extras.putBoolean("isCheckListTabSelected", isCheckListTabSelected);
				if (dueDateSet) {
					extras.putString(DatabaseHandler.KEY_DUEDATE, dueDate);
					extras.putString(DatabaseHandler.KEY_DUETIME, dueTime);

				}
				if (alarmSet) {
					extras.putInt(DatabaseHandler.KEY_ALARM, alarmDelay);
				}
				extras.putInt(DatabaseHandler.KEY_WORKSPACEID, workspaceId);
				extras.putInt(DatabaseHandler.KEY_PROJECTID, projectId);
				intent.putExtras(extras);
				startActivityForResult(intent, AddTaskActivity.ACTIVITY_PROJECT);
			}
		});

		//set TaskListFragment as the listview to be seen for the first time.
		fm = getSupportFragmentManager();
		taskListFragment = new PTaskListFragment();
		Bundle args = new Bundle();
		args.putInt(DatabaseHandler.KEY_PROJECTID, projectId);
		taskListFragment.setArguments(args);
		FragmentTransaction transaction = fm.beginTransaction().add(R.id.fragmentContainer, taskListFragment);
		transaction.addToBackStack(null);
		transaction.commit();

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ProjectViewActivity.this, TabhostLayoutActivity.class);
		intent.putExtra(DatabaseHandler.KEY_WORKSPACEID, workspaceId);

		finish();
		startActivity(intent);
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

		//if (resultCode == ActivityCodeConstants.EDIT_PROJECT_ACTIVITY) {

		Bundle extras = data.getExtras();

		projectName = extras.getString(DatabaseHandler.KEY_PROJECTNAME);
		projectTitle = (TextView) findViewById(R.id.projectTitle);
		projectTitle.setText(projectName);
		priority = extras.getInt(DatabaseHandler.KEY_PRIORITY);
		Log.i(TAG,"priority coming from EditProject is "+priority);
		if(priority == 1) {
			projectTitle.setTextColor(Color.RED);
		} else {
			projectTitle.setTextColor(Color.BLACK);
		}

		//}

	}

	@Override
	public void onTaskListSelected() {
		FragmentTransaction ft = fm.beginTransaction();
		ft.addToBackStack(null);
		taskListFragment = new PTaskListFragment();
		Bundle args = new Bundle();
		args.putInt(DatabaseHandler.KEY_PROJECTID, projectId);
		taskListFragment.setArguments(args);
		ft.replace(R.id.fragmentContainer, taskListFragment);
		ft.addToBackStack(null);
		ft.commit();
		isTaskListTabSelected = true;
		isCheckListTabSelected = false;
	}

	@Override
	public void onCheckListSelected() {
		FragmentTransaction ft = fm.beginTransaction();
		checklistFragment = new PCheckListFragment();
		Bundle args = new Bundle();
		args.putInt(DatabaseHandler.KEY_PROJECTID, projectId);
		checklistFragment.setArguments(args);
		ft.replace(R.id.fragmentContainer, checklistFragment);
		ft.addToBackStack(null);
		ft.commit();
		isCheckListTabSelected = true;
		isTaskListTabSelected = false;
	}
}
