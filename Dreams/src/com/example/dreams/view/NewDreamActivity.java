package com.example.dreams.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dreams.R;
import com.example.dreams.controller.NewDreamController;
import com.example.dreams.model.Dream;
import com.example.dreams.model.Sleep;
import com.example.dreams.model.User;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

public class NewDreamActivity extends Activity {

	private NewDreamController controller;
	private boolean[] checkArray;
	private boolean[] colorArray;
	private Button createButton;

	MediaRecorder recorder;
	File audiofile = null;
	private static final String TAG = "SoundRecordingActivity";
	private View startButton;
	private View stopButton;
	private View playButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_newdream);
		controller = new NewDreamController(this);
		checkArray = new boolean[3];
		colorArray = new boolean[3];

		startButton = findViewById(R.id.start);
		stopButton = findViewById(R.id.stop);
		playButton = findViewById(R.id.play);

		createButton = (Button) findViewById(R.id.createButton);
		createButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String name = ((EditText) findViewById(R.id.dreamNameText))
						.getText().toString();
				String note = ((EditText) findViewById(R.id.dreamNotesText))
						.getText().toString();
				List<Integer> emotions = new ArrayList<Integer>();
				for (int i = 0; i < 3; i++) {
					if (checkArray[i]) {
						emotions.add(i);
					}
				}
				List<Integer> colors = new ArrayList<Integer>();
				for (int i = 0; i < 3; i++) {
					if (colorArray[i]) {
						colors.add(i);
					}
				}
				String tagsString = ((EditText) findViewById(R.id.dreamTagsText))
						.getText().toString();
				String[] tagsArray = tagsString.split(" ");
				List<String> tags = new ArrayList<String>();
				for (int i = 0; i < tagsArray.length; i++) {
					tags.add(tagsArray[i]);
				}
				final Dream newDream = new Dream(name, note, emotions, colors,
						tags);
				final Sleep sleep = new Sleep((int) System.currentTimeMillis(),
						(int) (System.currentTimeMillis() + 5), 5, newDream);

				Log.i("NewDreamActivity", name);
				Log.i("NewDreamActivity", note);
				Log.i("NewDreamActivity", "num emotions: " + emotions.size());
				Log.i("NewDreamActivity", "num tags: " + tags.size());
				Log.i("NewDreamActivity", "num colors: " + colors.size());

				if (StackMob.getStackMob().isLoggedIn()) {
					User.getLoggedInUser(User.class,
							new StackMobQueryCallback<User>() {
								@Override
								public void failure(StackMobException arg0) {
									// continue with login process
									Log.i("NewDreamActivity",
											"No logged in user found!");

								}

								@Override
								public void success(List<User> arg0) {
									// TODO Auto-generated method stub
									User loggedInUser = arg0.get(0);
									loggedInUser.getSleepList().add(sleep);
									loggedInUser.getDreamList().add(sleep);
									Log.i("NewDreamActivity", "Found user");
									loggedInUser.save(
											StackMobOptions.depthOf(3),
											new StackMobModelCallback() {
												@Override
												public void success() {
													Log.i("NewDreamActivity",
															"Dream and sleep saved!");
												}

												@Override
												public void failure(
														StackMobException e) {
													Log.i("NewDreamActivity",
															e.getMessage());
												}
											});
								}
							});
				}

				finish();
			}
		});
	}

	public void startRecording(View view) throws IOException {

		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		if (mediaAvailable()) {
			// File sampleDir = Environment.getExternalStorageDirectory();
			String root = Environment.getExternalStorageDirectory().toString();
			File sampleDir = new File(root + "/saved_audio");
			sampleDir.mkdirs();
			try {
				audiofile = File.createTempFile("sound", ".3gp", sampleDir);
			} catch (IOException e) {
				Log.e(TAG, "sdcard access error");
				return;
			}
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(audiofile.getAbsolutePath());
			recorder.prepare();
			recorder.start();
		} else {
			Log.i("NewDreamActivity", "Out of memory!");
		}
	}

	public void stopRecording(View view) {
		startButton.setEnabled(true);
		playButton.setEnabled(true);
		stopButton.setEnabled(false);
		recorder.stop();
		recorder.release();
		addRecordingToMediaLibrary();
	}

	public void playRecording(View view) {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/saved_audio";
		String fileName = "sound";
		// set up MediaPlayer
		MediaPlayer mp = new MediaPlayer();

		try {
			mp.setDataSource(path + "/" + fileName);
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean mediaAvailable() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		return (mExternalStorageAvailable && mExternalStorageWriteable);
	}

	protected void addRecordingToMediaLibrary() {

		ContentValues values = new ContentValues(4);
		long current = System.currentTimeMillis();
		values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
		values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
		values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
		ContentResolver contentResolver = getContentResolver();

		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);

		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
		Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();

	}

	private void SaveImage(Bitmap finalBitmap) {

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/saved_images");
		myDir.mkdirs();
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "Image-" + n + ".jpg";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onCheckboxClicked(View view) {
		// Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();

		// Check which checkbox was clicked
		switch (view.getId()) {
		case R.id.checkBox1:
			checkArray[0] = checked;
			break;
		case R.id.checkBox2:
			checkArray[1] = checked;
			break;
		case R.id.checkBox3:
			checkArray[2] = checked;
			break;
		case R.id.color1:
			colorArray[0] = checked;
			break;
		case R.id.color2:
			colorArray[1] = checked;
			break;
		case R.id.color3:
			colorArray[2] = checked;
			break;
		}
	}

	@Override
	public void onBackPressed() {
		Bundle extras = getIntent().getExtras();

		boolean launchedFromNotif = false;

		if (extras.containsKey("LAUNCHED_BY_NOTIFICATION")) {
			launchedFromNotif = extras.getBoolean("LAUNCHED_BY_NOTIFICATION");
		}

		if (launchedFromNotif) {
			// Launched from notification, handle as special case
			Intent intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
		} else {
			super.onBackPressed();
		}
	}

}
