package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.tbag_943.classes.*;

public class TitlePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(false); 
		
		// Checks if the session has been created and if the User class has been 
		// initialized in the session. 
		if(session == null || session.getAttribute("user") == null) {
			// User is not created or there is no session. 
			
			// Create the session if necessary. 
			session = req.getSession(); 
			
			// Create the User class and set it as the user attribute.
			// Also create the login error attribute. 
			User user = new User(); 
			String loginError = ""; 
			session.setAttribute("user", user);
			session.setAttribute("loginErr", loginError);
			session.setAttribute("makeNewAccount", false);
			
		}

		System.out.println("TitlePage Servlet: doGet");	
		
		// call JSP to generate empty form
		req.getRequestDispatcher("/_view/titlePage.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Retrieve the session. 
		HttpSession session = req.getSession(); 
		System.out.println("TitlePage Servlet: doPost");
		
		if(req.getParameter("game") != null) {
			
			System.out.println("TitlePage Servlet: Game");
			
			resp.sendRedirect("/tbag_943/game");
			return; 
			
		} else if(req.getParameter("credits") != null) {
			
			System.out.println("TitlePage Servlet: Credits");
			
			resp.sendRedirect("/tbag_943/credits");
			return; 
			
		} else if(req.getParameter("options") != null) {
			
			System.out.println("TitlePage Servlet: Options");
			
			resp.sendRedirect("/tbag_943/game");
			return; 
			
		} else if(req.getParameter("loginSubmit") != null) {
			// This map will represent the Database Table of usernames/passwords until implemented. 
			HashMap<String, String> userPass = new HashMap<String, String>(); 
			
			userPass.put("robbyw", "tbags"); 
			userPass.put("admin", "adminPassword"); 
			
			
			
			// The User has attempted to log in. 
			User user = (User) session.getAttribute("user"); 
			String username = (String) req.getParameter("username"); 
			String password = (String) req.getParameter("password"); 
			
			System.out.println("username: " + username); 
			
			// Consider if user doesn't enter a username or password. 
			if(username == null || password == null || username.isEmpty() || password.isEmpty()) {
				String loginError = "Please specify a username and password."; 
				session.setAttribute("loginErr", loginError);
			}
			
			// Check that the username and password matches a username and password
			// within the database (for now, users are hardcoded). 
			
			// Check if username exists. 
			if(userPass.containsKey(username)) {
				// Check if password matches. 
				// Log in if so, otherwise supply proper error.
				if(userPass.get(username).equals(password)) {
					System.out.println("	User " + username + " has logged in."); 
					
					user.setCreated(true);
					user.setUsername(username);
					user.setPassword(password);
					
					// User is now logged in, store user data into session. 
					session.setAttribute("user", user);
					session.setAttribute("loggedIn", true); 
					
				} else {
					// Password does not match. 
					String loginError = "Incorrect password."; 
					session.setAttribute("loginErr", loginError);
				}
				
			} else {
				// Username does not exist. 
				String loginError = "Username does not exist."; 
				session.setAttribute("loginErr", loginError);
			}
			
		} else if (req.getParameter("createAccount") != null) {
			// The user wants to create an account. 
			// This will change the overlay so that it displays
			// account creation instead of login. 
			
			session.setAttribute("makeNewAccount", true); 
			session.setAttribute("loginErr", "");
			
		} else if (req.getParameter("attemptCreateAccount") != null){
			
			// This map will represent the Database Table of usernames/passwords until implemented. 
			HashMap<String, String> userPass = new HashMap<String, String>(); 
			
			userPass.put("robbyw", "tbags"); 
			userPass.put("admin", "adminPassword"); 
			
			// The User has attempted to create an account. 
			User user = (User) session.getAttribute("user"); 
			String username = (String) req.getParameter("username"); 
			String password = (String) req.getParameter("password"); 
			
			System.out.println("username: " + username); 
			
			// Consider if user doesn't enter a username or password. 
			if(username == null || password == null || username.isEmpty() || password.isEmpty()) {
				String loginError = "Please specify a username and password."; 
				session.setAttribute("loginErr", loginError);
			}
			
			// The user has entered a username and password for their account. 
			// We want to ensure a username does not already exist. 
			else if (userPass.containsKey(username)) {
				String loginError = username + " already exists. Pick a different username.";  
				session.setAttribute("loginErr", loginError);
			} else {
				System.out.println("	Creating account for username: " + username); 
				// The username does not yet exist. Make the account and log the user in. 
				user.setUsername(username);
				user.setPassword(password);
				user.setCreated(true);
				userPass.put(username, password); 
				
				// User is now logged in, store user data into session. 
				session.setAttribute("user", user);
				session.setAttribute("loggedIn", true); 
				session.setAttribute("makeNewAccount", false);
				
			}
			
		} else if (req.getParameter("createCancel") != null) {
			// The user is cancelling account creation. 
			// Reload the login page. 
			session.setAttribute("loggedIn", false); 
			session.setAttribute("makeNewAccount", false);
			session.setAttribute("loginErr", "");
		}
		
		
		// decode POSTed form parameters and dispatch to controller
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/titlePage.jsp").forward(req, resp);
	}

}
