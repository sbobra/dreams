package com.example.dreams.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="dreamTags")
public class DreamTag {
	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField(canBeNull = false, foreign = true)
	public Dream dream;
	@DatabaseField(canBeNull = false)
	public String tag;
}
