package com.example.gobi;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OWorkspaceFragment extends Fragment {


	private OrganiseActivity fragmentActivity;
	private OnWorkspaceSelected mCallback;
	private ListView oWorkspaceListView;
	private ArrayAdapter<String> adapter;
	private String[] workspaces = 
			new String[] {TabhostLayoutActivity.PERSONAL_WORKSPACE_NAME, TabhostLayoutActivity.WORK_WORKSPACE_NAME, TabhostLayoutActivity.UNI_WORKSPACE_NAME, 
			TabhostLayoutActivity.PLAY_WORKSPACE_NAME};


	public OWorkspaceFragment() {}

	public interface OnWorkspaceSelected {
		public void saveWorkspaceId(long id);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentActivity = (OrganiseActivity) getActivity();
		adapter = new ArrayAdapter<String>(fragmentActivity,android.R.layout.simple_list_item_1,workspaces);
		oWorkspaceListView.setAdapter(adapter);
		oWorkspaceListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Log.i("ORGANISE", "id + 1 = "+(id+1));
				mCallback.saveWorkspaceId(id+1);
				for (int i = 0; i < parent.getChildCount(); i++) {
					parent.getChildAt(i).setBackgroundColor(Color.parseColor("#e8e8e8"));
				}
				view.setBackgroundColor(Color.parseColor("#21c4f9"));
			}
		});

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mCallback = (OnWorkspaceSelected) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnWorkspaceSelected");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.o_listview_workspace, container, false);
		oWorkspaceListView = (ListView) view.findViewById(R.id.ListViewOrganiseWorkspaceList);
		return view;
	}



}
