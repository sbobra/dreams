package com.example.dreams.db.entity;

import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "dreams")
public class Dream {
	@DatabaseField(generatedId = true)
	public int id;
	@ForeignCollectionField
	public ForeignCollection<DreamColor> colors;
	@ForeignCollectionField
	public ForeignCollection<DreamEmotion> emotions;
	@DatabaseField
	public String name;
	@DatabaseField
	public String note;
	@ForeignCollectionField
	public ForeignCollection<DreamAudio> audios;
	@ForeignCollectionField
	public ForeignCollection<DreamTag> tags;
	@DatabaseField
	public Date timestamp;
}
