package com.example.gobi;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class OProjectFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;
	private ListView oProjectListView;
	private OrganiseActivity fragmentActivity;
	private static final int LOADER_ID_ORGANISE_PROJECT = 0;
	private OnProjectSelected mCallback;
	private TextView noProjectsToDisplay;


	public OProjectFragment() {}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnProjectSelected) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnProjectSelected");
		}
	}

	public interface OnProjectSelected {
		public void saveProjectId(long id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.o_listview_project, container, false);
		oProjectListView = (ListView) view.findViewById(R.id.ListViewOrganiseProjectList);
		noProjectsToDisplay = (TextView) view.findViewById(R.id.noProjectsOrganise);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentActivity = (OrganiseActivity) super.getActivity();

		String[] projectColumns = new String[] {
				ScratchProjectColumns.PROJECTNAME,
				ScratchProjectColumns.WORKSPACE_ID,
		};

		int[] boundToProject = new int[] { R.id.organise_project, R.id.organise_workspace};
		adapter = new SimpleCursorAdapter(fragmentActivity, R.layout.organise_row, null, projectColumns, boundToProject, 0);

		//make the adapter use our ViewBinder
		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int index) {
				if(index == cursor.getColumnIndex(ScratchProjectColumns.WORKSPACE_ID)) {
					int workspaceIdOfProject = cursor.getInt(index);
					switch(workspaceIdOfProject) {
					case(TabhostLayoutActivity.PERSONAL_WORKSPACE_ID):
						((TextView) view).setText(TabhostLayoutActivity.PERSONAL_WORKSPACE_NAME);
					break;
					case(TabhostLayoutActivity.PLAY_WORKSPACE_ID):
						((TextView) view).setText(TabhostLayoutActivity.PLAY_WORKSPACE_NAME);
					break;
					case(TabhostLayoutActivity.WORK_WORKSPACE_ID):
						((TextView) view).setText(TabhostLayoutActivity.WORK_WORKSPACE_NAME);
					break;
					case(TabhostLayoutActivity.UNI_WORKSPACE_ID):
						((TextView) view).setText(TabhostLayoutActivity.UNI_WORKSPACE_NAME);
					break;
					}
					return true;
				}
				return false;
			}
		});

		//make the ListView use our mTaskAdapter
		oProjectListView.setAdapter(adapter);

		oProjectListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				mCallback.saveProjectId(id);
				for (int i = 0; i < parent.getChildCount(); i++) {
					parent.getChildAt(i).setBackgroundColor(Color.parseColor("#e8e8e8"));
				}
				view.setBackgroundColor(Color.parseColor("#21c4f9"));
			}


		});

		//loader manager initialization
		getLoaderManager().initLoader(LOADER_ID_ORGANISE_PROJECT, null, this);

	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {ScratchProjectColumns._ID,
				ScratchProjectColumns.PROJECTNAME,
				ScratchProjectColumns.WORKSPACE_ID};
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				GobiContentProvider.CONTENT_URI_SCRATCH_PROJECT,
				projection, null,
				null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if(!cursor.moveToFirst()) {
			noProjectsToDisplay.setVisibility(View.VISIBLE);
		}
		adapter.swapCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);

	}


}
