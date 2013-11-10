package com.example.dreams.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "dreamColors")
public class DreamColor {
	public static enum Color {
		RED, GREEN, BLUE
	}

	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField(canBeNull = false, foreign = true)
	public Dream dream;
	@DatabaseField(canBeNull = false)
	public Color color;
}
