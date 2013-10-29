package com.example.dreams.model;

import java.util.List;

import com.stackmob.sdk.model.StackMobModel;

public class Dream extends StackMobModel {

	private String note;
	private String name;
	private List<Double> emotions;
	private List<Double> colors;
	private List<String> tags;
	private List<Double> recordings;

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

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return this.note;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public List<Double> getEmotions() {
		return this.emotions;
	}
	
	public List<Double> getRecordings() {
		return this.recordings;
	}
	
	public List<Double> getColors() {
		return this.colors;
	}
	
	public List<String> getTags() {
		return this.tags;
	}
}