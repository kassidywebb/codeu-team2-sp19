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

package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import java.util.Map;
import java.util.Enumeration;
import com.google.appengine.api.images.ImagesServiceFailureException;

/** Handles fetching and saving {@link Message} instances. */
@WebServlet("/messages")
public class MessageServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with a JSON representation of {@link Message} data for a specific user. Responds with
   * an empty array if the user is not provided.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String user = request.getParameter("user");
    String recipient = request.getParameter("recipient");

    if (user == null || user.equals("")) {
      // Request is invalid, return empty array
      response.getWriter().println("[]");
      return;
    }

    List<Message> messages = datastore.getMessages(user);
    Gson gson = new Gson();
    String json = gson.toJson(messages);

    response.getWriter().println(json);
  }

  /** Stores a new {@link Message}. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String user = userService.getCurrentUser().getEmail();

    /* here a regular expression is used to search for an image url entered by the user
       String regex is all the elements found in the url, it is the search pattern
       the link is then replaced with an img tag, a user is able to submit a
       pdf,png,jpg,gif,tiff, and bmp file */
    String text = Jsoup.clean(request.getParameter("text"), Whitelist.none());
    String regex = "(https?://\\S+\\.(png|jpeg|jpg|gif|pdf|tiff|bmp))";
    String replacement = "<img src=\"$1\" />";
    String textWithImagesReplaced = text.replaceAll(regex, replacement);

    String sendto = request.getParameter("sendto");
    String recipient = request.getParameter("recipient");
    String privatemessage = request.getParameter("private");

    if (privatemessage != null) {
      if (recipient.compareTo(sendto) < 0) {
        recipient = recipient + sendto;
      } else {
        recipient = sendto + recipient;
      }
    } else if (!sendto.isEmpty()) {
        recipient = sendto;
    } else {
      recipient = recipient;
    }

    Message message = new Message(user, textWithImagesReplaced, recipient);

    //blobstore function that gets and saves image url
    setMessageImageUrl(request, message);
    datastore.storeMessage(message);

    response.sendRedirect("/user-page.html?user=" + user);
  }

  /* Function that handles all blobstore requests for adding an image
     to a message */
  private void setMessageImageUrl (HttpServletRequest request, Message message) {

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
         message.setImageUrl(imageUrl);
      } else {
       blobstoreService.delete(blobKey);
      }
    }
  }
}
