/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import java.util.UUID;

/** A single message posted by a user. */
public class Comment {

	private UUID id;
	private String eventId;
	private String user;
	private String text;
	private long timestamp;
	private String imageUrl;
	

	/**
	 * Constructs a new {@link Comment} posted by {@code user} with {@code text} content. Generates a
	 * random ID and uses the current system time for the creation time. Added new imageUrl parameter
	 * and set function.
	 */

	public Comment(){
		this.id = null;
		this.eventId = null;
		this.user = null;
		this.text = null;
		this.timestamp = (Long) null;
		this.imageUrl = null;
		

	}

	public Comment(String eventId, String user, String text) {
		this(UUID.randomUUID(), eventId, user, text, System.currentTimeMillis());
	}
	//Make the eventID as first, but also have a seperate ID for the comment*/

	public Comment(UUID id, String eventId, String user, String text, long timestamp) {
		this.id = id;
		this.eventId = eventId;
		this.user = user;
		this.text = text;
		this.timestamp = timestamp;
		this.imageUrl = "";
		
	}

	public void setImageUrl(String url) {
		this.imageUrl = url;
	}
	public void setEventId(String id) {
		this.eventId = id;
	}

	public UUID getId() {
		return id;
	}

	public String getUser() {
		return user;
	}

	public String getText() {
		return text;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getEventId(){
		return eventId;
	}

	public String getImageUrl(){
		return this.imageUrl;
	}

}
