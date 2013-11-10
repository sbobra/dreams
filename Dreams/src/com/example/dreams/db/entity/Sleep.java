package com.example.dreams.db.entity;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sleeps")
public class Sleep {
	@DatabaseField(id=true)
	public Date date;
	@DatabaseField
	public long dreamLength;
}
