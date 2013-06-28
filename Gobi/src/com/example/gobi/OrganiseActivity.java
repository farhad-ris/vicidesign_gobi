package com.example.gobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
//
public class OrganiseActivity extends FragmentActivity implements OProjectFragment.OnProjectSelected, OWorkspaceFragment.OnWorkspaceSelected, 
OrganiseTabsFragment.OnOrganiseIconClickListener{

	private int projectId;
	private int workspaceId;

	private boolean projectChanged = false;
	private boolean workspaceChanged = false;

	private Fragment oProjectFragment;
	private Fragment oWorkspaceFragment;
	private OrganiseTabsFragment organiseTabsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // to get rid of title bar
		// NEEDS TO BE SET BEFORE setContentView
		setContentView(R.layout.organise);

		oProjectFragment = new OProjectFragment();
		oWorkspaceFragment = new OWorkspaceFragment();
		organiseTabsFragment = new OrganiseTabsFragment();
		Bundle args = new Bundle();


		if (getIntent().getIntExtra("requestCode", 0)
				!= ActivityCodeConstants.EDIT_PROJECT_ACTIVITY) {
			args.putBoolean("singleTab", false);
			organiseTabsFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction().add(R.id.OrganiseTabsFragmentContainer, organiseTabsFragment).commit();
			getSupportFragmentManager().beginTransaction().add(R.id.OrganiseFragmentContainer, oProjectFragment).commit();


		} else {

			args.putBoolean("singleTab", true);
			organiseTabsFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction().add(R.id.OrganiseTabsFragmentContainer, organiseTabsFragment).commit();
			getSupportFragmentManager().beginTransaction().add(R.id.OrganiseFragmentContainer, oWorkspaceFragment).commit();

		}



		TextView organiseHeader = (TextView) findViewById(R.id.taskinfo_title);
		organiseHeader.setText(R.string.organise_title_taskinfo);
		ImageButton doneButton = (ImageButton) findViewById(R.id.editDoneButton);
		doneButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(ScratchProjectColumns.PROJECT_ID, projectId);
				i.putExtra("projectChanged", projectChanged);
				setResult(ActivityCodeConstants.ORGANISE_ACTIVITY,i);
				i.putExtra(DatabaseHandler.KEY_WORKSPACEID, workspaceId);
				i.putExtra("workspaceChanged", workspaceChanged);
				finish();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.organise, menu);
		return true;
	}

	@Override
	public void saveProjectId(long id) {
		this.projectId = (int) id;
		projectChanged = true;
		workspaceChanged = false;
	}

	@Override
	public void saveWorkspaceId(long id) {
		this.workspaceId = (int) id;
		workspaceChanged = true;
		projectChanged = false;
	}

	@Override
	public void onProjectIconSelected() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.addToBackStack(null);
		oProjectFragment = new OProjectFragment();
		ft.replace(R.id.OrganiseFragmentContainer, oProjectFragment);
		ft.commit();

	}

	@Override
	public void onWorkspaceIconSelected() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.addToBackStack(null);
		oWorkspaceFragment = new OWorkspaceFragment();
		ft.replace(R.id.OrganiseFragmentContainer, oWorkspaceFragment);
		ft.commit();

	}

}
