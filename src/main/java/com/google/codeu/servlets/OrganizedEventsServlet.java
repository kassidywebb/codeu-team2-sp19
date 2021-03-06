package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Event;
import com.google.gson.Gson;

/**
 * Handles fetching all messages for the public feed.
 */
@WebServlet("/OrganizedEvents")
public class OrganizedEventsServlet extends HttpServlet {

	private Datastore datastore;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	/**
	 * Responds with a JSON representation of Message data for all users.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)

			throws IOException {

		response.setContentType("application/json");
		String user = request.getParameter("user");

		List<Event> events = datastore.getEvents(user);
		Gson gson = new Gson();
		String json = gson.toJson(events);
		response.getOutputStream().println(json);
	}
}