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
  public void doGet(HttpServletRequest request, HttpServletResponse response)
  throws IOException{

  response.setContentType("application/json");

  String eventTitle = request.getParameter("Event Title");
  String date = request.getParameter("Date (mm/dd/yy)");
  String time = request.getParameter("Time");
  String location = request.getParameter("Location");
  String details = request.getParameter("Details");

}
}