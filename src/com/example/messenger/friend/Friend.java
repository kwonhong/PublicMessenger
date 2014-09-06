package com.example.messenger.friend;

import android.graphics.Bitmap;

public class Friend {
	private String name;
	private Bitmap picture;
	private String status;
	
	public Friend(String name, String status) {
		this.name = name;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
