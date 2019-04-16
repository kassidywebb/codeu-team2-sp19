package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.lang.*;
import java.util.*;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Comment;
import com.google.gson.JsonObject;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

@WebServlet("/CommentServlet")
 public class CommentServlet extends HttpServlet{


  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)

          throws IOException {

      response.setContentType("application/json");
      String eventId = request.getParameter("event");
      List<Comment> comments = datastore.getEventComments(UUID.fromString(eventId));
      Gson gson = new Gson();
      String json = gson.toJson(comments);

      response.getOutputStream().println(json);
  }

@Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    /*
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }*/

    if(request.getParameter("comment") == null){
        response.sendRedirect("/event.html?event=" + request.getParameter("id"));
    }

    String user = userService.getCurrentUser().getEmail();
    String commenttext = request.getParameter("comment");
    String id = request.getParameter("id");

    
    Comment comment = new Comment(id,user,commenttext);
    datastore.storeComment(comment);
    
    response.sendRedirect("/");
  }
 }