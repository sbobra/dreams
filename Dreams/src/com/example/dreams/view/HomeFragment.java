package com.example.dreams.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dreams.R;
import com.example.dreams.controller.HomeFragmentController;

public class HomeFragment extends Fragment {
	/**
	 * The argument key for the page number this fragment represents.
	 */
	public static final String ARG_PAGE = "page";
	public HomeFragmentController controller = null;
	public ViewGroup rootView;
	public Button sleepButton;
	public boolean asleep;

	/**
	 * The fragment's page number, which is set to the argument value for
	 * {@link #ARG_PAGE}.
	 */
	private int mPageNumber;

	/**
	 * Factory method for this fragment class. Constructs a new fragment for the
	 * given page number.
	 */
	public static HomeFragment create(int pageNumber) {
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
		if (controller == null)
			controller = new HomeFragmentController(this);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout containing a title and body text.
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_home, container, false);

		this.rootView = rootView;
		
		asleep = false;
		sleepButton = (Button) rootView.findViewById(R.id.sleepButton);
		sleepButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!asleep) {
					sleepButton.setText("Wake up");
				} else {
					sleepButton.setText("Go to sleep");
					startActivity(new Intent(v.getContext(), NewDreamActivity.class));
				}
				asleep = !asleep;
			}
		});
		
		return rootView;
	}
	
	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		return mPageNumber;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (controller != null)
			controller.onRefresh();
		else
			controller = new HomeFragmentController(this);
		controller.onRefresh();
	}
}