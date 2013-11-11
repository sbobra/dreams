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
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "dreams.db";
	private static final int DATABASE_VERSION = 1;
	
	private RuntimeExceptionDao<Dream, Integer> dreamDao = null;
	private RuntimeExceptionDao<DreamAudio, Integer> dreamAudioDao = null;
	private RuntimeExceptionDao<DreamColor, Integer> dreamColorDao = null;
	private RuntimeExceptionDao<DreamEmotion, Integer> dreamEmotionDao = null;
	private RuntimeExceptionDao<DreamTag, Integer> dreamTagDao = null;
	private RuntimeExceptionDao<Sleep, Date> sleepDao = null;

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

	public RuntimeExceptionDao<Dream, Integer> getDreamDao() {
		if (dreamDao == null) {
			dreamDao = getRuntimeExceptionDao(Dream.class);
		}
		return dreamDao;
	}

	public RuntimeExceptionDao<DreamAudio, Integer> getDreamAudioDao() {
		if (dreamAudioDao == null) {
			dreamAudioDao = getRuntimeExceptionDao(DreamAudio.class);
		}
		return dreamAudioDao;
	}

	public RuntimeExceptionDao<DreamColor, Integer> getDreamColorDao() {
		if (dreamColorDao == null) {
			dreamColorDao = getRuntimeExceptionDao(DreamColor.class);
		}
		return dreamColorDao;
	}

	public RuntimeExceptionDao<DreamEmotion, Integer> getDreamEmotionDao() {
		if (dreamEmotionDao == null) {
			dreamEmotionDao = getRuntimeExceptionDao(DreamEmotion.class);
		}
		return dreamEmotionDao;
	}

	public RuntimeExceptionDao<DreamTag, Integer> getDreamTagDao() {
		if (dreamTagDao == null) {
			dreamTagDao = getRuntimeExceptionDao(DreamTag.class);
		}
		return dreamTagDao;
	}

	public RuntimeExceptionDao<Sleep, Date> getSleepDao() {
		if (sleepDao == null) {
			sleepDao = getRuntimeExceptionDao(Sleep.class);
		}
		return sleepDao;
	}
}
