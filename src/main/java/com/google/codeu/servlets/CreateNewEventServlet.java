package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.lang.*;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Event;
import com.google.gson.JsonObject;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.images.ImagesServiceFailureException;
import java.util.Map;
import java.util.Enumeration;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
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

    String user = userService.getCurrentUser().getEmail();
    String title = request.getParameter("event-title");
    String location = request.getParameter("location");
    String date = request.getParameter("date");
    String time = request.getParameter("time");
    long timestamp = System.currentTimeMillis();
    String details = request.getParameter("details");
    String host = request.getParameter("host");

    
    Event event = new Event(user, title, date, time, timestamp, location, details, host);
    /*
    setEventImageUrl(request,event);*/
    datastore.storeEvent(event);
    
    response.sendRedirect("/user-page.html?user=" + user);
  }
   private void setEventImageUrl (HttpServletRequest request, Event event) {

     /* This creates a Blobstore instance, then gets the image url(s) which are stored
      in a map of string. Then converts the urls to a list. */
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

    /* Makes sure the list of images is not empty (and image was uploaded),
       then gets the url from Blobstore */
    if(blobKeys != null && !blobKeys.isEmpty()) {
      BlobKey blobKey = blobKeys.get(0);

      final BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
      long size = blobInfo.getSize();
      if(size > 0){
         ImagesService imagesService = ImagesServiceFactory.getImagesService();
         ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
         String imageUrl = imagesService.getServingUrl(options);
         event.setImageUrl(imageUrl);
      } else {
       blobstoreService.delete(blobKey);
      }
    }
  }
}