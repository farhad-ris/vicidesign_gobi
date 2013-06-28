package com.example.gobi;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class OrganiseTabsFragment extends Fragment {

	private Button projectButtonTab;
	private Button workspaceButtonTab;
	OnOrganiseIconClickListener mCallback;
	private boolean isProjectIconSelected;
	private boolean isWorkspaceIconSelected;
	private Drawable projectIconActive;
	private Drawable projectIconNormal;
	private Drawable workspaceIconActive;
	private Drawable workspaceIconNormal;
	private int fragmentLayout;
	private boolean isSingleTab;

	public OrganiseTabsFragment() {

	}

	public interface OnOrganiseIconClickListener {
		public void onProjectIconSelected();

		public void onWorkspaceIconSelected();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		workspaceIconActive = getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace_blue);
		workspaceIconNormal = getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace);

		if (!isSingleTab) {

			isProjectIconSelected = true;
			projectIconActive = getActivity().getResources().getDrawable(
					R.drawable.ic_create_project_active);
			projectIconNormal = getActivity().getResources().getDrawable(
					R.drawable.ic_create_project);

			projectButtonTab.setCompoundDrawablesWithIntrinsicBounds(null,
					projectIconActive, null, null);
			projectButtonTab.setTextColor(Color.parseColor("#21c4f9"));

			projectButtonTab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!isProjectIconSelected) {
						projectButtonTab
						.setCompoundDrawablesWithIntrinsicBounds(null,
								projectIconActive, null, null);
						projectButtonTab.setTextColor(Color
								.parseColor("#21c4f9"));
						workspaceButtonTab
						.setCompoundDrawablesWithIntrinsicBounds(null,
								workspaceIconNormal, null, null);
						workspaceButtonTab.setTextColor(Color
								.parseColor("#000000"));
						isProjectIconSelected = true;
						isWorkspaceIconSelected = false;
						// set the workspace icon to non selected
					}
					mCallback.onProjectIconSelected();

				}
			});
		}

		workspaceButtonTab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isWorkspaceIconSelected) {
					// make workspace icon active

					workspaceButtonTab.setCompoundDrawablesWithIntrinsicBounds(
							null, workspaceIconActive, null, null);
					workspaceButtonTab.setTextColor(Color.parseColor("#21c4f9"));
					projectButtonTab.setCompoundDrawablesWithIntrinsicBounds(
							null, projectIconNormal, null, null);
					projectButtonTab.setTextColor(Color.parseColor("#000000"));
					isProjectIconSelected = false;
					isWorkspaceIconSelected = true;

				}
				mCallback.onWorkspaceIconSelected();

			}
		});

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnOrganiseIconClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnOrganiseIconClickListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (getArguments() != null) {
			isSingleTab = getArguments().getBoolean("singleTab");
		}
		if (isSingleTab) {
			fragmentLayout = R.layout.tabs_fragment_organise_workspace;
		} else {
			fragmentLayout = R.layout.tabs_fragment_organise;
		}
		View view = inflater.inflate(fragmentLayout, container, false);
		projectButtonTab = (Button) view.findViewById(R.id.projectsButtonTab);
		workspaceButtonTab = (Button) view
				.findViewById(R.id.workspaceButtonTab);
		return view;
	}

}
