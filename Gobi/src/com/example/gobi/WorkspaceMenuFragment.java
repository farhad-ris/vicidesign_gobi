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


public class WorkspaceMenuFragment extends Fragment {



	private OnWorkspaceClickedListener mCallback;
	private Button personalWorkspace;
	private Button workWorkspace;
	private Button uniWorkspace;
	private Button playWorkspace;

	private boolean isPersonalSelected = true;
	private boolean isWorkSelected;
	private boolean isUniSelected;
	private boolean isPlaySelected;

	private Drawable personalIconActive;
	private Drawable personalIconNormal;
	private Drawable workIconActive;
	private Drawable workIconNormal;
	private Drawable uniIconActive;
	private Drawable uniIconNormal;
	private Drawable playIconActive;
	private Drawable playIconNormal; 

	public interface OnWorkspaceClickedListener {
		public void onPersonalClicked();
		public void onWorkClicked();
		public void onUniClicked();
		public void onPlayClicked();
	}

	public WorkspaceMenuFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.workspace, container, false);

		personalWorkspace = (Button) view.findViewById(R.id.workspaceMenuIcon_Personal);
		workWorkspace = (Button) view.findViewById(R.id.workspaceMenuIcon_Work);
		uniWorkspace = (Button) view.findViewById(R.id.workspaceMenuIcon_Uni);
		playWorkspace = (Button) view.findViewById(R.id.workspaceMenuIcon_Play);
		return view;

	}

	//	public interface OnTouchOutsideFragmentListener {
	//		public void onTouch();
	//	}
	//
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnWorkspaceClickedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must " +
					"implement OnWorkspaceClickedListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//get normal icon of every workspace
		//get active icon of every workspace
		personalIconActive = getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace_icon3_orange);//uni->1
		personalIconNormal= getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace_icon3);

		workIconActive= getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace_icon9_orange);//personal->3
		workIconNormal= getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace_icon9);

		uniIconActive= getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace_icon1_orange);//play->5
		uniIconNormal= getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace_icon1);

		playIconActive= getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace_icon5_orange);//work->9
		playIconNormal= getActivity().getResources().getDrawable(
				R.drawable.ic_action_workspace_icon5);


		personalWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
				personalIconActive, null, null);
		personalWorkspace.setTextColor(Color.parseColor("#ff9900"));






		personalWorkspace.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isPersonalSelected) {
					//change all icons
					personalWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							personalIconActive, null, null);
					personalWorkspace.setTextColor(Color.parseColor("#ff9900"));

					workWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							workIconNormal, null, null);
					workWorkspace.setTextColor(Color.parseColor("#999999"));

					uniWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							uniIconNormal, null, null);
					uniWorkspace.setTextColor(Color.parseColor("#999999"));

					playWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							playIconNormal, null, null);
					playWorkspace.setTextColor(Color.parseColor("#999999"));

					//change all booleans
					isPersonalSelected = true;
					isPlaySelected = false;
					isWorkSelected = false;
					isUniSelected = false;


				}
				mCallback.onPersonalClicked();

			}
		});

		workWorkspace.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isWorkSelected) {


					workWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							workIconActive, null, null);
					workWorkspace.setTextColor(Color.parseColor("#ff9900"));

					personalWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							personalIconNormal, null, null);
					personalWorkspace.setTextColor(Color.parseColor("#999999"));

					uniWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							uniIconNormal, null, null);
					uniWorkspace.setTextColor(Color.parseColor("#999999"));

					playWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							playIconNormal, null, null);
					playWorkspace.setTextColor(Color.parseColor("#999999"));

					//change all booleans
					isWorkSelected = true;
					isPersonalSelected = false;
					isPlaySelected = false;
					isUniSelected = false;
				}
				mCallback.onWorkClicked();

			}
		});

		uniWorkspace.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isUniSelected) {

					uniWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							uniIconActive, null, null);
					uniWorkspace.setTextColor(Color.parseColor("#ff9900"));

					personalWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							personalIconNormal, null, null);
					personalWorkspace.setTextColor(Color.parseColor("#999999"));

					playWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							playIconNormal, null, null);
					playWorkspace.setTextColor(Color.parseColor("#999999"));

					workWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							workIconNormal, null, null);
					workWorkspace.setTextColor(Color.parseColor("#999999"));

					//change all booleans
					isUniSelected = true;
					isPersonalSelected = false;
					isPlaySelected = false;
					isWorkSelected = false;
				}
				mCallback.onUniClicked();

			}
		});

		playWorkspace.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isPlaySelected) {

					playWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							playIconActive, null, null);
					playWorkspace.setTextColor(Color.parseColor("#ff9900"));

					workWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							workIconNormal, null, null);
					workWorkspace.setTextColor(Color.parseColor("#999999"));

					uniWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							uniIconNormal, null, null);
					uniWorkspace.setTextColor(Color.parseColor("#999999"));

					personalWorkspace.setCompoundDrawablesWithIntrinsicBounds(null,
							personalIconNormal, null, null);
					personalWorkspace.setTextColor(Color.parseColor("#999999"));

					//change all booleans		
					isPlaySelected = true;
					isPersonalSelected = false;
					isWorkSelected = false;
					isUniSelected = false;
				}
				mCallback.onPlayClicked();

			}
		});
	}


}
