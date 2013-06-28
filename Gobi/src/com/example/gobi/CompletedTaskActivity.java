package com.example.gobi;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.widget.ListView;

public class CompletedTaskActivity extends FragmentActivity  implements LoaderCallbacks<Cursor>{

	public static final String TAG = CompletedTaskActivity.class.getSimpleName();
	private static final int LOADER_ID = 1;
	private SimpleCursorAdapter mAdapter;
	private ListView completedTaskList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.completed_task);

		String[] columns = new String[] {DatabaseHandler.KEY_TASKNAME, DatabaseHandler.KEY_DUEDATE, DatabaseHandler.KEY_DUEDATE} ;
		int[] boundTo = new int[] { R.id.taskNameCompleted, R.id.taskDateTopCompleted, R.id.taskDateBottomCompleted};

		mAdapter = new SimpleCursorAdapter(this, R.layout.completed_task_item, null, columns, boundTo, 
				0);

		completedTaskList = (ListView) findViewById(R.id.ListViewCompletedTask);
		completedTaskList.setAdapter(mAdapter);

		getSupportLoaderManager().initLoader(LOADER_ID, null, this);

		//		resolver.query(GobiContentProvider.CONTENT_URI_SCRATCH_TASK, null, DatabaseHandler.KEY_STATUS + " = ?", new String[] {"1"}, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.completed_task, menu);
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {DatabaseHandler.KEY_COLUMNID, DatabaseHandler.KEY_TASKNAME, 
				DatabaseHandler.KEY_DUEDATE, DatabaseHandler.KEY_DUEDATE, DatabaseHandler.KEY_STATUS};
		CursorLoader cursorLoader = new CursorLoader(this, GobiContentProvider.CONTENT_URI_SCRATCH_TASK, 
				projection, DatabaseHandler.KEY_STATUS + " = ?", new String[] {"1"}, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);

	}

}
