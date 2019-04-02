package com.google.codeu.data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.FetchOptions.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

//Run this test twice to prove we're not leaking any state across tests.
 private void doTest() {
   DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
   assertEquals(0, ds.prepare(new Query("yam")).countEntities(FetchOptions.Builder.withLimit(10)));
   ds.put(new Entity("yam"));
   ds.put(new Entity("yam"));
   assertEquals(2, ds.prepare(new Query("yam")).countEntities(FetchOptions.Builder.withLimit(10)));
 }

 @Test
 public void testInsert1() {
   doTest();
 }

 @Test
 public void testInsert2() {
   doTest();
 }
/*
  // Run this test twice to prove we're not leaking any state across tests.
	@Test
    void doTest() {
	   
		 DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		 
    Event exampleEvent = new Event("Ahmed", "HotPocket Event", "03/29/19",
    		"3:00PM", 4523, "Sunnyvalue, CA", "It will be fun and have lots of food",
    		"some random img string");
    
    Entity eventEntity = new Entity("Event", exampleEvent.getId().toString());
	eventEntity.setProperty("user", exampleEvent.getUser());
	eventEntity.setProperty("title", exampleEvent.getTitle());
	eventEntity.setProperty("date", exampleEvent.getDate());
	eventEntity.setProperty("time", exampleEvent.getTime());
	eventEntity.setProperty("timestamp", exampleEvent.getTimestamp());
	eventEntity.setProperty("location", exampleEvent.getLocation());
	eventEntity.setProperty("details", exampleEvent.getLocation());
	if(exampleEvent.getImageUrl() != null) {
			eventEntity.setProperty("imageUrl", exampleEvent.getImageUrl());
	}
	 ds.put(eventEntity); 
    
    List<Event> exampleList = new ArrayList<Event>(); ;
    exampleList.add(exampleEvent);
   ((Datastore) ds).storeEvent(exampleEvent);
 //  assertEquals(4,2+2);
   // List<Event> eventList = ((Datastore) ds).getEvents("Ahmed");
    //assertEquals(exampleList,eventList);
    
    assertEquals(2, ds.prepare(new Query("Event")).countEntities(FetchOptions.Builder.withLimit(10)));
   // ds.put(new Entity("yam"));
    //ds.put(new Entity("yam"));
    //assertEquals(2, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
    
  }
*/
}