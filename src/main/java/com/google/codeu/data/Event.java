package com.google.codeu.data;

import java.util.UUID;

/** A single message posted by a user. */
public class Event {

  private UUID id;
  private String eventid;
  private String user;
  private String title;
  private String date;
  private String time;
  private long timestamp;
  private String location;
  private String details;
  private String hostedBy;
  private String imageUrl;

  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text} content. Generates a
   * random ID and uses the current system time for the creation time.
   */
  public Event(){
     this.id = null;
     this.eventid = null;
     this.user = null;
     this.title = null;
     this.date = null;
     this.time = null;
     this.timestamp =  (Long) null;
     this.location = null;
     this.details = null;
     this.hostedBy = null;
     this.imageUrl = null;
   
  }  
   public Event(String eventid,
		   		String user,
                  String title,
                  String date,
                  String time,
                  long timestamp,
                  String location,
                  String details,
                  String hostedBy,
                  String imageUrl) {
     this(UUID.randomUUID(), eventid, user, title, date, time,
    		 System.currentTimeMillis(), location, details, hostedBy, imageUrl);
   }

   public Event(UUID id, 
		   		  String eventid,
                  String user,
                  String title,
                  String date,
                  String time,
                  long timestamp,
                  String location,
                  String details,
                  String hostedBy,
                  String imageUrl) {
     this.id = id;
     this.eventid = eventid;
     this.user = user;
     this.title = title;
     this.date = date;
     this.time = time;
     this.timestamp = timestamp;
     this.location = location;
     this.details = details;
     this.hostedBy = hostedBy;
     this.imageUrl = imageUrl;
   }

  public void setImageUrl(String imageUrl) {
     this.imageUrl = imageUrl;
     return;
  }

  public UUID getId() {
    return id;
  }

  public String getEventId() {
    return id.toString();
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
  
  public String getHostedBy() {
	  return hostedBy;
  }
  
  public String getImageUrl() {
    return imageUrl;
  }
  

}
