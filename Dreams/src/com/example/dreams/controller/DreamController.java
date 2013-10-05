package com.example.dreams.controller;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.dreams.R;
import com.example.dreams.model.Sleep;
import com.example.dreams.view.DreamActivity;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

public class DreamController {

	private DreamActivity view;

	public DreamController(DreamActivity activity) {
		this.view = activity;

	}

	public void fetchDream() {

		Bundle b = view.getIntent().getExtras();
		final String id = b.getString("id");

		// final Sleep sleep = new Sleep();
		// sleep.setID(id);

		Sleep.query(Sleep.class,
				new StackMobQuery().fieldIsEqualTo("sleep_id", id),
				StackMobOptions.https(true).withDepthOf(2),
				new StackMobQueryCallback<Sleep>() {
					@Override
					public void success(List<Sleep> result) {
						final Sleep sleep = result.get(0);

						Log.i("Dream Activity",
								"Sleep duration: " + sleep.getDuration());
						view.runOnUiThread(new Runnable() {
							public void run() {
								((TextView) view.findViewById(R.id.dream_date))
										.setText("Date: "
												+ sleep.getStartTime());
								((TextView) view
										.findViewById(R.id.dream_duration))
										.setText("Duration: "
												+ sleep.getDuration());
								((TextView) view
										.findViewById(R.id.dream_dream_name))
										.setText("Name: "
												+ sleep.getDream().getName());
								String colors = "";
								for (int i = 0; i < sleep.getDream()
										.getColors().size(); i++) {
									colors += sleep.getDream().getColors()
											.get(i)
											+ " ";
								}
								((TextView) view
										.findViewById(R.id.dream_colors))
										.setText("Colors: " + colors);
								String emotions = "";
								for (int i = 0; i < sleep.getDream()
										.getEmotions().size(); i++) {
									emotions += sleep.getDream().getEmotions()
											.get(i)
											+ " ";
								}
								((TextView) view
										.findViewById(R.id.dream_emotions))
										.setText("Emotions: " + emotions);
								String tags = "";
								for (int i = 0; i < sleep.getDream().getTags()
										.size(); i++) {
									tags += sleep.getDream().getTags().get(i)
											+ " ";
								}
								((TextView) view.findViewById(R.id.dream_tags))
										.setText("Tags: " + tags);
							}

						});
					}

					@Override
					public void failure(StackMobException arg0) {
						// TODO Auto-generated method stub

					}
				});

		// sleep.fetch(new StackMobModelCallback() {
		// @Override
		// public void success() {
		// // the call succeeded
		// runOnUiThread (new Thread(new Runnable() {
		// public void run() {
		//
		//
		// }
		// }));
		//
		// }
		//
		//
		// @Override
		// public void failure(StackMobException arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}
}
