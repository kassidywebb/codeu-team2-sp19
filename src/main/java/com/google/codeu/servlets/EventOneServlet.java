
package com.google.codeu.servlets;

import java.io.IOException;

import java.util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

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
import com.google.codeu.data.Comment;
import com.google.gson.Gson;
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
		String details = event.getDetails();
		String host = event.getHost();
		String imageurl = event.getImageUrl();

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("user", userauthor);
		jsonObject.addProperty("title", title);
		jsonObject.addProperty("date", date);
		jsonObject.addProperty("time",time);
		jsonObject.addProperty("timestamp",timestamp);
		jsonObject.addProperty("location",location);
		jsonObject.addProperty("details",details);
		jsonObject.addProperty("host",host);
		jsonObject.addProperty("imageUrl",imageurl);

		response.getOutputStream().println(jsonObject.toString());

		//Get the comments for the event id, if the arraylist is empty do not print

		List<Comment> comments = datastore.getEventComments(uid);

		if(!comments.isEmpty()) {

			Gson gson = new Gson();
			String json = gson.toJson(comments);
			response.getWriter().println(json);

		}
	}


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserLoggedIn()) {
			//may need to change redirect
			response.sendRedirect("/index.html");
			return;
		}

		String user = userService.getCurrentUser().getEmail();

		//Gets the ID from the event to save to comment
		String eventId = request.getParameter("event");
		if (eventId == null) {
			// Request is invalid, return empty array
			response.getWriter().println("[]");
			return;
		}

		/* here a regular expression is used to search for an image url entered by the user
	       String regex is all the elements found in the url, it is the search pattern
	       the link is then replaced with an img tag, a user is able to submit a
	       pdf,png,jpg,gif,tiff, and bmp file */
		String text = Jsoup.clean(request.getParameter("text"), Whitelist.none());
		String regex = "(https?://\\S+\\.(png|jpeg|jpg|gif|pdf|tiff|bmp))";
		String replacement = "<img src=\"$1\" />";
		String textWithImagesReplaced = text.replaceAll(regex, replacement);

		Comment comment = new Comment(eventId, user, textWithImagesReplaced);

		//blobstore function that gets and saves image url
		setCommentImageUrl(request, comment);
		datastore.storeComment(comment);

		response.sendRedirect("/event.html?event=" + eventId);
	}

	/* Function that handles all blobstore requests for adding an image
  to a message */

	private void setCommentImageUrl (HttpServletRequest request, Comment comment) {

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
				comment.setImageUrl(imageUrl);
			} else {
				blobstoreService.delete(blobKey);
			}
		}
	}
}
