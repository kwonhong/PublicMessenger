package com.example.messengerproject.service;

import android.graphics.Bitmap;

public class Member {
	private String name;
	private Bitmap imagesoure_id;
	private int id;
	private String position;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Bitmap getImagesoure_id() {
		return imagesoure_id;
	}
	public void setImagesoure_id(Bitmap imagesoure_id) {
		this.imagesoure_id = imagesoure_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

}
