package com.example.gobi;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddChecklistItemActivity extends Activity {

	public static final int ADD_CHECKLIST_ITEM_ACTIVITY_CODE = 10;
	private EditText addChecklistItemName;
	String objectName = DatabaseHandler.KEY_ITEMTEXT;
	private int columnId;
	ContentResolver resolver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_checklist_item);
		addChecklistItemName = (EditText) findViewById(R.id.addChecklistItemText);
		resolver = getContentResolver();
		ImageButton addChecklistItemButton = (ImageButton) findViewById(R.id.addChecklistItemButton);

		Intent i = getIntent();
		columnId = i.getIntExtra(DatabaseHandler.KEY_COLUMNID, -1);
		addChecklistItemButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = addChecklistItemName.getText().toString();
				if (name.isEmpty()) {
					new GobiToast(getApplicationContext(), "Please insert text.");
				} else {
					Intent i = new Intent(AddChecklistItemActivity.this, ChecklistViewActivity.class);
					i.putExtra(ActivityCodeConstants.ACTIVITY_CODE_STRING, ActivityCodeConstants.ADD_CHECKLIST_ITEM_ACTIVITY);
					ContentValues values = new ContentValues();
					values.put(objectName, addChecklistItemName.getText().toString());
					values.put(DatabaseHandler.KEY_CHECKLISTID, columnId);
					Uri contentURI = GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST_ITEM;
					resolver.insert(contentURI, values);

					//					startActivityForResult(i, ActivityCodeConstants.ADD_CHECKLIST_ITEM_ACTIVITY);
					setResult(ActivityCodeConstants.ADD_CHECKLIST_ITEM_ACTIVITY, i);
					finish();
				}
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_checklist, menu);
		return true;
	}

}
