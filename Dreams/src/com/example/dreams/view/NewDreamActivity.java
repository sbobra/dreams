package com.example.dreams.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.InjectView;
import butterknife.Views;

import com.example.dreams.R;
import com.example.dreams.model.Dream;
import com.example.dreams.model.Sleep;
import com.example.dreams.model.User;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobUser;

public class NewDreamActivity extends Activity {
	public static final String PREFS_NAME = "timeStamps";
	private static final String TAG = "SoundRecordingActivity";
	File audiofile = null;
	private boolean[] checkArray;
	private int mood = -1;
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
	
	public void onMoodClicked(View view) {
		switch (view.getId()) {
		case R.id.mood1:
			mood = 0;
			setMoodSelected(view);
			break;
		case R.id.mood2:
			mood = 1;
			setMoodSelected(view);
			break;
		case R.id.mood3:
			setMoodSelected(view);
			mood = 2;
			break;
		}
	}
	
	public void setMoodSelected(View view) {
		((ImageView) findViewById(R.id.mood1)).setAlpha(0.2f);
		((ImageView) findViewById(R.id.mood2)).setAlpha(0.2f);
		((ImageView) findViewById(R.id.mood3)).setAlpha(0.2f);
		view.setAlpha(1.0f);
	}

	public void onCheckboxClicked(View view) {
		// Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();

		// Check which checkbox was clicked
		switch (view.getId()) {
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

		TextView cancelButton = (TextView) findViewById(R.id.newdream_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		CircleImageView colorPickerButton = (CircleImageView) findViewById(R.id.c1);
		colorPickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showColorDialog(v);
			}
		});
		
		CircleImageView cButton2 = (CircleImageView) findViewById(R.id.c2);
		cButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showColorDialog(v);
			}
		});
		CircleImageView cButton3 = (CircleImageView) findViewById(R.id.c3);
		cButton3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showColorDialog(v);
			}
		});
		CircleImageView cButton4 = (CircleImageView) findViewById(R.id.c4);
		cButton4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showColorDialog(v);
			}
		});
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
				if (mood!=-1)
					emotions.add((double)mood);
