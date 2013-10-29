package com.example.dreams.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dreams.R;
import com.example.dreams.model.Dream;
import com.example.dreams.model.Sleep;
import com.example.dreams.view.Constants;
import com.example.dreams.view.DreamActivity;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

public class DreamController {

	private DreamActivity view;
	private Sleep mySleep = null;

	public DreamController(DreamActivity activity) {
		this.view = activity;

	}
	
	public void deleteDream() {
		if (mySleep !=null) {
			for (int i = 0; i< mySleep.getDream().getRecordings().size(); i++) {
				int recordingNum = (int) mySleep.getDream().getRecordings().get(i).intValue();
				deleteRecording(recordingNum);
			}
			if (mySleep.getDream()!=null) {
				Dream myDream = mySleep.getDream();
				myDream.destroy(new StackMobModelCallback() {
				    @Override
				    public void success() {
				        // the call succeeded
				    	Log.i("DreamController", "successfully destroyed dream");
				    }
				  
				    @Override
				    public void failure(StackMobException e) {
				    	Log.i("DreamController", e.getMessage());
				    }
				});
			}
			mySleep.destroy(new StackMobModelCallback() {
			    @Override
			    public void success() {
			    	Log.i("DreamController", "successfully destroyed sleep");
			    }
			  
			    @Override
			    public void failure(StackMobException e) {
			    	Log.i("DreamController", e.getMessage());
			    }
			});
		}
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
						mySleep = sleep;
						// Log.i("Dream Activity",
						// "Sleep duration: nothing");
						view.runOnUiThread(new Runnable() {
							public void run() {
								Date startTime = new Date(Long.valueOf(sleep
										.getStartTime()));
								((TextView) view.findViewById(R.id.dream_date))
										.setText("Date: "
												+ startTime.toString());
								float duration = Utils.millisToMins(Long
										.valueOf(sleep.getEndTime())
										- Long.valueOf(sleep.getStartTime()));
								((TextView) view
										.findViewById(R.id.dream_duration))
										.setText("Duration: " + duration
												+ " mins");
								((TextView) view
										.findViewById(R.id.dream_dream_name))
										.setText("Name: "
												+ sleep.getDream().getName());
								String colors = "";
								for (int i = 0; i < sleep.getDream()
										.getColors().size(); i++) {
									colors += Constants.colors[sleep.getDream()
											.getColors().get(i).intValue()]
											+ ", ";
								}
								((TextView) view
										.findViewById(R.id.dream_colors))
										.setText("Colors: " + colors);
								String emotions = "";
								for (int i = 0; i < sleep.getDream()
										.getEmotions().size(); i++) {
									emotions += Constants.emotions[sleep
											.getDream().getEmotions().get(i)
											.intValue()]
											+ ", ";
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

								if (sleep.getDream().getRecordings() != null) {
									List<Double> recordings = sleep.getDream()
											.getRecordings();
									for (int i = 0; i < recordings.size(); i++) {
										Button myButton = new Button(view);
										final int recordingNum = (int) recordings
												.get(i).intValue();
										myButton.setText("Recording " + i
												+ ": file-" + recordingNum);
										myButton.setOnClickListener(new View.OnClickListener() {
											public void onClick(View v) {
												playRecording(recordingNum);
											}
										});
										LinearLayout ll = (LinearLayout) view
												.findViewById(R.id.journal_button_layout);
										LayoutParams lp = new LayoutParams(
												LayoutParams.WRAP_CONTENT,
												LayoutParams.WRAP_CONTENT);
										ll.addView(myButton, lp);
									}
								}
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

	public void playRecording(int n) {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/dreamCatcher/file-" + Constants.getFileName(n) + ".3gp";
		Log.i("NewDreamActivity", "Playing file: " + path);
		// set up MediaPlayer
		MediaPlayer mp = new MediaPlayer();

		try {
			// mp.setDataSource(path + "/" + fileName);
			mp.setDataSource(path);
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void deleteRecording(int n) {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/dreamCatcher/file-" + Constants.getFileName(n) + ".3gp";
		File deleteFile = new File(path);
		deleteFile.delete();
	}
}
