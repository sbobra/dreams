package com.example.dreams.model;

import java.util.List;

import com.stackmob.sdk.model.StackMobModel;

public class Dream extends StackMobModel {

	private List<Double> colors;
	private List<Double> emotions;
	private String name;
	private String note;
	private List<Double> recordings;
	private List<String> tags;

	public Dream(String note, String name, List<Double> emotions,
			List<Double> colors, List<String> tags, List<Double> recordings) {
		super(Dream.class);
		this.note = note;
		this.name = name;
		this.emotions = emotions;
		this.colors = colors;
		this.tags = tags;
		this.recordings = recordings;
	}

	public List<Double> getColors() {
		return this.colors;
	}

	public List<Double> getEmotions() {
		return this.emotions;
	}
	
	public String getName() {
		return this.name;
	}

	public String getNote() {
		return this.note;
	}

	public List<Double> getRecordings() {
		return this.recordings;
	}
	
	public List<String> getTags() {
		return this.tags;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
}