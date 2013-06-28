package com.example.gobi;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddWorkspaceActivity extends Activity {

	EditText workspaceTitle;
	ImageButton addWorkspace;
	ContentResolver resolver;

	private ImageButton iconOne;
	private ImageButton iconTwo;
	private ImageButton iconThree;
	private ImageButton iconFour;
	private ImageButton iconFive;
	private ImageButton iconSix;
	private ImageButton iconSeven;
	private ImageButton iconEight;
	private ImageButton iconNine;

	private int workspaceIcon = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_workspace);

		workspaceTitle = (EditText) findViewById(R.id.WorkspaceTitle);

		addWorkspace = (ImageButton) findViewById(R.id.addWorkspaceButton);
		addWorkspace.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ContentValues values = new ContentValues();

				values.put(DatabaseHandler.KEY_WORKSPACENAME, workspaceTitle.getText().toString());
				values.put(DatabaseHandler.KEY_WORKSPACEICON, workspaceIcon);

				//resolver.insert(GobiContentProvider.CONTENT_URI_SCRATCH_WORKSPACE, values);
				finish();//TODO:Mr. Wade: wait for the table

			}
		});

		iconOne   = (ImageButton) findViewById(R.id.workspace_icon1);
		iconTwo   = (ImageButton) findViewById(R.id.workspace_icon2);
		iconThree = (ImageButton) findViewById(R.id.workspace_icon3);
		iconFour  = (ImageButton) findViewById(R.id.workspace_icon4);
		iconFive  = (ImageButton) findViewById(R.id.workspace_icon5);
		iconSix   = (ImageButton) findViewById(R.id.workspace_icon6);
		iconSeven = (ImageButton) findViewById(R.id.workspace_icon7);
		iconEight = (ImageButton) findViewById(R.id.workspace_icon8);
		iconNine  = (ImageButton) findViewById(R.id.workspace_icon9);

		//workspace icons - change variable
		iconOne.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				workspaceIcon = 1;
			}
		});

		iconTwo.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				workspaceIcon = 2;
			}
		});

		iconThree.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				workspaceIcon = 3;
			}
		});

		iconFour.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				workspaceIcon = 4;
			}
		});

		iconFive.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				workspaceIcon = 5;
			}
		});

		iconSix.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				workspaceIcon = 6;
			}
		});

		iconSeven.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				workspaceIcon = 7;
			}
		});

		iconEight.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				workspaceIcon = 8;
			}
		});

		iconNine.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				workspaceIcon = 9;
			}
		});

	}

}
