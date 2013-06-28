package com.example.gobi;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PTaskListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

	public static final String TAG = PTaskListFragment.class.getSimpleName();
	private static final int LOADER_ID_TASK = 0;
	private GestureDetectorCompat mGdetector;
	View.OnTouchListener gestureListener;
	private ContentResolver resolver;
	private SimpleCursorAdapter mAdapterTask;
	private ListView taskList;
	private ProjectViewActivity fragmentActivity;
	private int projectId;

	int[] boundToTask;
	ImageView icon;
	// SidebarMenuFragment variables

	public PTaskListFragment() {}

	@Override
	public void onResume() {
		super.onResume();
		//		Bundle args = this.getArguments();
		//		if(args != null) {
		//		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//inflate UI, get references and bind to views within my fragment.
		super.onCreateView(inflater, container, savedInstanceState);

		return inflater.inflate(R.layout.listview_tasks, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// Here is where I can interact with the UI of the parent activity
		super.onActivityCreated(savedInstanceState);
		fragmentActivity = (ProjectViewActivity) super.getActivity();
		//view variables
		icon = (ImageView) getActivity().findViewById(R.id.taskIcon);
		taskList = (ListView) getActivity().findViewById(R.id.ListViewTaskList);

		//currentWroskapceId
		Bundle args = this.getArguments();
		if(args != null) {
			projectId = args.getInt(DatabaseHandler.KEY_PROJECTID);
		}

		//taskList listview on item click listener
		taskList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor c = (Cursor) mAdapterTask.getItem(position);
				// c.moveToFirst();
				// task name, due date, priority, note
				int taskNameIndex = c
						.getColumnIndex(DatabaseHandler.KEY_TASKNAME);
				int taskNoteIndex = c.getColumnIndex(DatabaseHandler.KEY_NOTE);
				int priorityIndex = c
						.getColumnIndex(DatabaseHandler.KEY_PRIORITY);
				int columnIdIndex = c
						.getColumnIndex(DatabaseHandler.KEY_COLUMNID);
				int dueDateIndex = c
						.getColumnIndex(DatabaseHandler.KEY_DUEDATE);
				int dueTimeIndex = c
						.getColumnIndex(DatabaseHandler.KEY_DUETIME);
				int taskIdIndex = c.getColumnIndex(DatabaseHandler.KEY_TASKID);
				int workspaceIdIndex = c.getColumnIndex(DatabaseHandler.KEY_WORKSPACEID);
				int projectIdindex = c.getColumnIndex(DatabaseHandler.KEY_PROJECTID);

				int projectId = c.getInt(projectIdindex);
				int workspaceId = c.getInt(workspaceIdIndex);
				String taskName = c.getString(taskNameIndex);
				String taskNote = c.getString(taskNoteIndex);
				int priority = c.getInt(priorityIndex);
				int columnId = c.getInt(columnIdIndex);
				String dueDate = c.getString(dueDateIndex);
				String dueTime = c.getString(dueTimeIndex);
				int taskId = c.getInt(taskIdIndex);
				Bundle extras = new Bundle();
				extras.putInt(DatabaseHandler.KEY_PROJECTID, projectId);
				extras.putInt(DatabaseHandler.KEY_WORKSPACEID, workspaceId);
				extras.putString(DatabaseHandler.KEY_TASKNAME, taskName);
				extras.putString(DatabaseHandler.KEY_NOTE, taskNote);
				extras.putInt(DatabaseHandler.KEY_PRIORITY, priority);
				extras.putInt(DatabaseHandler.KEY_COLUMNID, columnId);
				extras.putString(DatabaseHandler.KEY_DUEDATE, dueDate);
				extras.putString(DatabaseHandler.KEY_DUETIME, dueTime);
				extras.putInt(DatabaseHandler.KEY_TASKID, taskId);


				// create bundle
				Intent intent = new Intent(fragmentActivity,
						EditTaskActivity.class);
				intent.putExtras(extras);
				startActivity(intent);
			}
		});


		//content resolver
		resolver = getActivity().getContentResolver();

		//columns from database
		String[] taskColumns = new String[] { ScratchTaskColumns.TASKNAME,
				ScratchTaskColumns.TASKNAME, ScratchTaskColumns.DUEDATE,
				ScratchTaskColumns.DUEDATE };

		//views that we are binding columns to
		boundToTask = new int[] { R.id.taskIcon, R.id.taskName,
				R.id.taskDateTop, R.id.taskDateBottom };

		//adapter
		mAdapterTask = new SimpleCursorAdapter(getActivity(), R.layout.task_list_view_item, null, taskColumns, boundToTask, 0);

		//make the adapter use our ViewBinder
		mAdapterTask.setViewBinder(new TabTaskListViewBinder());


		//make the ListView use our mTaskAdapter
		taskList.setAdapter(mAdapterTask);

		//loader manager initialization
		getLoaderManager().initLoader(LOADER_ID_TASK, null, this);


		XGestureListener x = new XGestureListener();
		x.setAdapter(mAdapterTask);
		Bundle columns = new Bundle();
		columns.putString("name", DatabaseHandler.KEY_TASKNAME);
		columns.putString("id", DatabaseHandler.KEY_TASKID);//changing to taskId
		x.setColumns(columns);
		x.setContentResolver(resolver);
		x.setFragmentActivity(fragmentActivity);
		x.setListView(taskList);
		x.setTargetActivity(fragmentActivity);
		x.setUri(GobiContentProvider.CONTENT_URI_TASK);
		x.setBoolean(true);
		SyncUpToCloud sutc = new SyncUpToCloud(fragmentActivity);
		x.setSyncUpToCloud(sutc);
		//gesture detector
		mGdetector = new GestureDetectorCompat(fragmentActivity.getApplicationContext(), x);
		gestureListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mGdetector.onTouchEvent(event);
			}
		};

		//make our list use our gesture listener
		taskList.setOnTouchListener(gestureListener);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);


	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}



	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { DatabaseHandler.KEY_TASKID,
				DatabaseHandler.KEY_COLUMNID, DatabaseHandler.KEY_TASKNAME,
				DatabaseHandler.KEY_DUEDATE, DatabaseHandler.KEY_DUEDATE,
				DatabaseHandler.KEY_DUETIME, DatabaseHandler.KEY_STATUS,
				DatabaseHandler.KEY_PRIORITY, DatabaseHandler.KEY_NOTE,
				DatabaseHandler.KEY_PROJECTID, DatabaseHandler.KEY_WORKSPACEID };
		//I'm not including cloud functionality as of yet, we still need to see if we are getting results locally 
		//with a TabbedLayout


		return new CursorLoader(getActivity(),

				GobiContentProvider.CONTENT_URI_TASK, projection,
				DatabaseHandler.KEY_STATUS + " = ?"
						+ " AND "+ DatabaseHandler.KEY_PROJECTID + " = ?", 
						new String[] {"0",""+projectId} , DatabaseHandler.KEY_TASKID + " desc ");

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapterTask.swapCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapterTask.swapCursor(null);

	}

	private class TabTaskListViewBinder implements SimpleCursorAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int index) {
			//

			if (index == cursor.getColumnIndex(DatabaseHandler.KEY_DUEDATE)) {
				String date = "";
				String day = "";
				String month = "";
				if (((TextView) view).getId() == R.id.taskDateTop) {
					date = cursor.getString(index);
					day = date.substring(8);
					((TextView) view).setText(day);

				} else if (((TextView) view).getId() == R.id.taskDateBottom) {
					date = cursor.getString(index);
					month = date.substring(5, 7);

					// array instead of switch for readability's sake
					String[] months = new String[] { "dummy",
							getResources().getString(R.string.jan),
							getResources().getString(R.string.feb),
							getResources().getString(R.string.mar),
							getResources().getString(R.string.apr),
							getResources().getString(R.string.may),
							getResources().getString(R.string.jun),
							getResources().getString(R.string.jul),
							getResources().getString(R.string.aug),
							getResources().getString(R.string.sep),
							getResources().getString(R.string.oct),
							getResources().getString(R.string.nov),
							getResources().getString(R.string.dec), };

					((TextView) view).setText(months[Integer.parseInt(month)]);

				}
				return true;

			} else if (index == cursor
					.getColumnIndex(DatabaseHandler.KEY_TASKNAME)) {

				if (view.getId() == R.id.taskName) {
					((TextView) view).setText(cursor.getString(index));
					if (cursor.getInt(cursor
							.getColumnIndex(DatabaseHandler.KEY_PRIORITY)) == 1) {
						((TextView) view).setTextColor(Color.RED);

					}
					if (cursor.getInt(cursor
							.getColumnIndex(DatabaseHandler.KEY_PRIORITY)) == 0) {
						((TextView) view).setTextColor(Color.BLACK);
					}
				} else if (view.getId() == R.id.taskIcon) {
					if (cursor.getString(index).toLowerCase().contains("email")
							|| cursor.getString(index).toLowerCase()
							.contains("e-mail")) {
						((ImageView) view)
						.setImageResource(R.drawable.ic_action_email);
					} else if (cursor.getString(index).toLowerCase()
							.contains("call")
							|| cursor.getString(index).toLowerCase()
							.contains("ring")) {
						((ImageView) view)
						.setImageResource(R.drawable.ic_action_call);
					} else if (cursor.getString(index).toLowerCase()
							.contains("browse")) {
						((ImageView) view)
						.setImageResource(R.drawable.ic_action_browse);
					}
				}

				return true;
			}

			else {
				return false;
			}
		}
	}
}
