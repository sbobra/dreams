package com.example.dreams.view;

import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

import com.example.dreams.R;

public class HomeFragment extends Fragment {
	/**
	 * The argument key for the page number this fragment represents.
	 */
	public static final String ARG_PAGE = "page";
	public static final String PREFS_NAME = "timeStamps";
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
	public boolean asleep;

	/**
	 * The fragment's page number, which is set to the argument value for
	 * {@link #ARG_PAGE}.
	 */
	private int mPageNumber;

	public ViewGroup rootView;

	@InjectView(R.id.sleepButton)
	ImageView sleepButton;

	public void createNotification(Context c) {
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(c)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("Wake up!")
		        .setContentText("Touch to record your dream.")
		        .setAutoCancel(true);
		// Creates an explicit intent for an Activity in your app
		
		//Add LAUNCHED_BY_NOTIFICATION boolean
		Intent resultIntent = new Intent(c, NewDreamActivity.class);
		resultIntent.putExtra("LAUNCHED_BY_NOTIFICATION", true);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.from(c);
		//TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(NewDreamActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.getNotification());
		//mNotificationManager.notify(1, mBuilder.build());
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
		rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_home, container, false);
		Views.inject(this, this.rootView);
		asleep = false;
		return rootView;
	}

	public void onRefresh() {
		// refresh
	}
	

	@Override
	public void onResume() {
		super.onResume();
		onRefresh();
	}

	public void saveTime() {
		SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("bedtime", ""+System.currentTimeMillis());

		// Commit the edits!
		editor.commit();
	}

	@OnClick(R.id.sleepButton)
	public void sleepButtonClick() {
		if (!asleep) {
			sleepButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_wakeup));
			saveTime();
			createNotification(rootView.getContext());
		} else {
			sleepButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_bedtime));
			startActivity(new Intent(rootView.getContext(), NewDreamActivity.class));
		}
		asleep = !asleep;
	}
}
