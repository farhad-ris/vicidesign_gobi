package com.example.gobi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class NotesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // to get rid of title bar
		// NEEDS TO BE SET BEFORE setContentView
		setContentView(R.layout.notes);

		//set existing note

		Bundle extras = getIntent().getExtras();
		String taskNote;
		if (extras != null && 
				(taskNote = extras.getString(DatabaseHandler.KEY_NOTE)) != null){
			EditText note = (EditText) findViewById(R.id.EditText02);
			note.setText(taskNote);
		}

		TextView commentsHeader = (TextView) findViewById(R.id.taskinfo_title);
		commentsHeader.setText(R.string.note_title_taskinfo);

		ImageButton noteSubmit = (ImageButton) findViewById(R.id.editDoneButton);
		noteSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText note = (EditText) findViewById(R.id.EditText02);
				String noteText = note.getText().toString();

				Intent i = new Intent();
				i.putExtra(DatabaseHandler.KEY_NOTE, noteText);
				Log.i("NOTE", "Leaving NoteActivity: " + noteText);
				setResult(ActivityCodeConstants.NOTES_ACTIVITY, i);
				finish();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes, menu);
		return true;
	}

}
