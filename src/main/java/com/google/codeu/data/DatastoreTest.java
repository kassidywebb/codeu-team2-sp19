package com.google.codeu.data;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatastoreTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  } 

 private void doTest() {
	 
   Datastore datastore = new Datastore();
	 
   Event exampleEvent = new Event("Ahmed", "HotPocket Event", "03/29/19",
   		"3:00PM", 4523, "Sunnyvalue, CA", "It will be fun and have lots of food",
   		"some random img string");

   /*Create an event entity using exampleEvent and populate it into datastore by calling     storeEvent*/
     datastore.storeEvent(exampleEvent);
     /* Check if there is 1 event inside the datastore right now*/
  	assertEquals(1, datastore.numberOfEvents());
  	//assertEquals(1, ds.prepare(new Query("Event")).countEntities(withLimit(10)));
  	
  	  /* When getEvents is called an arrayList of Events is created according to the 
  	   * user specified in the parameter. In this case getEvents should create a list
  	   * holding 1 object from user Ahmed.
  	   */
 	assertEquals(1, datastore.getEvents("Ahmed").size());
  	
	Event exampleEvent2 = new Event("Demha", "Orchestra Maze", "03/31/19",
"8:00PM", 8472523, "SanFrancisco, CA", "It will be fun and have lots of music", "a cello     and violion img url");
	datastore.storeEvent(exampleEvent2);
	
	/*getAllEvents returns an arraylist of events regardless of User. 
	 * With the second added event, there should now be two  events in
	 * the list right now.
	 */
  	assertEquals(2, datastore.getAllEvents().size());
  	
  	assertEquals(exampleEvent.getId(), datastore.getIndividualEvent(exampleEvent.getId()).getId());
  	
 }
 @Test //where testing function is called
 public void testInsert1() {
   doTest();
 }

}
