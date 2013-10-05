package com.example.dreams.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.dreams.R;
import com.example.dreams.controller.JournalFragmentController;
import com.example.dreams.model.Sleep;

public class JournalFragment extends Fragment {
	/**
	 * The argument key for the page number this fragment represents.
	 */
	public static final String ARG_PAGE = "page";
	public JournalFragmentController controller = null;
	public ViewGroup rootView;
	public Button refreshButton;

	/**
	 * The fragment's page number, which is set to the argument value for
	 * {@link #ARG_PAGE}.
	 */
	private int mPageNumber;

	/**
	 * Factory method for this fragment class. Constructs a new fragment for the
	 * given page number.
	 */
	public static JournalFragment create(int pageNumber) {
		JournalFragment fragment = new JournalFragment();
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
			controller = new JournalFragmentController(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout containing a title and body text.
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_journal, container, false);

		this.rootView = rootView;
		
		refreshButton = (Button) rootView.findViewById(R.id.refreshButton);
		refreshButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				controller.onRefresh();
			}
		});

		return rootView;
	}
	
	public void testRow() {
		TableLayout table = (TableLayout) rootView
				.findViewById(R.id.tableLayout);
		// Inflate your row "template" and fill out the fields.
		final TableRow row = (TableRow) LayoutInflater.from(
				rootView.getContext()).inflate(R.layout.row_journal_table, null);
		table.addView(row);

		table.requestLayout();
	}
	
	public void populate(final Sleep sleep) {
		if (rootView != null) {
			TableLayout table = (TableLayout) rootView
					.findViewById(R.id.tableLayout);
			// Inflate your row "template" and fill out the fields.
			final TableRow row = (TableRow) LayoutInflater.from(
					rootView.getContext()).inflate(R.layout.row_journal_table, null);
			((TextView) row.findViewById(R.id.journal_row_date)).setText("Date: "+sleep.getStartTime());
			((TextView) row.findViewById(R.id.journal_row_duration)).setText("Duration: "+sleep.getDuration());
			((TextView) row.findViewById(R.id.journal_row_dream_name)).setText("Name: "+sleep.getDream().getName());
			String colors = "";
			for (int i = 0; i<sleep.getDream().getColors().size();i++) {
				colors+=sleep.getDream().getColors().get(i) + " ";
			}
			((TextView) row.findViewById(R.id.journal_row_colors)).setText("Colors: "
					+ colors);
			String emotions = "";
			for (int i = 0; i<sleep.getDream().getEmotions().size();i++) {
				emotions+=sleep.getDream().getEmotions().get(i) + " ";
			}
			((TextView) row.findViewById(R.id.journal_row_emotions)).setText("Emotions: "
					+ emotions);
			String tags = "";
			for (int i = 0; i<sleep.getDream().getTags().size();i++) {
				tags+=sleep.getDream().getTags().get(i) + " ";
			}
			((TextView) row.findViewById(R.id.journal_row_tags)).setText("Tags: "
					+ tags);

			row.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Log.i("ListFragment", "Dream pressed! Dream: " + sleep.getID());
					Intent intent = new Intent(rootView.getContext(), DreamActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("id", sleep.getID());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});

			table.addView(row);
//			TableRow spacer = (TableRow) LayoutInflater.from(
//					rootView.getContext()).inflate(R.layout.spacer_row, null);
//			table.addView(spacer);

			table.requestLayout();
		}
	}
	
	public void clearTable() {
		if (rootView != null) {
			TableLayout table = (TableLayout) rootView
					.findViewById(R.id.tableLayout);
			table.removeAllViews();
		}

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
		if (controller == null)
			controller = new JournalFragmentController(this);
		controller.onRefresh();
	}
}