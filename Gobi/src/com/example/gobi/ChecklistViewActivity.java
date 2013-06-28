package com.example.gobi;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ChecklistViewActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{

	protected static final String TAG = ChecklistViewActivity.class.getSimpleName();
	private int columnId;
	private int checklistId;
	private String checklistNote;
	private String dueDate;
	private String dueTime;
	//	private static final int LOADER_ID_CHECKLIST_TITLE = 0;
	private static final int LOADER_ID_CHECKLIST_ITEMS = 1;
	private GestureDetectorCompat mGdetector;
	View.OnTouchListener gestureListener;
	private ContentResolver resolver;
	private SimpleCursorAdapter mChecklistItemsAdapter;
	private int[] boundToItems;
	private String[] checklistItemColumns;
	private String checklistName;
	private ListView checklistItemListView;
	private CheckBox checkBox;
	private int priority;


	private String projectName;
	private String workspaceName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.checklist_view);
		TextView checklistTitle = (TextView) findViewById(R.id.checklistTitle);


		//gesture detector
		mGdetector = new GestureDetectorCompat(this, new VGestureListener());
		gestureListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mGdetector.onTouchEvent(event);
			}
		};

		//content resolver
		resolver = getContentResolver();

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		columnId = extras.getInt(DatabaseHandler.KEY_COLUMNID);
		checklistId = extras.getInt(DatabaseHandler.KEY_CHECKLISTID);
		checklistName = extras.getString(DatabaseHandler.KEY_CHECKLISTNAME);
		checklistNote = extras.getString(DatabaseHandler.KEY_NOTE);
		dueDate = extras.getString(DatabaseHandler.KEY_DUEDATE);
		dueTime = extras.getString(DatabaseHandler.KEY_DUETIME);
		priority = extras.getInt(DatabaseHandler.KEY_PRIORITY);

		if(priority == 1) {
			checklistTitle.setTextColor(Color.RED);
		} else {
			checklistTitle.setTextColor(Color.BLACK);
		}

		checklistTitle.setText(checklistName);

		TextView checklistProject = (TextView) findViewById(R.id.checklistProject);
		projectName = extras.getString(DatabaseHandler.KEY_PROJECTNAME);
		if (projectName != null) {
			checklistProject.setText(projectName);
		} else {
			checklistProject.setVisibility(View.INVISIBLE);
		}

		TextView checklistWorkspace = (TextView) findViewById(R.id.checklistWorkspace);
		workspaceName = extras.getString(DatabaseHandler.KEY_WORKSPACENAME);
		if (workspaceName != null) {
			checklistWorkspace.setText(workspaceName);
		} else {
			checklistWorkspace.setVisibility(View.INVISIBLE);
		}

		boundToItems = new int[]{R.id.checkboxChecklistItem,R.id.checkboxChecklistItem};
		checklistItemColumns = new String[]{DatabaseHandler.KEY_ITEMTEXT, DatabaseHandler.KEY_STATUS};

		//adapters
		mChecklistItemsAdapter = new SimpleCursorAdapter(this, R.layout.checklist_item, null, checklistItemColumns, boundToItems, 0);
		mChecklistItemsAdapter.setViewBinder(new ChecklistViewBinder());
		Bundle loaderBundle = new Bundle();
		loaderBundle.putInt(DatabaseHandler.KEY_COLUMNID, checklistId);

		//getSupportLoaderManager().initLoader(LOADER_ID_CHECKLIST_TITLE, loaderBundle, this);
		getSupportLoaderManager().initLoader(LOADER_ID_CHECKLIST_ITEMS, loaderBundle, this);

		checklistItemListView = (ListView) findViewById(R.id.checklistItemListView);
		checklistItemListView.setAdapter(mChecklistItemsAdapter);
		checklistItemListView.setOnTouchListener(gestureListener);
		ImageButton addChecklistItemButton = (ImageButton) findViewById(R.id.addChecklistItemButton);


		checklistItemListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		checklistItemListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});

		addChecklistItemButton.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ChecklistViewActivity.this, AddChecklistItemActivity.class);
				i.putExtra(DatabaseHandler.KEY_COLUMNID, columnId);
				startActivityForResult(i,90);

			}
		});
		//redirect to EditChecklistActivity when checklist name is tapped
		checklistTitle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChecklistViewActivity.this, EditChecklistActivity.class);
				Bundle extras = new Bundle();
				//extras.putInt("requestCode", AddTaskActivity.ACTIVITY_CHECKLIST);
				//				if (dueDateSet) {
				//					extras.putString(DatabaseHandler.KEY_DUEDATE, dueDate);
				//					extras.putString(DatabaseHandler.KEY_DUETIME, dueTime);
				//				}
				//				if (alarmSet) {
				//					extras.putInt(DatabaseHandler.KEY_ALARM, alarmDelay);
				//				}
				extras.putInt(DatabaseHandler.KEY_COLUMNID, columnId);
				extras.putInt(DatabaseHandler.KEY_CHECKLISTID, checklistId);
				extras.putString(DatabaseHandler.KEY_CHECKLISTNAME, checklistName);
				extras.putString(DatabaseHandler.KEY_NOTE, checklistNote);
				extras.putString(DatabaseHandler.KEY_DUEDATE, dueDate);
				extras.putString(DatabaseHandler.KEY_DUETIME, dueTime);
				extras.putInt(DatabaseHandler.KEY_PRIORITY, priority);
				intent.putExtras(extras);
				startActivityForResult(intent, 0);
			}
		});
	}

	class VGestureListener extends GestureDetector.SimpleOnGestureListener {
		private static final String DEBUG_TAG = "Gestures";

		@Override
		public boolean onDown(MotionEvent event) {
			Log.d(DEBUG_TAG, "onDown: " + event.toString());
			return true;
		}

		@Override
		public void onLongPress(MotionEvent event) {

			Cursor c = (Cursor) mChecklistItemsAdapter.getItem(checklistItemListView.pointToPosition(
					Math.round(event.getX()), Math.round(event.getY())));

			Log.d(DEBUG_TAG,
					"onLongPress: "
							+ c.getString(c
									.getColumnIndex(DatabaseHandler.KEY_TASKNAME)));

		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
				float velocityX, float velocityY) {
			// thresholds for distance and velocity
			int swipe_min_distance = 120;
			int swipe_threshold_velocity = 200;

			// calculate x and y displacement
			float distanceY = event1.getY() - event2.getY();
			float distanceX = event1.getX() - event2.getX();

			// check currently works off of displacement, but it's easy enough
			// to switch to velocity if you prefer
			// TODO: Discourage diagonals, enforce more horizontal swipe
			if (Math.abs(distanceX) > Math.abs(distanceY)) {

				// test against specified threshold
				boolean enoughSpeedX = Math.abs(velocityX) > swipe_threshold_velocity;

				// right to left swipe
				if (distanceX > swipe_min_distance && enoughSpeedX) {

					// Get the cursor corresponding to the flung item
					try {
						// Get the cursor corresponding to the flung item
						Cursor c = (Cursor) mChecklistItemsAdapter.getItem(checklistItemListView
								.pointToPosition(Math.round(event1.getX()),
										Math.round(event1.getY())));

						new GobiToast(getApplicationContext(),
								c.getString(
										c.getColumnIndex(DatabaseHandler.KEY_ITEMTEXT))
										+ " was deleted");

						if (c != null && c.getCount() != 0) {
							// get the column ID of the record that needs to be
							// deleted
							int id = c
									.getInt(c
											.getColumnIndex(DatabaseHandler.KEY_COLUMNID));//TODO:itemId instead of columnId when cloud is used
							Uri chItemToBeDeleted = ContentUris.withAppendedId(GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST_ITEM, id);
							int deletedRowNum = resolver.delete(
									chItemToBeDeleted, null, null);//TODO:change to CONTENT_URI_TASK for cloud, and include where clauses
							//DatabaseHandler.KEY_TASKID + " = ?",
							//new String[] { id + "" });
							if (deletedRowNum == 1) {
								//								sutc.deleteTask(id);//TODO:uncomment for cloud functionality
							} else {

							}
							new GobiToast(getApplicationContext(),
									c.getString(
											c.getColumnIndex(DatabaseHandler.KEY_ITEMTEXT))
											+ " was deleted");
							return true;
						}
					} catch (Exception e) {
						Log.e(TAG, "right to left swipe error", e);

					}

					/*
					 * SidebarMenuFragment
					 */

					// left to right swipe
				} else if (distanceX < -swipe_min_distance && enoughSpeedX) {

					Display display = getWindowManager().getDefaultDisplay();
					Point size = new Point();
					display.getSize(size);



					// left to right swipe - only works if from left 20% of
					// screen
					//					if (event1.getX() <= 0.1 * width) {BEGINNING OF SIDEBAR FLING
					//
					//						Log.v("HANZY", "sidebar swipe");
					//						android.support.v4.app.Fragment sideBarMenu = new SideBarMenuActivity();
					//						android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
					//								.beginTransaction();
					//
					//						if (isWorkspaceIconPressed == false) {
					//							ft.add(R.id.sidebarMenuFragment, sideBarMenu)
					//									.commit();
					//							ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					//							isWorkspaceIconPressed = true;
					//
					//						} else {
					//							ft.remove(sideBarMenu).commit();
					//							ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
					//							isWorkspaceIconPressed = false;
					//							Log.v("sideBarMenuFragmentAfterPress",
					//									"This is being pressed");
					//						}
					//						return true;
					//					} else { END OF SIDEBAR FLING *also look down and uncomment a curly brace

					// Get the cursor corresponding to the flung item
					try {
						// Get the cursor corresponding to the flung item
						Cursor c = (Cursor) mChecklistItemsAdapter.getItem(checklistItemListView
								.pointToPosition(Math.round(event1.getX()),
										Math.round(event1.getY())));

						new GobiToast(getApplicationContext(),
								"Completed "
										+ c.getString(
												c.getColumnIndex(DatabaseHandler.KEY_ITEMTEXT)));
						if (c != null && c.getCount() != 0) {
							// get the column ID of the record that needs to
							// be updated
							int id = c
									.getInt(c
											.getColumnIndex(DatabaseHandler.KEY_COLUMNID));//TODO: itemId instead of columnId when cloud is used
							// construct uri that includes the column id
							// for this call the method
							// ContentUris.withAppendedId(uri of the table
							// of the record(s) needs to be updated from,
							// column id)
							// Uri taskSelectedUri =
							// ContentUris.withAppendedId(GobiContentProvider.CONTENT_URI_TASK,id);
							// create ContentValues and put updated values
							// into it
							ContentValues newStatus = new ContentValues();
							newStatus.put(DatabaseHandler.KEY_STATUS, 1);

							// call the update method on ContentResolver
							// variable
							// it takes uri, new updated values and if
							// needed a where clause for further filtering

							Uri taskToBeUpdated = ContentUris.withAppendedId(GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST_ITEM,id);
							resolver.update(
									taskToBeUpdated,//TODO: change to CONTENT_URI_TASK for cloud and add where clause
									newStatus, null, null);
							//DatabaseHandler.KEY_TASKID + " = ?", new String[] { id + "" });
							checkBox.setChecked(true);
							//								if (completedRowNum == 1) {
							////									sutc.completeTask(id);//TODO: uncomment for cloud functionality
							//								} else {
							////									Log.i("completed task",
							////											"didn't work and num rows "
							////													+ completedRowNum);
							//								}
							return true;
						}

					} catch (Exception e) {
						Log.e(TAG, "left to right swipe error", e);
					}

					//}*uncomment this curly brace for SIDEBAR FLING
				}

				// else if Y
				// TODO: Discourage diagonals, enforce more horizontal swipe
			} else if (Math.abs(distanceX) < Math.abs(distanceY)) {

				// test against specified threshold
				boolean enoughSpeedY = Math.abs(velocityY) > swipe_threshold_velocity;

				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				int height = size.y;

				// down to up swipe - only works if from bottom 20% of screen
				if (distanceY > swipe_min_distance && enoughSpeedY
						&& event1.getY() >= 0.9 * height) {
					Intent intent = new Intent(ChecklistViewActivity.this,
							CompletedTaskActivity.class);
					startActivity(intent);
					return true;
				}

			}

			// Return false if you never flang for success
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ActivityCodeConstants.EDIT_CHECKLIST_ACTIVITY) {
			checklistName = data.getStringExtra(DatabaseHandler.KEY_CHECKLISTNAME);
			TextView checklistTitle = (TextView) findViewById(R.id.checklistTitle);
			checklistTitle.setText(checklistName);
			priority = data.getIntExtra(DatabaseHandler.KEY_PRIORITY,-1);
			if(priority == 1) {
				checklistTitle.setTextColor(Color.RED);
			} else {
				checklistTitle.setTextColor(Color.BLACK);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.checklist_view, menu);
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		String[] projectionChecklistItems = {DatabaseHandler.KEY_COLUMNID, DatabaseHandler.KEY_STATUS, DatabaseHandler.KEY_ITEMTEXT, DatabaseHandler.KEY_CHECKLISTID};
		CursorLoader cursorLoader = null;
		//		if(id == LOADER_ID_CHECKLIST_TITLE) {
		//			
		//		//	Uri uriWithId = ContentUris.withAppendedId(GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST, checklistId);
		//			cursorLoader = new CursorLoader(this, GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST, projection, DatabaseHandler.KEY_COLUMNID + " = " + checklistId, null, null);
		//		}Log.i();
		//	else if(id == LOADER_ID_CHECKLIST_ITEMS) {
		//cursorLoader = new CursorLoader(this, GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST_ITEM, projectionChecklistItems, DatabaseHandler.KEY_CHECKLISTID + " = "+ checklistId, null, null);
		cursorLoader = new CursorLoader(this, GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST_ITEM, projectionChecklistItems, DatabaseHandler.KEY_CHECKLISTID + " = "+ columnId, null, null);
		//}

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mChecklistItemsAdapter.swapCursor(cursor);


	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		mChecklistItemsAdapter.swapCursor(null);


	}

	private class ChecklistViewBinder implements SimpleCursorAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int index) {
			if(index == cursor.getColumnIndex(DatabaseHandler.KEY_ITEMTEXT)) {
				if(view.getId() == R.id.checkboxChecklistItem) {
					((CheckBox) view).setText(cursor.getString(index));
					//					
				}
				return true;
			}
			else if(index == cursor.getColumnIndex(DatabaseHandler.KEY_STATUS)) {
				if(view.getId() == R.id.checkboxChecklistItem) {
					boolean status = cursor.getInt(index) == 1;

					CheckBox cb = (CheckBox) view;

					cb.setOnCheckedChangeListener(null);
					cb.setChecked(status);
					cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							int position = checklistItemListView.getPositionForView(buttonView);
							long id = checklistItemListView.getItemIdAtPosition(position);

							if(isChecked) {
								ContentValues newStatus = new ContentValues();
								newStatus.put(DatabaseHandler.KEY_STATUS, 1);
								Uri checklistItemToBeUpdated = ContentUris.withAppendedId(GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST_ITEM, id);
								resolver.update(checklistItemToBeUpdated, newStatus, null, null);
							}
							else {
								ContentValues newStatus = new ContentValues();
								newStatus.put(DatabaseHandler.KEY_STATUS, 0);
								Uri checklistItemToBeUpdated = ContentUris.withAppendedId(GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST_ITEM, id);
								resolver.update(checklistItemToBeUpdated, newStatus, null, null);
							}

						}
					});



				}
				return true;
			}
			return false;
		}

	}

}



