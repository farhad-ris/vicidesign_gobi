package com.example.gobi;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TabhostLayoutActivity extends FragmentActivity implements TabsFragment2.OnIconClickListener, WorkspaceMenuFragment.OnWorkspaceClickedListener {



	public static final String TAG = TabhostLayoutActivity.class.getSimpleName();
	private Fragment taskListFragment;
	private Fragment checklistFragment;
	private Fragment projectListFragment;
	private WorkspaceMenuFragment workspaceFragment;
	FragmentManager fm;
	ImageButton addTaskButton;
	private ImageView workspaceIconImageView;

	//interface to pass data to fragments
	public interface OnWorkspaceChangeListener {
		public void onWorkspaceChange(int currentWorkspaceId);
	}

	private TextView workspaceName;

	//Workspaces
	public static final int PERSONAL_WORKSPACE_ID = 1;
	public static final String PERSONAL_WORKSPACE_NAME = "PERSONAL";
	public static final int WORK_WORKSPACE_ID = 2;
	public static final String WORK_WORKSPACE_NAME = "WORK";
	public static final int UNI_WORKSPACE_ID = 3;
	public static final String UNI_WORKSPACE_NAME = "UNI";
	public static final int PLAY_WORKSPACE_ID = 4;
	public static final String PLAY_WORKSPACE_NAME = "PLAY";


	private int currentWorkspaceIdStatic = PERSONAL_WORKSPACE_ID;//used in the static implementation of workspaces 
	private boolean showWorkspaceFragment = false;

	// SidebarMenuFragment variables
	boolean isWorkspaceIconPressed;

	View.OnTouchListener gestureListener;

	boolean isTaskListTabSelected = true;
	boolean isCheckListTabSelected;
	boolean isProjectTabSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tabhost_layout);

		addTaskButton = (ImageButton) findViewById(R.id.addTaskButton);
		workspaceName = (TextView) findViewById(R.id.workspaceTitle);
		workspaceIconImageView = (ImageView) findViewById(R.id.workspaceIcon);



		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			currentWorkspaceIdStatic = extras.getInt(DatabaseHandler.KEY_WORKSPACEID, 1);
			//TODO: need to get workspace stuff to refresh
			switch(currentWorkspaceIdStatic) {
			case PERSONAL_WORKSPACE_ID:
				onPersonalClicked();
				break;
			case WORK_WORKSPACE_ID:
				onWorkClicked();
				break;
			case UNI_WORKSPACE_ID:
				onUniClicked();
				break;
			case PLAY_WORKSPACE_ID:
				onPlayClicked();
				break;
			}
		}


		workspaceIconImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(TabhostLayoutActivity.this, SideBarMenuActivity.class);
				startActivity(i);
			}
		});

		addTaskButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TabhostLayoutActivity.this,
						AddTaskActivity.class);
				intent.putExtra(DatabaseHandler.KEY_WORKSPACEID, currentWorkspaceIdStatic);
				intent.putExtra("isTaskListTabSelected", isTaskListTabSelected);
				intent.putExtra("isCheckListTabSelected", isCheckListTabSelected);
				intent.putExtra("isProjectTabSelected", isProjectTabSelected);
				intent.putExtra("requestCode", ActivityCodeConstants.TABHOST_LAYOUT_ACTIVITY);

				startActivityForResult(intent, 0); 

			}
		});



		/*
		 * WorkspaceMenuFragment top menu
		 */
		ImageView workspaceMenuButton = (ImageView) findViewById(R.id.workspaceMenuButton);



		workspaceMenuButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageView workspaceMenuButton = (ImageView) findViewById(R.id.workspaceMenuButton);
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				if (!showWorkspaceFragment) {

					workspaceMenuButton.setImageResource(R.drawable.ic_action_workspace_orange);

					workspaceFragment = new WorkspaceMenuFragment();
					ft.add(R.id.workspaceFragmentContainer, workspaceFragment).commit();
					showWorkspaceFragment = true;
				}
				else {
					workspaceMenuButton.setImageResource(R.drawable.ic_action_workspace);

					ft.hide(workspaceFragment);
					showWorkspaceFragment = false;
					ft.commit();
				}
			}
		});

		//set TaskListFragment as the listview to be seen for the first time.
		fm = getSupportFragmentManager();
		taskListFragment = new TaskListFragment();
		Bundle args = new Bundle();
		args.putInt(DatabaseHandler.KEY_WORKSPACEID, currentWorkspaceIdStatic);
		taskListFragment.setArguments(args);
		FragmentTransaction transaction = fm.beginTransaction().add(R.id.fragmentContainer, taskListFragment);
		transaction.addToBackStack(null);
		transaction.commit();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == AddTaskActivity.ACTIVITY_ADD) {
			Bundle extras = new Bundle();
			extras = data.getExtras();
			isTaskListTabSelected = extras.getBoolean("isTodoButtonPressed");
			isCheckListTabSelected = extras.getBoolean("isChecklistButtonPressed");
			isProjectTabSelected = extras.getBoolean("isProjectButtonPressed");
		}
	}

	public boolean isTaskListTabSelected() {
		return isTaskListTabSelected;
	}

	public boolean isCheckListTabSelected() {
		return isCheckListTabSelected;
	}

	public boolean isProjectTabSelected() {
		return isProjectTabSelected;
	}

	//To disable back button from this specific activity
	@Override
	public void onBackPressed() {
	}

	public int getCurrentWorkspaceIdStatic() {
		return currentWorkspaceIdStatic;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabhost_layout, menu);
		return true;
	}

	@Override
	public void onTaskListSelected() {
		FragmentTransaction ft = fm.beginTransaction();
		ft.addToBackStack(null);
		taskListFragment = new TaskListFragment();
		Bundle args = new Bundle();
		args.putInt(DatabaseHandler.KEY_WORKSPACEID, currentWorkspaceIdStatic);
		taskListFragment.setArguments(args);
		ft.replace(R.id.fragmentContainer, taskListFragment);
		ft.addToBackStack(null);
		ft.commit();
		isTaskListTabSelected = true;
		isCheckListTabSelected = false;
		isProjectTabSelected = false;
	}

	@Override
	public void onCheckListSelected() {
		FragmentTransaction ft = fm.beginTransaction();
		checklistFragment = new CheckListFragment();
		Bundle args = new Bundle();
		args.putInt(DatabaseHandler.KEY_WORKSPACEID, currentWorkspaceIdStatic);
		checklistFragment.setArguments(args);
		ft.replace(R.id.fragmentContainer, checklistFragment);//initialise fragments
		ft.addToBackStack(null);
		ft.commit();
		isCheckListTabSelected = true;
		isTaskListTabSelected = false;
		isProjectTabSelected = false;
	}

	@Override
	public void onProjectListSelected() {

		FragmentTransaction ft = fm.beginTransaction();
		projectListFragment = new ProjectListFragment();
		Bundle args = new Bundle();
		args.putInt(DatabaseHandler.KEY_WORKSPACEID, currentWorkspaceIdStatic);
		projectListFragment.setArguments(args);
		ft.replace(R.id.fragmentContainer, projectListFragment);
		ft.addToBackStack(null);
		ft.commit();
		isProjectTabSelected = true;
		isCheckListTabSelected = false;
		isTaskListTabSelected = false;
	}

	@Override
	public void onPersonalClicked() {
		currentWorkspaceIdStatic = PERSONAL_WORKSPACE_ID;
		workspaceName.setText(PERSONAL_WORKSPACE_NAME);
		final Drawable personalIcon = getResources().getDrawable(R.drawable.ic_action_workspace_icon3);
		workspaceIconImageView.setImageDrawable(personalIcon);
		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
		if(currentFragment instanceof TaskListFragment) {
			((TaskListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (TaskListFragment) currentFragment);

		} else if (currentFragment instanceof CheckListFragment) {
			((CheckListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (CheckListFragment) currentFragment);

		} else if (currentFragment instanceof ProjectListFragment) {
			((ProjectListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (ProjectListFragment) currentFragment);

		}

	}

	@Override
	public void onWorkClicked() {
		currentWorkspaceIdStatic = WORK_WORKSPACE_ID;
		workspaceName.setText(WORK_WORKSPACE_NAME);
		final Drawable workIcon = getResources().getDrawable(R.drawable.ic_action_workspace_icon9);
		workspaceIconImageView.setImageDrawable(workIcon);
		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
		if(currentFragment instanceof TaskListFragment) {
			((TaskListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (TaskListFragment) currentFragment);

		} else if (currentFragment instanceof CheckListFragment) {
			((CheckListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (CheckListFragment) currentFragment);

		} else if (currentFragment instanceof ProjectListFragment) {
			((ProjectListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (ProjectListFragment) currentFragment);

		}



	}

	@Override
	public void onUniClicked() {
		currentWorkspaceIdStatic = UNI_WORKSPACE_ID;
		workspaceName.setText(UNI_WORKSPACE_NAME);
		final Drawable uniIcon = getResources().getDrawable(R.drawable.ic_action_workspace_icon1);
		workspaceIconImageView.setImageDrawable(uniIcon);
		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
		if(currentFragment instanceof TaskListFragment) {
			((TaskListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (TaskListFragment) currentFragment);

		} else if (currentFragment instanceof CheckListFragment) {
			((CheckListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (CheckListFragment) currentFragment);

		} else if (currentFragment instanceof ProjectListFragment) {
			((ProjectListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (ProjectListFragment) currentFragment);

		}


	}

	@Override
	public void onPlayClicked() {
		currentWorkspaceIdStatic = PLAY_WORKSPACE_ID;
		workspaceName.setText(PLAY_WORKSPACE_NAME);
		final Drawable playIcon = getResources().getDrawable(R.drawable.ic_action_workspace_icon5);
		workspaceIconImageView.setImageDrawable(playIcon);
		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
		if(currentFragment instanceof TaskListFragment) {
			((TaskListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (TaskListFragment) currentFragment);

		} else if (currentFragment instanceof CheckListFragment) {
			((CheckListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (CheckListFragment) currentFragment);

		} else if (currentFragment instanceof ProjectListFragment) {
			((ProjectListFragment) currentFragment).setWorkspaceId(currentWorkspaceIdStatic);
			currentFragment.getLoaderManager().restartLoader(0, null, (ProjectListFragment) currentFragment);

		}

	}




}




