
package com.google.codeu.servlets;

import java.io.IOException;
import java.util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Event;
import com.google.gson.JsonObject;

@WebServlet("/IndividualEvent")
public class EventOneServlet extends HttpServlet{

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
      response.setContentType("application/json");
      /** Request value of the event id**/
      String id = request.getParameter("event");
      if (id == null) {
        // Request is invalid, return empty array
        response.getWriter().println("[]");
        return;
      }
      UUID uid = UUID.fromString(id);
      Event event = datastore.getIndividualEvent(uid);
      
      String userauthor = event.getUser();
      String title = event.getTitle();
      String date = event.getDate();
      String time = event.getTime();
      long timestamp = event.getTimestamp();
      String location = event.getLocation();
      String details = event.getDetais();
      String imageurl = event.getImageUrl();
      
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("user", userauthor);
      jsonObject.addProperty("title", title);
      jsonObject.addProperty("date", date);
      jsonObject.addProperty("time",time);
      jsonObject.addProperty("timestamp",timestamp);
      jsonObject.addProperty("location",location);
      jsonObject.addProperty("details",details);
      jsonObject.addProperty("imageUrl",imageurl);

      response.getOutputStream().println(jsonObject.toString());
  }
}