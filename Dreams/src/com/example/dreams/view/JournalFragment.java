package com.example.dreams.view;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

import com.example.dreams.R;
import com.example.dreams.db.DatabaseHelper;
import com.example.dreams.db.entity.Dream;
import com.example.dreams.db.entity.DreamColor;
import com.example.dreams.db.entity.DreamEmotion;
import com.example.dreams.db.entity.DreamTag;
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

	public void populate(Sleep sleep) {
		if (rootView != null) {
			for (final Dream dream : sleep.dreams) {
				TableLayout table = (TableLayout) rootView
						.findViewById(R.id.tableLayout);
				// Inflate your row "template" and fill out the fields.
				final TableRow row = (TableRow) LayoutInflater.from(
						rootView.getContext()).inflate(
						R.layout.row_journal_table, null);
				((TextView) row.findViewById(R.id.journal_row_date))
					.setText(sleep.date.toString());
				float duration = sleep.duration;
				Log.i("JournalFragment", "Duration: " + duration);
				// ((TextView)
				// row.findViewById(R.id.journal_row_duration)).setText("Duration: "
				// + duration + " mins");
				if (dream.name != null)
					((TextView) row.findViewById(R.id.journal_row_dream_name))
							.setText(dream.name);

				StringBuilder colorBuilder = new StringBuilder();
				for (DreamColor color : dream.colors) {
					colorBuilder.append(color.color.name());
					colorBuilder.append(", ");
				}
				Log.i("JournalFragment", "Colors: " + colorBuilder);
				// ((TextView) row.findViewById(R.id.journal_row_colors)).setText("Colors: " + colors);
				StringBuilder emotionBuilder = new StringBuilder();
				for (DreamEmotion emotion : dream.emotions) {
					emotionBuilder.append(emotion.emotion);
					emotionBuilder.append(", ");
				}
				Log.i("JournalFragment", "Emotions: " + emotionBuilder);
				// ((TextView) row.findViewById(R.id.journal_row_emotions)).setText("Emotions: " + emotions);
				StringBuilder tagBuilder = new StringBuilder();
				for (DreamTag tag : dream.tags) {
					tagBuilder.append(tag.tag);
					tagBuilder.append(", ");
				}

				Log.i("JournalFragment", "Tags: " + tagBuilder);
				// ((TextView) row.findViewById(R.id.journal_row_tags)).setText("Tags: " + tags);

				row.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.i("ListFragment",
								"Dream pressed! Dream: " + dream.id);
						DreamActivity.startActivity(getActivity(), dream);
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
		for (final Sleep sleep : sleepList) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					populate(sleep);
				}
			});
		}
	}
}
