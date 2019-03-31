package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.gson.JsonObject;

/**
 * Handles fetching site statistics.
 */
@WebServlet("/CreateNewEventpage")
 public class CreateNewEventServlet extends HttpServlet{


  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
  *Gets Information from user
  */
  @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String user = request.getParameter("user");
    String recipient = request.getParameter("recipient");

    if (user == null || user.equals("")) {
      response.getWriter().println("[]");
      return;
    }
}
@Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String title = request.getParameter("Title");
    String location = request.getParameter("Location");
    String date = request.getParameter("Date");
    String time = request.getParameter("Time");
    String details = request.getParameter("Details");
    
    Event event = new Event(user, title, date, time, location, details, imageUrl);
    datastore.storeEvent(event);

    response.sendRedirect("/user-page.html?user=" + title);
  }