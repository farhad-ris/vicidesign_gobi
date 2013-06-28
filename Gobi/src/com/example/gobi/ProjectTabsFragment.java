package com.example.gobi;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ProjectTabsFragment extends Fragment {
	OnIconClickListener mCallback;

	public Drawable todoIconActive;
	public Drawable todoIconNormal;
	public Drawable checklistIconActive;
	public Drawable checklistIconNormal;
	public Drawable projectIconActive;
	public Drawable projectIconNormal;

	private Button todoButton;
	private Button checklistButton;
	private Button projectButton;

	boolean isTodoButtonPressed;
	boolean isChecklistButtonPressed;
	boolean isProjectButtonPressed;


	//empty constructor as per Android doco
	public ProjectTabsFragment() {

	}


	//container Activity must implement this interace so the frag can deliver messages
	protected interface OnIconClickListener {
		/** Called by TabsFragment2 when an icon is selected */
		public void onTaskListSelected();
		public void onCheckListSelected();


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.tabs_fragment, container, false);
		todoButton = (Button) fragmentView.findViewById(R.id.tasksButton);
		checklistButton = (Button) fragmentView.findViewById(R.id.checklistsButton);
		projectButton = (Button) fragmentView.findViewById(R.id.projectsButton);
		projectButton.setVisibility(View.INVISIBLE);

		return fragmentView;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnIconClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnIconClickListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		todoIconActive = getActivity().getResources().getDrawable(
				R.drawable.ic_create_todo_active);
		todoIconNormal = getActivity().getResources().getDrawable(
				R.drawable.ic_create_todo);

		checklistIconActive = getActivity().getResources().getDrawable(
				R.drawable.ic_create_checklist_active);
		checklistIconNormal = getActivity().getResources().getDrawable(
				R.drawable.ic_create_checklist);

		projectIconActive = getActivity().getResources().getDrawable(
				R.drawable.ic_create_project_active);
		projectIconNormal = getActivity().getResources().getDrawable(
				R.drawable.ic_create_project);

		//initial settings: todoButton is pressed, its icon is active and the other buttons are not pressed and their icons are inactive. 
		isTodoButtonPressed = true;

		todoButton.setCompoundDrawablesWithIntrinsicBounds(null,
				todoIconActive, null, null);

		checklistButton.setCompoundDrawablesWithIntrinsicBounds(
				null, checklistIconNormal, null, null);

		projectButton.setCompoundDrawablesWithIntrinsicBounds(null,
				projectIconNormal, null, null);


		todoButton.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isTodoButtonPressed == false) {
					todoButton.setCompoundDrawablesWithIntrinsicBounds(null,
							todoIconActive, null, null);

					isTodoButtonPressed = true;
					checklistButton.setCompoundDrawablesWithIntrinsicBounds(
							null, checklistIconNormal, null, null);

					projectButton.setCompoundDrawablesWithIntrinsicBounds(null,
							projectIconNormal, null, null);

					isChecklistButtonPressed = false;
					isProjectButtonPressed = false;
				}

				mCallback.onTaskListSelected();

			}
		});

		checklistButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isChecklistButtonPressed == false) {
					checklistButton.setCompoundDrawablesWithIntrinsicBounds(
							null, checklistIconActive, null, null);

					isChecklistButtonPressed = true;
					todoButton.setCompoundDrawablesWithIntrinsicBounds(null,
							todoIconNormal, null, null);

					projectButton.setCompoundDrawablesWithIntrinsicBounds(null,
							projectIconNormal, null, null);

					isTodoButtonPressed = false;
					isProjectButtonPressed = false;
				}

				mCallback.onCheckListSelected();

			}
		});
	}
}
