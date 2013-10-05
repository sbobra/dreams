package com.example.dreams.model;

import java.util.List;

import com.stackmob.sdk.model.StackMobModel;

public class Dream extends StackMobModel {

	private String note;
	private String name;
	private List<Integer> emotions;
	private List<Integer> colors;
	private List<String> tags;

	public Dream(String note, String name, List<Integer> emotions,
			List<Integer> colors, List<String> tags) {
		super(Dream.class);
		this.note = note;
		this.name = name;
		this.emotions = emotions;
		this.colors = colors;
		this.tags = tags;
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

	public List<Integer> getEmotions() {
		return this.emotions;
	}
	
	public List<Integer> getColors() {
		return this.emotions;
	}
	
	public List<String> getTags() {
		return this.tags;
	}
}