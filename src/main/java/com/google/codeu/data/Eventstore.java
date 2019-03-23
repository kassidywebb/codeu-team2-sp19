package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Eventstore {


	private DatastoreService eventstore;

	public Eventstore() {
		eventstore = DatastoreServiceFactory.getDatastoreService();
	}

	/** Stores the User in Eventstore. */
	public void storeUser(User user) {
		Entity userEntity = new Entity("User",user.getEmail());
		userEntity.setProperty("email",user.getEmail());
		userEntity.setProperty("aboutMe",user.getAboutMe());
		eventstore.put(userEntity);
	}
	/**
	* Returns the User owned by the email address,
	*null if no matching User was found
	*/
	public User getUser(String email) {

		Query query = new Query("User").setFilter(new Query.FilterPredicate("email",FilterOperator.EQUAL, email));
		PreparedQuery results = eventstore.prepare(query);
		Entity userEntity = results.asSingleEntity();

		if(userEntity == null) {
			return null;
		}

	String aboutMe = (String)userEntity.getProperty("aboutMe");
	User user = new User(email, aboutMe);

	return user;
	}

	/** Stores the Event in Datastore. */
	public void storeEvent(Event event) {
		Entity eventEntity = new Entity("Event", event.getId().toString());
		eventEntity.setProperty("user", event.getUser());
		eventEntity.setProperty("text", event.getText());
		eventEntity.setProperty("timestamp", event.getTimestamp());
    	eventEntity.setProperty("recipient", event.getRecipient());
		eventEntity.setProperty("sentimentScore", event.getSentimentScore());
		if(event.getImageUrl() != null) {
  			eventEntity.setProperty("imageUrl", event.getImageUrl());
		}
		eventstore.put(eventEntity);
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
				String text = (String) entity.getProperty("text");
				long timestamp = (long) entity.getProperty("timestamp");
				String recipient = (String) entity.getProperty("recipient");

				/*Before adding sentiment scores as a feature, there were already messages
				 without scores. This sets the old sentiment scores to 0 for old messages
				 */
				float sentimentScore = entity.getProperty("sentimentScore") == null? (float) 0.0 : ((Double) entity.getProperty("sentimentScore")).floatValue();
				
				String imageUrl = (String) entity.getProperty("imageUrl");

				Event event = new Event(id, user, text, timestamp, recipient, sentimentScore, imageUrl);
				events.add(event);
			} catch (Exception e) {
				System.err.println("Error reading message.");
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

	public List<Event> getEvents(String recipient) {
		List<Event> events = new ArrayList<>();

		Query query =
				new Query("Event")
				.setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
				.addSort("timestamp", SortDirection.DESCENDING);
		PreparedQuery results = eventstore.prepare(query);

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
		PreparedQuery results = eventstore.prepare(query);

		saveEventInformation(events, results);

    return events;
  }


  /** Returns the total number of event for all users. */
  	public int getTotalEventCount(){
    	Query query = new Query("Event");
   	 	PreparedQuery results = eventstore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }



}
