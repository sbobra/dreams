package com.example.dreams.view;

import java.io.File;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

import com.example.dreams.R;
import com.example.dreams.Utils;
import com.example.dreams.model.Dream;
import com.example.dreams.model.Sleep;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobModel;

public class DreamActivity extends Activity {
	// @InjectView(R.id.journal_button_layout)
	// LinearLayout layoutJournalButtons;
	private Sleep mySleep;
	// @InjectView(R.id.dream_colors)
	// TextView textDreamColors;
	@InjectView(R.id.dream_date)
	TextView textDreamDate;
	// @InjectView(R.id.dream_duration)
	// TextView textDreamDuration;
	// @InjectView(R.id.dream_emotions)
	// TextView textDreamEmotion;
	@InjectView(R.id.dream_dream_name)
	TextView textDreamName;
	@InjectView(R.id.dream_tags)
	TextView textDreamTags;
	@InjectView(R.id.audio_name)
	TextView audioName;
	@InjectView(R.id.play)
	ImageView audioPlay;
	@InjectView(R.id.audioRecording)
	View audioRecording;
	@InjectView(R.id.dream_note)
	TextView textDreamNote;
	@InjectView(R.id.dream_note_more)
	TextView textDreamNoteMore;

	@OnClick(R.id.dream_note_more)
	public void showMoreNote() {
		if (textDreamNoteMore.getText().equals("See more...")) {
			textDreamNoteMore.setText("See less...");
			textDreamNote.setText(mySleep.getDream().getNote());
		} else {
			textDreamNoteMore.setText("See more...");
			String shortNote = mySleep.getDream().getNote().substring(0, 100)
					+ "...";
			textDreamNote.setText(shortNote);
		}
	}

	// @OnClick(R.id.deleteButton)
	public void deleteDream() {
		if (mySleep != null) {
			for (int i = 0; i < mySleep.getDream().getRecordings().size(); i++) {
				int recordingNum = mySleep.getDream().getRecordings().get(i)
						.intValue();
				deleteRecording(recordingNum);
			}
			if (mySleep.getDream() != null) {
				Dream myDream = mySleep.getDream();
				myDream.destroy(new StackMobModelCallback() {
					@Override
					public void failure(StackMobException e) {
						Log.i("DreamController", e.getMessage());
					}

					@Override
					public void success() {
						// the call succeeded
						Log.i("DreamController", "successfully destroyed dream");
					}
				});
			}
			mySleep.destroy(new StackMobModelCallback() {
				@Override
				public void failure(StackMobException e) {
					Log.i("DreamController", e.getMessage());
				}

				@Override
				public void success() {
					Log.i("DreamController", "successfully destroyed sleep");
				}
			});
		}
		finish();
	}

	public void deleteRecording(int n) {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/dreamCatcher/file-" + Constants.getFileName(n) + ".3gp";
		File deleteFile = new File(path);
		deleteFile.delete();
	}

	@OnClick(R.id.dream_cancel)
	public void done() {
		finish();
	}

	public void setEmotion(int emotionId) {
		((ImageView) findViewById(R.id.mood1)).setAlpha(0.2f);
		((ImageView) findViewById(R.id.mood2)).setAlpha(0.2f);
		((ImageView) findViewById(R.id.mood3)).setAlpha(0.2f);

		int emotionImageId = Constants.emotionIDs[emotionId];
		switch(emotionImageId) {
		case R.drawable.ic_happy_black:
			((ImageView) findViewById(R.id.mood1)).setAlpha(1f);
			break;
		case R.drawable.ic_meh_black:
			((ImageView) findViewById(R.id.mood2)).setAlpha(1f);
			break;
		case R.drawable.ic_sad_black:
			((ImageView) findViewById(R.id.mood3)).setAlpha(1f);
			break;
		}
	}

	public void fetchDream() {

		Bundle b = getIntent().getExtras();
		final String id = b.getString("id");

		// final Sleep sleep = new Sleep();
		// sleep.setID(id);

		StackMobModel.query(Sleep.class,
				new StackMobQuery().fieldIsEqualTo("sleep_id", id),
				StackMobOptions.https(true).withDepthOf(2),
				new StackMobQueryCallback<Sleep>() {
					@Override
					public void failure(StackMobException arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void success(List<Sleep> result) {
						final Sleep sleep = result.get(0);
						mySleep = sleep;
						// Log.i("Dream Activity",
						// "Sleep duration: nothing");
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Date startTime = new Date(Long.valueOf(sleep
										.getStartTime()));
								textDreamDate.setText(startTime.toString());
								float duration = Utils.millisToMins(Long
										.valueOf(sleep.getEndTime())
										- Long.valueOf(sleep.getStartTime()));
								// textDreamDuration.setText("Duration: " +
								// duration
								// + " mins");
								if (sleep.getDream().getNote() != null) {
									String shortNote = "";
									if (sleep.getDream().getNote().length() > 100) {
										shortNote = sleep.getDream().getNote()
												.substring(0, 100)
												+ "...";
										textDreamNoteMore
												.setVisibility(View.VISIBLE);
									} else {
										shortNote = sleep.getDream().getNote();
									}
									textDreamNote.setText(shortNote);
								}
								textDreamName.setText(sleep.getDream()
										.getName());
								String colors = "";
								for (int i = 0; i < sleep.getDream()
										.getColors().size(); i++) {
									colors += Constants.colors[sleep.getDream()
											.getColors().get(i).intValue()]
											+ ", ";
								}
								// textDreamColors.setText("Colors: " + colors);
								String emotions = "";
								for (int i = 0; i < sleep.getDream()
										.getEmotions().size(); i++) {
									emotions += Constants.emotions[sleep
											.getDream().getEmotions().get(i)
											.intValue()]
											+ ", ";
								}
								// textDreamEmotion.setText("Emotions: " +
								// emotions);
								String tags = "";
								for (int i = 0; i < sleep.getDream().getTags()
										.size(); i++) {
									tags += sleep.getDream().getTags().get(i)
											+ " ";
								}
								textDreamTags.setText("Tags: " + tags);

								if (sleep.getDream().getRecordings() != null) {
									List<Double> recordings = sleep.getDream()
											.getRecordings();
									for (int i = 0; i < recordings.size(); i++) {
										if (i == 0) {
											audioRecording
													.setVisibility(View.VISIBLE);
											// Button myButton = new
											// Button(DreamActivity.this);

											final int recordingNum = recordings
													.get(i).intValue();
											audioName
													.setText("Audio Recording "
															+ i + ": file-"
															+ recordingNum);
											audioPlay
													.setOnClickListener(new View.OnClickListener() {
														@Override
														public void onClick(
																View v) {
															playRecording(recordingNum);
														}
													});
											// layoutJournalButtons
											// .addView(
											// myButton,
											// new LayoutParams(
											// LayoutParams.WRAP_CONTENT,
											// LayoutParams.WRAP_CONTENT));
										}
									}
								}
							}

						});

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_dream_new);
		Views.inject(this);

		fetchDream();
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
}
