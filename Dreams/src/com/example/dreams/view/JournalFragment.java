package com.example.dreams.view;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

import com.example.dreams.R;
import com.example.dreams.Utils;
import com.example.dreams.db.DatabaseHelper;
import com.example.dreams.db.entity.Sleep;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class JournalFragment extends Fragment {
	/**
	 * The argument key for the page number this fragment represents.
	 */
	public static final String ARG_PAGE = "page";

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

	/**
	 * The fragment's page number, which is set to the argument value for
	 * {@link #ARG_PAGE}.
	 */
	private int mPageNumber;

	@InjectView(R.id.refreshButton)
	Button refreshButton;

	DatabaseHelper dbHelper;
	public ViewGroup rootView;

	List<Sleep> sleepList;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_journal,
				container, false);
		Views.inject(this, rootView);
		dbHelper = ((OrmLiteBaseActivity<DatabaseHelper>) getActivity()).getHelper();
		return rootView;
	}

	@OnClick(R.id.refreshButton)
	public void onRefresh() {
		clearTable();
		// request dream data
		sleepList = dbHelper.getSleepDao().queryForAll();
	}

	@Override
	public void onResume() {
		super.onResume();
		onRefresh();
	}

	public void populate(final Sleep sleep) {
		if (rootView != null) {
			if (sleep.getDream() != null) {
				TableLayout table = (TableLayout) rootView
						.findViewById(R.id.tableLayout);
				// Inflate your row "template" and fill out the fields.
				final TableRow row = (TableRow) LayoutInflater.from(
						rootView.getContext()).inflate(
						R.layout.row_journal_table, null);
				Date startTime = new Date(Long.valueOf(sleep.getStartTime()));
				((TextView) row.findViewById(R.id.journal_row_date))
						.setText(startTime.toString());
				float duration = Utils.millisToMins(Long.valueOf(sleep
						.getEndTime()) - Long.valueOf(sleep.getStartTime()));
				Log.i("JournalFragment", "Duration: " + duration);
				// ((TextView)
				// row.findViewById(R.id.journal_row_duration)).setText("Duration: "
				// + duration + " mins");
				if (sleep.getDream().getName() != null)
					((TextView) row.findViewById(R.id.journal_row_dream_name))
							.setText(sleep.getDream().getName());
				String colors = "";
				for (int i = 0; i < sleep.getDream().getColors().size(); i++) {
					colors += Constants.colors[sleep.getDream().getColors()
							.get(i).intValue()]
							+ ", ";
				}
				Log.i("JournalFragment", "Colors: " + colors);
				// ((TextView)
				// row.findViewById(R.id.journal_row_colors)).setText("Colors: "
				// + colors);
				String emotions = "";
				for (int i = 0; i < sleep.getDream().getEmotions().size(); i++) {
					emotions += Constants.emotions[sleep.getDream()
							.getEmotions().get(i).intValue()]
							+ ", ";
				}
				if (sleep.getDream().getEmotions().size() > 0) {
					int emotionId = sleep.getDream().getEmotions().get(0)
							.intValue();
					((ImageView) row.findViewById(R.id.journal_row_emotion))
							.setImageDrawable(getResources().getDrawable(
									Constants.emotionFadedIDs[emotionId]));
				}
				Log.i("JournalFragment", "Emotions: " + emotions);
				// ((TextView)
				// row.findViewById(R.id.journal_row_emotions)).setText("Emotions: "
				// + emotions);
				String tags = "";
				for (int i = 0; i < sleep.getDream().getTags().size(); i++) {
					tags += sleep.getDream().getTags().get(i) + " ";
				}
				Log.i("JournalFragment", "Tags: " + tags);
				// ((TextView)
				// row.findViewById(R.id.journal_row_tags)).setText("Tags: "
				// + tags);

				row.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.i("ListFragment",
								"Dream pressed! Dream: " + sleep.getID());
						Intent intent = new Intent(rootView.getContext(),
								DreamActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("id", sleep.getID());
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});

				table.addView(row);
				TableRow spacer = (TableRow) LayoutInflater.from(
						rootView.getContext()).inflate(R.layout.spacer_row,
						null);
				table.addView(spacer);

				table.requestLayout();
			}
		}
	}

	public void testRow() {
		TableLayout table = (TableLayout) rootView
				.findViewById(R.id.tableLayout);
		// Inflate your row "template" and fill out the fields.
		final TableRow row = (TableRow) LayoutInflater.from(
				rootView.getContext())
				.inflate(R.layout.row_journal_table, null);
		table.addView(row);

		table.requestLayout();
	}

	public void updateTable() {
		for (int i = 0; i < sleepList.length; i++) {
			final Sleep s = sleepList[i];
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					populate(s);
				}
			});

		}
	}
}