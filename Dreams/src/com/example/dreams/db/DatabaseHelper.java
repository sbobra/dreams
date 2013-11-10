package com.example.dreams.db;

import java.sql.SQLException;
import java.util.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.dreams.db.entity.Dream;
import com.example.dreams.db.entity.DreamAudio;
import com.example.dreams.db.entity.DreamColor;
import com.example.dreams.db.entity.DreamEmotion;
import com.example.dreams.db.entity.DreamTag;
import com.example.dreams.db.entity.Sleep;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "dreams.db";
	private static final int DATABASE_VERSION = 1;
	
	private Dao<Dream, Integer> dreamDao = null;
	private Dao<DreamAudio, Integer> dreamAudioDao = null;
	private Dao<DreamColor, Integer> dreamColorDao = null;
	private Dao<DreamEmotion, Integer> dreamEmotionDao = null;
	private Dao<DreamTag, Integer> dreamTagDao = null;
	private Dao<Sleep, Date> sleepDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(
			SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Dream.class);
			TableUtils.createTable(connectionSource, DreamAudio.class);
			TableUtils.createTable(connectionSource, DreamColor.class);
			TableUtils.createTable(connectionSource, DreamEmotion.class);
			TableUtils.createTable(connectionSource, DreamTag.class);
			TableUtils.createTable(connectionSource, Sleep.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(
			SQLiteDatabase database,
			ConnectionSource connectionSource,
			int oldVersion,
			int newVersion) {
		// do nothing...
	}

	public Dao<Dream, Integer> getDreamDao() throws SQLException {
		if (dreamDao == null) {
			dreamDao = getDao(Dream.class);
		}
		return dreamDao;
	}

	public Dao<DreamAudio, Integer> getDreamAudioDao() throws SQLException {
		if (dreamAudioDao == null) {
			dreamAudioDao = getDao(DreamAudio.class);
		}
		return dreamAudioDao;
	}

	public Dao<DreamColor, Integer> getDreamColorDao() throws SQLException {
		if (dreamColorDao == null) {
			dreamColorDao = getDao(DreamColor.class);
		}
		return dreamColorDao;
	}

	public Dao<DreamEmotion, Integer> getDreamEmotionDao() throws SQLException {
		if (dreamEmotionDao == null) {
			dreamEmotionDao = getDao(DreamEmotion.class);
		}
		return dreamEmotionDao;
	}

	public Dao<DreamTag, Integer> getDreamTagDao() throws SQLException {
		if (dreamTagDao == null) {
			dreamTagDao = getDao(DreamTag.class);
		}
		return dreamTagDao;
	}

	public Dao<Sleep, Date> getSleepDao() throws SQLException {
		if (sleepDao == null) {
			sleepDao = getDao(Sleep.class);
		}
		return sleepDao;
	}
}
