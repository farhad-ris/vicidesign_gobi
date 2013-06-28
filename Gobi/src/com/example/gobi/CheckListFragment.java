package com.example.gobi;



import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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


public class CheckListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TabhostLayoutActivity.OnWorkspaceChangeListener{

	public static final String TAG = CheckListFragment.class.getSimpleName();
	private static final int LOADER_ID_CHECKLIST = 0;
	private GestureDetectorCompat mGdetector;
	View.OnTouchListener gestureListener;
	private ContentResolver resolver;
	private SimpleCursorAdapter mAdapterChecklist;
	private ListView checkList;
	int[] boundToChecklist;
	ImageView icon;
	// SidebarMenuFragment variables
	boolean isWorkspaceIconPressed;
	private FragmentActivity fragmentActivity;
	public static final int PERSONAL_WORKSPACE_ID = 1;
	private int currentWorkspaceId = PERSONAL_WORKSPACE_ID;

	public CheckListFragment() {}

	public void setWorkspaceId(int id) {
		this.currentWorkspaceId = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.listview_checklists, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		fragmentActivity = super.getActivity();

		Bundle args = this.getArguments();
		if(args != null) {
			currentWorkspaceId = args.getInt(DatabaseHandler.KEY_WORKSPACEID);
		}		

		icon = (ImageView) getActivity().findViewById(R.id.taskIcon);
		checkList = (ListView) getActivity().findViewById(R.id.ListViewChecklistList);

		checkList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(fragmentActivity,
						ChecklistViewActivity.class);
				Bundle extras = new Bundle();
				Cursor c = (Cursor) mAdapterChecklist.getItem(position);
				int checklistIdIndex = c
						.getColumnIndex(DatabaseHandler.KEY_COLUMNID);
				int checklistId = c.getInt(checklistIdIndex);
				// extras.putInt(ActivityCodeConstants.ACTIVITY_CODE_STRING,
				// ActivityCodeConstants.TASKLIST_VIEW_ACTIVITY);
				extras.putInt(ScratchChecklistColumns._ID, checklistId);
				extras.putString(
						DatabaseHandler.KEY_CHECKLISTNAME,
						c.getString(c
								.getColumnIndex(DatabaseHandler.KEY_CHECKLISTNAME)));
				extras.putString(DatabaseHandler.KEY_NOTE,
						c.getString(c.getColumnIndex(DatabaseHandler.KEY_NOTE)));
				extras.putString(DatabaseHandler.KEY_DUEDATE, 
						c.getString(c.getColumnIndex(DatabaseHandler.KEY_DUEDATE)));
				extras.putString(DatabaseHandler.KEY_DUETIME, 
						c.getString(c.getColumnIndex(DatabaseHandler.KEY_DUETIME)));
				extras.putInt(DatabaseHandler.KEY_PRIORITY, c.getInt(c.getColumnIndex(DatabaseHandler.KEY_PRIORITY)));
				intent.putExtras(extras);
				startActivity(intent);
			}

		});

		String[] checklistColumns = new String[] {
				ScratchChecklistColumns.CHECKLISTNAME,
				ScratchChecklistColumns.CHECKLISTNAME,
				ScratchChecklistColumns.DUEDATE,
				ScratchChecklistColumns.DUEDATE };

		boundToChecklist = new int[] { R.id.taskIcon, R.id.taskName,
				R.id.taskDateTop, R.id.taskDateBottom };

		//adapter
		mAdapterChecklist = new SimpleCursorAdapter(getActivity(), R.layout.task_list_view_item, null, checklistColumns, boundToChecklist, 0);

		//make the adapter use our ViewBinder
		mAdapterChecklist.setViewBinder(new TabCheckListViewBinder());

		//content resolver
		resolver = getActivity().getContentResolver();

		//make the ListView use our mTaskAdapter
		checkList.setAdapter(mAdapterChecklist);

		//loader manager initialization
		getLoaderManager().initLoader(LOADER_ID_CHECKLIST, null, this);

		XGestureListener x = new XGestureListener();
		x.setAdapter(mAdapterChecklist);
		Bundle columns = new Bundle();
		columns.putString("name", DatabaseHandler.KEY_CHECKLISTNAME);
		columns.putString("id", DatabaseHandler.KEY_COLUMNID);
		x.setColumns(columns);
		x.setContentResolver(resolver);
		x.setFragmentActivity(fragmentActivity);
		x.setListView(checkList);
		x.setTargetActivity(fragmentActivity);
		x.setUri(GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST);
		x.setBoolean(false);
		//gesture detector
		mGdetector = new GestureDetectorCompat(fragmentActivity.getApplicationContext(), x);
		gestureListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mGdetector.onTouchEvent(event);
			}
		};


		//make our adapter use our gesture listener
		checkList.setOnTouchListener(gestureListener);

	}

	@Override
	public void onResume() {
		super.onResume();
		//		Bundle args = this.getArguments();
		//		if(args != null) {
		//			Log.i(TAG, args.getString(DatabaseHandler.KEY_WORKSPACEID));
		//		}
		//		
	}


	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		String[] projection = { ScratchChecklistColumns.FULL_ID,
				ScratchChecklistColumns.FULL_CHECKLISTNAME,
				ScratchChecklistColumns.FULL_DUEDATE,
				ScratchChecklistColumns.FULL_DUEDATE,
				ScratchChecklistColumns.FULL_DUETIME,
				ScratchChecklistColumns.FULL_STATUS,
				ScratchChecklistColumns.FULL_PRIORITY,
				ScratchChecklistColumns.FULL_NOTE,
				ScratchChecklistColumns.FULL_PROJECT_ID,
				ScratchChecklistColumns.FULL_WORKSPACE_ID};
		return new CursorLoader(getActivity(),
				GobiContentProvider.CONTENT_URI_SCRATCH_CHECKLIST,
				projection, 
				ScratchChecklistColumns.FULL_STATUS + " = ?"
						+ " AND "+ ScratchChecklistColumns.FULL_WORKSPACE_ID + " = ? "
						+ " AND "+ ScratchChecklistColumns.FULL_PROJECT_ID + " = ? ",
						new String[] {"0", ""+currentWorkspaceId, "0"}, ScratchChecklistColumns.FULL_ID + " desc ");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapterChecklist.swapCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapterChecklist.swapCursor(null);

	}

	private class TabCheckListViewBinder implements SimpleCursorAdapter.ViewBinder {

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
					.getColumnIndex(ScratchChecklistColumns.CHECKLISTNAME)) {

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
					((ImageView) view).setImageResource(R.drawable.ic_action_checklist);

				}

				return true;
			}

			else {
				return false;
			}

		}
	}

	@Override
	public void onWorkspaceChange(int currentWorkspaceId) {
		this.currentWorkspaceId = currentWorkspaceId;
		getLoaderManager().restartLoader(LOADER_ID_CHECKLIST, null, this);

	}



}
