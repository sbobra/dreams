package com.example.dreams.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import butterknife.InjectView;
import butterknife.Views;

import com.example.dreams.R;
import com.example.dreams.model.State;
import com.example.dreams.model.User;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

public class HomeActivity extends Activity implements PopupMenu.OnMenuItemClickListener{

	/**
	 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment}
	 * objects, in sequence.
	 */
	public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(android.app.FragmentManager fragmentManager) {
			super(fragmentManager);
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

		@Override
		public Fragment getItem(int position) {
			return getFragment(position);
		}
	}

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_PAGES = 3;

	@InjectView(R.id.imageView1)
	ImageView i1;

	@InjectView(R.id.imageView2)
	ImageView i2;
	@InjectView(R.id.imageView3)
	ImageView i3;
	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	@InjectView(R.id.pager)
	ViewPager mPager;
	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;
	
	

	@InjectView(R.id.newGoalButton)
	ImageView newGoal;

	public void logout() {
		User user = new User(State.getInstance().getUsername(), State
				.getInstance().getPassword());
		user.logout(new StackMobModelCallback() {
			@Override
			public void failure(StackMobException arg0) {
				Log.i("HomeController", arg0.getMessage());
			}

			@Override
			public void success() {
				//TODO: logout on shared preferences
				Log.i("HomeController", "Successful logout!");
				startActivity(new Intent(HomeActivity.this, LoginActivity.class));
				finish();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);
		Views.inject(this);

		// Instantiate a ViewPager and a PagerAdapter.
		mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(1);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// ((OnRefreshListener)((ScreenSlidePagerAdapter)mPagerAdapter).getItem(position)).onRefresh();
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
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.log_out:
			logout();
			return true;
		default:
			return false;
		}
	}


	public void showPopup(View v) {
		PopupMenu popup = new PopupMenu(this, v);
		popup.setOnMenuItemClickListener(this);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.popup_menu_main, popup.getMenu());
		popup.show();
	}
}
