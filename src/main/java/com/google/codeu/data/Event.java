package com.google.codeu.data;

import java.util.UUID;

/** A single message posted by a user. */
public class Event {

	private UUID id;
	private String user;
	private String title;
	private String date;
	private String time;
	private long timestamp;
	private String location;
	private String details;
	private String imageUrl;
	private String sID;

	/**
	 * Constructs a new {@link Message} posted by {@code user} with {@code text} content. Generates a
	 * random ID and uses the current system time for the creation time.
	 */
	public Event(){
		this.id = null;
		this.user = null;
		this.title = null;
		this.date = null;
		this.time = null;
		this.timestamp =  0;
		this.location = null;
		this.details = null;
		this.imageUrl = null;

	}  
	public Event(String user,
			String title,
			String date,
			String time,
			long timestamp,
			String location,
			String details,
			String imageUrl) {
		this(UUID.randomUUID(), user, title, date, time, System.currentTimeMillis(), location, details, imageUrl);
	}



	public Event(UUID id, 
			String user,
			String title,
			String date,
			String time,
			long timestamp,
			String location,
			String details,
			String imageUrl) {
		this.id = id;
		this.user = user;
		this.title = title;
		this.date = date;
		this.time = time;
		this.timestamp = timestamp;
		this.location = location;
		this.details = details;
		this.imageUrl = imageUrl;
	}
	
	public Event(String sId, 
			String user,
			String title,
			String date,
			String time,
			long timestamp,
			String location,
			String details,
			String imageUrl) {
		this.sID = sId;
		this.user = user;
		this.title = title;
		this.date = date;
		this.time = time;
		this.timestamp = timestamp;
		this.location = location;
		this.details = details;
		this.imageUrl = imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
		return;
	}

	public UUID getId() {
		return id;
	}
	public String getsID() {
		return sID;
	}

	public String getUser() {
		return user;
	}

	public String getTitle() {
		return title;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getLocation() {
		return location;
	}

	public String getDetails() {
		return details;
	}

	public String getImageUrl() {
		return imageUrl;
	}


}
