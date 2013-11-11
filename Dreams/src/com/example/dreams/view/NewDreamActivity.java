package com.example.dreams.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.InjectView;
import butterknife.Views;

import com.example.dreams.R;
import com.example.dreams.db.DatabaseHelper;
import com.example.dreams.db.entity.Dream;
import com.example.dreams.db.entity.DreamAudio;
import com.example.dreams.db.entity.DreamColor;
import com.example.dreams.db.entity.DreamColor.Color;
import com.example.dreams.db.entity.DreamEmotion;
import com.example.dreams.db.entity.DreamTag;
import com.example.dreams.db.entity.Sleep;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class NewDreamActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	public static final String PREFS_NAME = "timeStamps";
	private static final String TAG = "SoundRecordingActivity";
	File audiofile = null;
	private boolean[] checkArray;

	private boolean[] colorArray;
	private TextView createButton;
	@InjectView(R.id.play)
	View playButton;
	MediaRecorder recorder;
	List<Double> recordings = new ArrayList<Double>();

	@InjectView(R.id.recordButton)
	ImageView recordButton;
	
	@InjectView(R.id.audioRecording)
	View playAudio;

	protected void addRecordingToMediaLibrary(String newName, String newPath) {

		ContentValues values = new ContentValues(4);
		long current = System.currentTimeMillis();
		values.put(MediaColumns.TITLE, "audio" + newName);
		values.put(MediaColumns.DATE_ADDED, (int) (current / 1000));
		values.put(MediaColumns.MIME_TYPE, "audio/3gpp");
		values.put(MediaColumns.DATA, newPath);
		Log.i("NewDreamActivity", audiofile.getAbsolutePath());
		ContentResolver contentResolver = getContentResolver();

		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);

		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
		Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
		Log.i("NewDreamActivity", newPath);

	}

	public void clearTempDir() {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/dreamCatcher/temp";
		File directory = new File(path);
		if (directory.isDirectory()) {
			for (File child : directory.listFiles()) {
				child.delete();
			}
		}
	}

	public int getFileNumber() {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/dreamCatcher";
		File directory = new File(path);
		directory.mkdirs();
		File[] files = directory.listFiles();
		Arrays.sort(files);
		if (files == null) {
			// nothing in dreamcatcher or dreamcatcher doesn't exist
			Log.i("NewDreamActivity", "directory is null");
			return 0;
		}
		if (files.length == 0) {
			Log.i("NewDreamActivity", "nothing in directory");
			return 0;
		}
		if (files.length == 1) {
			Log.i("NewDreamActivity", "only temp folder");
			return 0;
		}
		String lastFileName = files[files.length - 2].getName();
		// if (lastFileName.equals("temp")) {
		// // only temp folder
		// Log.i("NewDreamActivity", "only temp folder");
		// return 0;
		// }
		try {
			String parsed1 = (lastFileName.split("-"))[1];
			Log.i("NewDreamActivity", parsed1);
			String parsed2 = parsed1.substring(0, parsed1.length() - 4);
			Log.i("NewDreamActivity", parsed2);
			int num = Integer.valueOf(parsed2);
			return num + 1;
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.i("NewDreamActivity", "Parsing error");
			return -1;
		}
	}

	public String getLastBedTime() {
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return (settings.getString("bedtime", ""));
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

	@Override
	public void onBackPressed() {
		Bundle extras = getIntent().getExtras();

		boolean launchedFromNotif = false;

		if (extras !=null && extras.containsKey("LAUNCHED_BY_NOTIFICATION")) {
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_newdream);

		Views.inject(this);

		checkArray = new boolean[3];
		colorArray = new boolean[3];

		createButton = (TextView) findViewById(R.id.newdream_save);
		createButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveRecording();
				String name = ((EditText) findViewById(R.id.dreamNameText))
						.getText().toString();
				String note = ((EditText) findViewById(R.id.dreamNotesText))
						.getText().toString();
				List<Double> emotions = new ArrayList<Double>();
				for (int i = 0; i < 3; i++) {
					if (checkArray[i]) {
						emotions.add((double) i);
					}
				}
				List<Double> colors = new ArrayList<Double>();
				for (int i = 0; i < 3; i++) {
					if (colorArray[i]) {
						colors.add((double) i);
					}
				}
				String tagsString = ((EditText) findViewById(R.id.dreamTagsText))
						.getText().toString();
				String[] tagsArray = tagsString.split(" ");
				List<String> tags = new ArrayList<String>();
				for (int i = 0; i < tagsArray.length; i++) {
					tags.add(tagsArray[i]);
				}
				
				Sleep sleep = new Sleep();
				Calendar cal = Calendar.getInstance();
				cal.getTime();
				cal.setTimeZone(TimeZone.getTimeZone("UTC"));
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				sleep.date = cal.getTime();
				getHelper().getSleepDao().createIfNotExists(sleep);
				// TODO: set the length of the sleep
				
				Dream dream = new Dream();
				dream.name = name;
				dream.note = note;
				dream.sleep = sleep;
				
				getHelper().getDreamDao().create(dream);
				
				for (Double emotion : emotions) {
					DreamEmotion dreamEmotion = new DreamEmotion();
					dreamEmotion.emotion = emotion.toString(); // TODO: write the emotion in as a real string
					dream.emotions.add(dreamEmotion);
				}

				for (Double color : colors) {
					DreamColor dreamColor= new DreamColor();
					dreamColor.color = Color.BLUE; // TODO: write a proper colour here
					dream.colors.add(dreamColor);
				}
				
				for (String tag : tags) {
					DreamTag dreamTag = new DreamTag();
					dreamTag.tag = tag;
					dream.tags.add(dreamTag);
				}

				for (Double recording : recordings) {
					DreamAudio dreamAudio = new DreamAudio();
					dreamAudio.audio = recording.toString(); // TODO: write a proper audio reference here
					dream.audios.add(dreamAudio);
					
				}

				Log.i("NewDreamActivity", name);
				Log.i("NewDreamActivity", note);
				Log.i("NewDreamActivity", "num emotions: " + emotions.size());
				Log.i("NewDreamActivity", "num tags: " + tags.size());
				Log.i("NewDreamActivity", "num colors: " + colors.size());
				Log.i("NewDreamActivity",
						"num recordings: " + recordings.size());

				onBackPressed();
			}
		});
	}

	public void playRecording(View view) {
		// String path = Environment.getExternalStorageDirectory().toString()
		// + "/dreamCatcher";
		String fileName = audiofile.getAbsolutePath();
		Log.i("NewDreamActivity", "Playing file: " + fileName);
		// set up MediaPlayer
		MediaPlayer mp = new MediaPlayer();

		try {
			// mp.setDataSource(path + "/" + fileName);
			mp.setDataSource(fileName);
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveRecording() {
		if (audiofile != null) {
			Log.i("NewDreamActivity", "Old name: " + audiofile.getName());
			int fileNum = getFileNumber();
			recordings.add((double) fileNum);
			String fileName = Constants.getFileName(fileNum);
			String newName = "file-" + fileName + ".3gp";
			Log.i("NewDreamActivity", "New name: " + newName);
			String path = Environment.getExternalStorageDirectory().toString()
					+ "/dreamCatcher/" + newName;
			File sampleDir = new File(path);
			audiofile.renameTo(sampleDir);
			addRecordingToMediaLibrary(newName, path);
		}
	}

	public void startRecording(View view) throws IOException {

		recordButton.setImageDrawable(getResources().getDrawable(R.drawable.stop));
		recordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopRecording(v);
			}
		});

		clearTempDir();
		if (mediaAvailable()) {
			// File sampleDir = Environment.getExternalStorageDirectory();
			String root = Environment.getExternalStorageDirectory().toString();
			File sampleDir = new File(root + "/dreamCatcher/temp");
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
		recorder.stop();
		recorder.release();

		recordButton.setImageDrawable(getResources().getDrawable(R.drawable.record));
		recordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					startRecording(v);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		playAudio.setVisibility(View.VISIBLE);
	}
}
