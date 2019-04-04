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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.FetchOptions.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Provides access to the data stored in Datastore. */
public class Datastore {


	private DatastoreService datastore;

	public Datastore() {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	/** Stores the User in Datastore. */
	public void storeUser(User user) {
		Entity userEntity = new Entity("User",user.getEmail());
		userEntity.setProperty("email",user.getEmail());
		userEntity.setProperty("aboutMe",user.getAboutMe());
		datastore.put(userEntity);
	}
	/**
	* Returns the User owned by the email address,
	*null if no matching User was found
	*/
	public User getUser(String email) {

		Query query = new Query("User").setFilter(new Query.FilterPredicate("email",FilterOperator.EQUAL, email));
		PreparedQuery results = datastore.prepare(query);
		Entity userEntity = results.asSingleEntity();

		if(userEntity == null) {
			return null;
		}

	String aboutMe = (String)userEntity.getProperty("aboutMe");
	User user = new User(email, aboutMe);

	return user;
	}

	/** Stores the Message in Datastore. */
	public void storeMessage(Message message) {
		Entity messageEntity = new Entity("Message", message.getId().toString());
		messageEntity.setProperty("user", message.getUser());
		messageEntity.setProperty("text", message.getText());
		messageEntity.setProperty("timestamp", message.getTimestamp());
    	messageEntity.setProperty("recipient", message.getRecipient());
  		messageEntity.setProperty("imageUrl", message.getImageUrl());

		datastore.put(messageEntity);
	}

	/**
	 * This method takes in an arraylist and query of all messages.
	 * It then loops through the results query and saves the information
	 * to a message variable to inserted into the message arraylist.
	 *
	 * @param messages message arraylist
	 * @param results the individual messages to be parsed
	 * @param the user of the message
	 */


	public void saveMessageInformation(List<Message> messages, PreparedQuery results) {

		for (Entity entity : results.asIterable()) {
			try {
				String idString = entity.getKey().getName();
				UUID id = UUID.fromString(idString);
				String user = (String) entity.getProperty("user");
				String text = (String) entity.getProperty("text");
				long timestamp = (long) entity.getProperty("timestamp");
				String recipient = (String) entity.getProperty("recipient");

				Message message = new Message(id, user, text, timestamp, recipient);
        
				String imageUrl = (String) entity.getProperty("imageUrl");
				if (imageUrl != null) {
					message.setImageUrl(imageUrl);
				}
				messages.add(message);
			} catch (Exception e) {
				System.err.println("Error reading message.");
				System.err.println(entity.toString());
				e.printStackTrace();
			}
		}
	}


	/**
	 * Gets messages posted by a specific user.
	 *
	 * @return a list of messages posted by the user, or empty list if user has never posted a
	 *     message. List is sorted by time descending. This is now dealt in saveMessageInformation()
	 */

	public List<Message> getMessages(String recipient) {
		List<Message> messages = new ArrayList<>();

		Query query =
				new Query("Message")
				.setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
				.addSort("timestamp", SortDirection.DESCENDING);
		PreparedQuery results = datastore.prepare(query);

		saveMessageInformation(messages, results);

		return messages;
	}

	/**
	 * Similar to the getMessages function, fetches all the messages regardless
	 * of user. Uses helper function saveMessageInformation
	 * of user.
	 * @return
	 */
	public List<Message> getAllMessages(){
		List<Message> messages = new ArrayList<>();

		Query query = new Query("Message")
				.addSort("timestamp", SortDirection.DESCENDING);
		PreparedQuery results = datastore.prepare(query);

		saveMessageInformation(messages, results);

    return messages;
  }


  /** Returns the total number of messages for all users. */
  	public int getTotalMessageCount(){
    	Query query = new Query("Message");
   	 	PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  /*Returns the largest message*/
  public String largestText(PreparedQuery results) {
		int iLargest = 0;
		String s = "";
		for (Entity entity : results.asIterable()) {
			try {
				String characters = (String) entity.getProperty("text");
				if(characters.length() >= iLargest){
					iLargest = characters.length();
					s = characters;
				}
			} catch (Exception e) {
				System.err.println("Error reading message.");
				System.err.println(entity.toString());
				e.printStackTrace();
			}
		}
		return s;
	}

  public String getLargestMessage(){
	Query query = new Query("Message");
		PreparedQuery results = datastore.prepare(query);
		String text = largestText(results);
	return text;
	}

  
  
  
	/** Stores the Event in Datastore. **/
	public void storeEvent(Event event) {
		Entity eventEntity = new Entity("Event", event.getId().toString());
		eventEntity.setProperty("user", event.getUser());
		eventEntity.setProperty("title", event.getTitle());
		eventEntity.setProperty("date", event.getDate());
		eventEntity.setProperty("time", event.getTime());
		eventEntity.setProperty("timestamp", event.getTimestamp());
    	eventEntity.setProperty("location", event.getLocation());
    	eventEntity.setProperty("details", event.getLocation());
		if(event.getImageUrl() != null) {
  			eventEntity.setProperty("imageUrl", event.getImageUrl());
		}
		datastore.put(eventEntity);
	}

	/**
	 * This method takes in an arraylist and query of all events.
	 * It then loops through the results query and saves the information
	 * to a event variable to inserted into the message arraylist.
	 *
	 * @param events message arraylist
	 * @param results the individual events to be parsed
	 * @param the user/creactor of the event
	 */


	public void saveEventInformation(List<Event> events, PreparedQuery results) {

		for (Entity entity : results.asIterable()) {
			try {
				String idString = entity.getKey().getName();
				UUID id = UUID.fromString(idString);
				String user = (String) entity.getProperty("user");
				String title = (String) entity.getProperty("title");
				String date = (String) entity.getProperty("date");
				String time = (String) entity.getProperty("time");
				long timestamp = (long) entity.getProperty("timestamp");
				String location = (String) entity.getProperty("location");
				String details = (String) entity.getProperty("details");

				/*Before adding sentiment scores as a feature, there were already messages
				 without scores. This sets the old sentiment scores to 0 for old messages
				 */

				String imageUrl = (String) entity.getProperty("imageUrl");

				Event event = new Event(id, user, title, date, time, timestamp, location, details, imageUrl);
				events.add(event);
			} catch (Exception e) {
				System.err.println("Error reading events.");
				System.err.println(entity.toString());
				e.printStackTrace();
			}
		}
	}


	/**
	 * Gets events posted by a specific user.
	 *
	 * @return a list of events posted by the user, or empty list if user has never posted a
	 *     message. List is sorted by time descending. This is now dealt in saveEventInformation()
	 */

	public List<Event> getEvents(String user) {
		List<Event> events = new ArrayList<>();

		Query query =
				new Query("Event")
				.setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
				.addSort("timestamp", SortDirection.DESCENDING);
		PreparedQuery results = datastore.prepare(query);

		saveEventInformation(events, results);

		return events;
	}

	/**
	 * Similar to the getEvents function, fetches all the events regardless
	 * of user. Uses helper function saveEventInformation
	 * of user.
	 * @return
	 */
	public List<Event> getAllEvents(){
		List<Event> events = new ArrayList<>();

		Query query = new Query("Event")
				.addSort("timestamp", SortDirection.DESCENDING);
		PreparedQuery results = datastore.prepare(query);

		saveEventInformation(events, results);

    return events;
  }


  /**This function gives a Eventby reference given a query and a empty Event as a parameter**/
  
  public Event saveIndividualEvent(PreparedQuery results) {
	
		Entity entity = results.asSingleEntity();

		String idString = entity.getKey().getName();
		UUID id = UUID.fromString(idString);
		String user = (String) entity.getProperty("user");
		String title = (String) entity.getProperty("title");
		String date = (String) entity.getProperty("date");
		String time = (String) entity.getProperty("time");
		long timestamp = (long) entity.getProperty("timestamp");
		String location = (String) entity.getProperty("location");
		String details = (String) entity.getProperty("details");		
		String imageUrl = (String) entity.getProperty("imageUrl");

		Event event = new Event(id, user, title, date, time, timestamp,location, details, imageUrl);
		return event;
  }
		
	
	/**This function returns one Event given a specific ID**/
  public Event getIndividualEvent(UUID id) {
	
	Query query =
	new Query("Event")
	.setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, id.toString()));

		PreparedQuery results = datastore.prepare(query);

		Event event = saveIndividualEvent(results);

		return event;
	}

}
