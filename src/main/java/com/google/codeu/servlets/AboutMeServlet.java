package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Event;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;

/**
 * Handles fetching and saving user data.
 */
@WebServlet("/about")
public class AboutMeServlet extends HttpServlet {

  private Datastore datastore;

 @Override
 public void init() {
  datastore = new Datastore();
 }

 /**
  * Responds with the "about me" section for a particular user.
  */
 @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException {

  response.setContentType("text/html");

  String user = request.getParameter("user");

  if(user == null || user.equals("")) {
   // Request is invalid, return empty response
   return;
  }

  User userData = datastore.getUser(user);

  if(userData == null || userData.getAboutMe() == null){
    return;
  }

  response.getOutputStream().println(userData.getAboutMe());
  //some code to get profile pic
 }

 @Override
 public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws IOException {

  UserService userService = UserServiceFactory.getUserService();
  if (!userService.isUserLoggedIn()) {
   response.sendRedirect("/index.html");
   return;
  }

  String userEmail = userService.getCurrentUser().getEmail();
  String aboutMe = request.getParameter("about-me");
  String name = request.getParameter("name");

  User user = new User(userEmail, aboutMe, name);
  //setProfilePic(request, user);
  datastore.storeUser(user);


  response.sendRedirect("/user-page.html?user=" + userEmail);
 }

 private void setProfilePic(HttpServletRequest request, User user) {

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
         user.profilePic = imageUrl;
      } else {
       blobstoreService.delete(blobKey);
      }
    }
  }
}