//				for (int i = 0; i < 3; i++) {
//					if (checkArray[i]) {
//						emotions.add((double) i);
//					}
//				}
				
				List<Double> colors = new ArrayList<Double>();
				if (((CircleImageView) findViewById(R.id.c1)).color!=-1)
					colors.add((double)((CircleImageView) findViewById(R.id.c1)).color);
				if (((CircleImageView) findViewById(R.id.c2)).color!=-1)
					colors.add((double)((CircleImageView) findViewById(R.id.c2)).color);
				if (((CircleImageView) findViewById(R.id.c3)).color!=-1)
					colors.add((double)((CircleImageView) findViewById(R.id.c3)).color);
				if (((CircleImageView) findViewById(R.id.c4)).color!=-1)
					colors.add((double)((CircleImageView) findViewById(R.id.c4)).color);
				
				String tagsString = ((EditText) findViewById(R.id.dreamTagsText))
						.getText().toString();
				String[] tagsArray = tagsString.split(" ");
				List<String> tags = new ArrayList<String>();
				for (int i = 0; i < tagsArray.length; i++) {
					tags.add(tagsArray[i]);
				}
				final Dream newDream = new Dream(name, note, emotions, colors,
						tags, recordings);

				String bedtime = getLastBedTime();
				final Sleep sleep = new Sleep(bedtime, ""
						+ (System.currentTimeMillis()), newDream);

				Log.i("NewDreamActivity", name);
				Log.i("NewDreamActivity", note);
				Log.i("NewDreamActivity", "num emotions: " + emotions.size());
				Log.i("NewDreamActivity", "num tags: " + tags.size());
				Log.i("NewDreamActivity", "num colors: " + colors.size());
				Log.i("NewDreamActivity",
						"num recordings: " + recordings.size());

				if (StackMob.getStackMob().isLoggedIn()) {
					StackMobUser.getLoggedInUser(User.class,
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
												public void failure(
														StackMobException e) {
													Log.i("NewDreamActivity",
															e.getMessage());
												}

												@Override
												public void success() {
													Log.i("NewDreamActivity",
															"Dream and sleep saved!");
												}
											});
								}
							});
				}

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
	
	
//	public void drawCircle(ImageView img) {
//		Bitmap bmp = Bitmap.createBitmap(20, 20, Bitmap.Config.RGB_565);
//		Canvas c = new Canvas(bmp);
//		Paint paint = new Paint();
//		paint.setColor(Color.BLACK);
//		c.drawCircle(10, 10, 10, paint);
//		img.setBackground(new BitmapDrawable(getResources(), bmp));
//	}
	
	protected void showColorDialog(View v) {
        // TODO Auto-generated method stub
        final ColorDialog dialog = new ColorDialog(v, NewDreamActivity.this);
        dialog.show();
    }
	
	
	public class ColorDialog extends Dialog {
		public CircleImageView selectedCircle = null;
		public CircleImageView parent;
		
		public ColorDialog(View v, Context context) {
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_color_picker);
	        parent = (CircleImageView) v;
	        
//	        ImageView img = (ImageView)dialog.findViewById(R.id.circle);
//	        drawCircle(img);
	        ((CircleImageView) this.findViewById(R.id.circle00)).setColor(getResources().getColor(R.color.yellow));
	        setClick(((CircleImageView) this.findViewById(R.id.circle00)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle01)).setColor(getResources().getColor(R.color.fuchsia));
	        setClick(((CircleImageView) this.findViewById(R.id.circle01)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle02)).setColor(getResources().getColor(R.color.red));
	        setClick(((CircleImageView) this.findViewById(R.id.circle02)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle03)).setColor(getResources().getColor(R.color.silver));
	        setClick(((CircleImageView) this.findViewById(R.id.circle03)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle10)).setColor(getResources().getColor(R.color.gray));
	        setClick(((CircleImageView) this.findViewById(R.id.circle10)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle11)).setColor(getResources().getColor(R.color.olive));
	        setClick(((CircleImageView) this.findViewById(R.id.circle11)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle12)).setColor(getResources().getColor(R.color.purple));
	        setClick(((CircleImageView) this.findViewById(R.id.circle12)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle13)).setColor(getResources().getColor(R.color.maroon));
	        setClick(((CircleImageView) this.findViewById(R.id.circle13)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle20)).setColor(getResources().getColor(R.color.lime));
	        setClick(((CircleImageView) this.findViewById(R.id.circle20)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle21)).setColor(getResources().getColor(R.color.teal));
	        setClick(((CircleImageView) this.findViewById(R.id.circle21)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle22)).setColor(getResources().getColor(R.color.green));
	        setClick(((CircleImageView) this.findViewById(R.id.circle22)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle23)).setColor(getResources().getColor(R.color.aqua));
	        setClick(((CircleImageView) this.findViewById(R.id.circle23)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle30)).setColor(getResources().getColor(R.color.blue));
	        setClick(((CircleImageView) this.findViewById(R.id.circle30)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle31)).setColor(getResources().getColor(R.color.navy));
	        setClick(((CircleImageView) this.findViewById(R.id.circle31)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle32)).setColor(getResources().getColor(R.color.black));
	        setClick(((CircleImageView) this.findViewById(R.id.circle32)));
	        
	        ((CircleImageView) this.findViewById(R.id.circle33)).setColor(getResources().getColor(R.color.orange));
	        setClick(((CircleImageView) this.findViewById(R.id.circle33)));

	        
	        TextView button = (TextView)this.findViewById(R.id.letsgo);    
	        button.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	            	if (selectedCircle!=null) {
		            	int color = selectedCircle.color;
		            	if (color!=-1)
		            		parent.setColor(color);
	            	}
	            	dismiss();
	            }
	        });
		}
		
		public ColorDialog(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			
		}
		
		public void setClick(CircleImageView c) {
			c.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	setOnClick(v);
			    }
			});
		}
		
		public void setOnClick(View v) {
			CircleImageView circle = (CircleImageView) v;
	        // TODO Auto-generated method stub
	    	if (selectedCircle == null) {
	    		circle.toggleSelected();
	    		selectedCircle = (CircleImageView)v;
	    		Log.i("CircleImageView", "no circle selected");
	    	}
	    	else if (selectedCircle.equals(circle)) {
	    		circle.toggleSelected();
	    		selectedCircle = null;
	    		Log.i("CircleImageView", "tapping selected circle");
	    	} else if(!selectedCircle.equals(circle)) {
	    		circle.toggleSelected();
	    		selectedCircle.toggleSelected();
	    		selectedCircle = circle;
	    		Log.i("CircleImageView", "tapping a different circle");
	    	}  
		}
		
		
	}

	
	
}
