package com.example.gobi;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditWorkspaceActivity extends Activity {

	private String workspaceName;
	private int workspaceIcon;

	//layout
	private EditText editWorkspaceName;
	private ImageButton iconOne;
	private ImageButton iconTwo;
	private ImageButton iconThree;
	private ImageButton iconFour;
	private ImageButton iconFive;
	private ImageButton iconSix;
	private ImageButton iconSeven;
	private ImageButton iconEight;
	private ImageButton iconNine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_workspace);

		//share workspace - hide
		Button createChecklist = (Button) findViewById(R.id.editWorkspace_button);
		createChecklist.setVisibility(View.INVISIBLE);

		//workspace title - pass in value and set
		workspaceName = "Set me please"; //TODO: pass in existing name
		editWorkspaceName = (EditText) findViewById(R.id.editWorkspaceTitle);
		editWorkspaceName.setText(workspaceName);

		//save button - save name and icon choice
		ImageButton confirmButton = (ImageButton) findViewById(R.id.editWorkspaceButton);
		confirmButton.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ContentValues values = new ContentValues();
				values.put(DatabaseHandler.KEY_WORKSPACENAME, editWorkspaceName.getText().toString());
				values.put(DatabaseHandler.KEY_WORKSPACEICON, workspaceIcon);

				//				getContentResolver().insert(GobiContentProvider.CONTENT_URI_SCRATCH_WORKSPACE, values);
				//				sutc.encodeWorkspaceEdit();
				finish();
			}


		});

		//set existing icon choice
		iconOne   = (ImageButton) findViewById(R.id.workspace_icon1);
		iconTwo   = (ImageButton) findViewById(R.id.workspace_icon2);
		iconThree = (ImageButton) findViewById(R.id.workspace_icon3);
		iconFour  = (ImageButton) findViewById(R.id.workspace_icon4);
		iconFive  = (ImageButton) findViewById(R.id.workspace_icon5);
		iconSix   = (ImageButton) findViewById(R.id.workspace_icon6);
		iconSeven = (ImageButton) findViewById(R.id.workspace_icon7);
		iconEight = (ImageButton) findViewById(R.id.workspace_icon8);
		iconNine  = (ImageButton) findViewById(R.id.workspace_icon9);

		workspaceIcon = 1; //TODO pass in existing name

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
