package com.example.gobi;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

public class XGestureListener extends GestureDetector.SimpleOnGestureListener {
	private static final String DEBUG_TAG = "Gestures";
	private SimpleCursorAdapter adapter;
	private ListView list;
	private FragmentActivity fa;
	private Bundle columns;
	private ContentResolver resolver;
	private static final String TAG = XGestureListener.class.getSimpleName();
	private Uri uri;
	private SyncUpToCloud sutc;
	private boolean isTaskListFragment;
	private int deletedRowNum;
	private int completedRowNum;

	public void setSyncUpToCloud(SyncUpToCloud sutc) {
		this.sutc = sutc;
	}

	public void setBoolean(boolean isTaskList) {
		this.isTaskListFragment = isTaskList;
	}

	public void setAdapter(SimpleCursorAdapter adapter) {
		this.adapter = adapter;

	}

	public void setListView(ListView list) {
		this.list = list;
	}

	public void setFragmentActivity(FragmentActivity fa) {
		this.fa = fa;
	}

	/**
	 * 
	 * @param columns
	 * use "name" for the data name value and "id" for the id value.
	 */
	public void setColumns(Bundle columns) {//just name and id, use 
		this.columns = columns;
	}

	public void setContentResolver(ContentResolver resolver) {
		this.resolver = resolver;
	}

	public void setTargetActivity(FragmentActivity target) {
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public void setFragmentManager(FragmentManager fm) {
	}

	@Override
	public boolean onDown(MotionEvent event) {
		Log.d(DEBUG_TAG, "onDown: " + event.toString());
		return true;
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
					Cursor c = (Cursor) adapter.getItem(list
							.pointToPosition(Math.round(event1.getX()),
									Math.round(event1.getY())));

					new GobiToast(fa.getApplicationContext(), c.getString(c
							.getColumnIndex(columns.getString("name")))
							+ " was deleted");

					if (c != null && c.getCount() != 0) {
						// get the column ID of the record that needs to be
						// deleted
						int id = c
								.getInt(c
										.getColumnIndex(columns.getString("id")));//TODO:listId instead of columnId when cloud is used
						Uri taskToBeDeleted = ContentUris.withAppendedId(uri, id);
						if(isTaskListFragment) {
							deletedRowNum = resolver.delete(
									//taskToBeDeleted
									uri,DatabaseHandler.KEY_TASKID + " = ? ", new String[] {" "+id });
						} else {
							deletedRowNum = resolver.delete(
									taskToBeDeleted, null, null);
						}
						//null, null);//TODO:change to CONTENT_URI_TASK for cloud, and include where clauses
						//DatabaseHandler.KEY_TASKID + " = ? ", new String[] {" "+id } <- better
						//DatabaseHandler.KEY_TASKID + " = ?", new String[] { id + "" });
						if ((deletedRowNum == 1)&&isTaskListFragment) {
							sutc.deleteTask(id);//TODO:uncomment for cloud functionality
						} else {

						}

						new GobiToast(fa.getApplicationContext(), c.getString(c
								.getColumnIndex(columns.getString("name")))
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

				Display display = fa.getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);



				// left to right swipe - only works if from left 20% of
				// screen
				//if (event1.getX() <= 0.1 * width) {//BEGINNING OF SIDEBAR FLING

				//					Fragment sideBarMenu = new SideBarMenuActivity();
				//					FragmentTransaction ft = fragmentManager.beginTransaction();
				//
				//					//if (isWorkspaceIconPressed == false) {
				//						ft.add(R.id.sidebarMenuFragment, sideBarMenu)
				//								.commit();
				//						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				//						isWorkspaceIconPressed = true;
				//
				//					//} else {
				//						ft.remove(sideBarMenu).commit();
				//						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				//						isWorkspaceIconPressed = false;
				//								"This is being pressed");
				//					//}
				//					return true;
				//} else { //END OF SIDEBAR FLING *also look down and uncomment a curly brace

				// Get the cursor corresponding to the flung item
				try {
					// Get the cursor corresponding to the flung item
					Cursor c = (Cursor) adapter.getItem(list
							.pointToPosition(Math.round(event1.getX()),
									Math.round(event1.getY())));

					new GobiToast(fa.getApplicationContext(), c.getString(c
							.getColumnIndex(columns.getString("name")))//columns.
							+ " was completed");

					if (c != null && c.getCount() != 0) {
						// get the column ID of the record that needs to
						// be updated
						int id = c
								.getInt(c
										.getColumnIndex(columns.getString("id")));//TODO: listId instead of columnId when cloud is used
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
						Uri taskToBeUpdated = ContentUris.withAppendedId(uri,id);


						if(isTaskListFragment) {
							completedRowNum = resolver.update(
									//taskToBeUpdated,//TODO: change to CONTENT_URI_TASK for cloud and add where clause
									uri,newStatus,DatabaseHandler.KEY_TASKID + " = ? ", new String[] { id + "" } );
						} else {
							completedRowNum = resolver.update(
									taskToBeUpdated,//TODO: change to CONTENT_URI_TASK for cloud and add where clause
									newStatus, null,null);
						}

						//null, null);
						//DatabaseHandler.KEY_TASKID + " = ? ", new String[] { id + "" } <- better
						//DatabaseHandler.KEY_TASKID + " = ?", new String[] { id + "" });
						if ((completedRowNum == 1)&&isTaskListFragment) {
							sutc.completeTask(id);//TODO: uncomment for cloud functionality
						} else {

						}
						return true;
					}

				} catch (Exception e) {
					Log.e(TAG, "left to right swipe error", e);
				}

				//}//*uncomment this curly brace for SIDEBAR FLING
			}

			// else if Y
			// TODO: Discourage diagonals, enforce more horizontal swipe
		} else if (Math.abs(distanceX) < Math.abs(distanceY)) {

			// test against specified threshold
			boolean enoughSpeedY = Math.abs(velocityY) > swipe_threshold_velocity;

			Display display = fa.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int height = size.y;

			// down to up swipe - only works if from bottom 20% of screen
			if (distanceY > swipe_min_distance && enoughSpeedY
					&& event1.getY() >= 0.9 * height) {
				Intent intent = new Intent(fa,
						CompletedTaskActivity.class);
				fa.startActivity(intent);
				return true;
			}

		}

		// Return false if you never flang for success
		return false;
	}
}
