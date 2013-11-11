package com.example.dreams.view;

import java.io.File;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

import com.example.dreams.R;
import com.example.dreams.db.DatabaseHelper;
import com.example.dreams.db.entity.Dream;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class DreamActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	@InjectView(R.id.journal_button_layout)
	LinearLayout layoutJournalButtons;
	private Dream dream;
	@InjectView(R.id.dream_colors)
	TextView textDreamColors;
	@InjectView(R.id.dream_date)
	TextView textDreamDate;
	@InjectView(R.id.dream_duration)
	TextView textDreamDuration;
	@InjectView(R.id.dream_emotions)
	TextView textDreamEmotion;
	@InjectView(R.id.dream_dream_name)
	TextView textDreamName;
	@InjectView(R.id.dream_tags)
	TextView textDreamTags;
	
	@OnClick(R.id.deleteButton)
	public void deleteDream() {
		if (dream != null) {
			getHelper().getDreamAudioDao().delete(dream.audios);
			getHelper().getDreamColorDao().delete(dream.colors);
			getHelper().getDreamEmotionDao().delete(dream.emotions);
			getHelper().getDreamTagDao().delete(dream.tags);
			getHelper().getDreamDao().delete(dream);
		}
		finish();
	}

	public void deleteRecording(int n) {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/dreamCatcher/file-" + Constants.getFileName(n) + ".3gp";
		File deleteFile = new File(path);
		deleteFile.delete();
	}

	@OnClick(R.id.doneButton)
	public void done() {
		finish();
	}

	public void fetchDream() {

		Bundle b = getIntent().getExtras();
		int id = b.getInt("id");
		getHelper().getDreamDao().queryForId(id);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_dream);
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
