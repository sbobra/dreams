package com.example.dreams.view;

import com.example.dreams.R;
import com.example.dreams.R.layout;
import com.example.dreams.R.menu;
import com.stackmob.android.sdk.common.StackMobAndroid;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class HomeActivity extends Activity {

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_PAGES = 3;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	private ImageView i1, i2, i3;
	private ImageView newGoal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);

		i1 = (ImageView) findViewById(R.id.imageView1);
		i2 = (ImageView) findViewById(R.id.imageView2);
		i3 = (ImageView) findViewById(R.id.imageView3);

		newGoal = (ImageView) findViewById(R.id.newGoalButton);
		newGoal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//do something
//				startActivity(new Intent(v.getContext(), GoalActivity1.class));
//				finish();
			}
		});

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(1);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				//((OnRefreshListener)((ScreenSlidePagerAdapter)mPagerAdapter).getItem(position)).onRefresh();
				switch (position) {
				case 0:
					i1.setBackgroundColor(Color.DKGRAY);
					i2.setBackgroundColor(Color.WHITE);
					i3.setBackgroundColor(Color.WHITE);
					break;
				case 1:
					i2.setBackgroundColor(Color.DKGRAY);
					i3.setBackgroundColor(Color.WHITE);
					i1.setBackgroundColor(Color.WHITE);
					break;
				case 2:
					i3.setBackgroundColor(Color.DKGRAY);
					i1.setBackgroundColor(Color.WHITE);
					i2.setBackgroundColor(Color.WHITE);
					break;
				}
				// When changing pages, reset the action bar actions since they
				// are dependent
				// on which page is currently active. An alternative approach is
				// to have each
				// fragment expose actions itself (rather than the activity
				// exposing actions),
				// but for simplicity, the activity provides the actions in this
				// sample.

				invalidateOptionsMenu();
			}
		});
	}

	/**
	 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment}
	 * objects, in sequence.
	 */
	public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return getFragment(position);
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}

		public Fragment getFragment(int position) {
			switch (position) {
			case 0:
				return MetricsFragment.create(position);
			case 1:
				return HomeFragment.create(position);
			case 2:
				return JournalFragment.create(position);
			}
			return null;
		}
	}
	
	public void showPopup(View v) {
	    PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.popup_menu_main, popup.getMenu());
	    popup.show();
	}
    
}
