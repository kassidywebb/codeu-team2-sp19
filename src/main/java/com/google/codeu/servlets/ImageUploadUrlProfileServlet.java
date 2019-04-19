package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.codeu.data.Datastore;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.codeu.data.User;
import com.google.gson.JsonObject;

/**
 * Provides access to a URL that allows a user to upload an image to Blobstore.
 */
@WebServlet("/image-upload-url-profile")
public class ImageUploadUrlProfileServlet extends HttpServlet {

	private Datastore datastore;

	@Override
	public void init() {
		datastore = new Datastore();
	}


	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String user = request.getParameter("user");
		response.setContentType("application/json");

		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		String uploadUrl = blobstoreService.createUploadUrl("/about");

		User userData = datastore.getUser(user);

		if(userData == null || userData.getAboutMe() == null){
			response.getWriter().println("[]");
			return;
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("useremail", user);
		jsonObject.addProperty("aboutMe", userData.getAboutMe());
		jsonObject.addProperty("name", userData.getName());
		userData.setProfilePic(uploadUrl);
		jsonObject.addProperty("profilePic", userData.getprofilePic());

		response.getOutputStream().println(jsonObject.toString());
		response.getOutputStream().println(uploadUrl);
	}
}
