package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.tbag_943.classes.*;
import edu.ycp.cs320.tbag_943.controller.DBController;

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
			session.setAttribute("playGameClicked", false);
			
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
		
		// Get the User class from the session. 
		User user = (User) session.getAttribute("user"); 
		
		// Initialize DBController
		DBController dbc = new DBController(); 
		
		System.out.println("TitlePage Servlet: doPost");
		
		if(req.getParameter("game") != null) {
			
			System.out.println("TitlePage Servlet: Play Game");
			// User has clicked Play Game. See if currentGame is null or if a Game is selected.
			
			if(user.getCurrentGame() == null) {
				// Load the overlay for selecting a game. 
				session.setAttribute("playGameClicked", true);
			} else {
				// Game was selected. 
				// Redirect the user to the Game page! 
				resp.sendRedirect("/tbag_943/game");
				return;
			} 
			
		} else if(req.getParameter("credits") != null) {
			
			System.out.println("TitlePage Servlet: Credits");
			
			resp.sendRedirect("/tbag_943/credits");
			return; 
			
		} else if(req.getParameter("options") != null) {
			
			System.out.println("TitlePage Servlet: Options");
			
			resp.sendRedirect("/tbag_943/game");
			return; 
			
		} else if(req.getParameter("loginSubmit") != null) {
			
			// The User has attempted to log in. 
			
			String username = (String) req.getParameter("username"); 
			String password = (String) req.getParameter("password"); 
			
			System.out.println("username: " + username); 
			
			// Consider if user doesn't enter a username or password. 
			if(username == null || password == null || username.isEmpty() || password.isEmpty()) {
				String loginError = "Please specify a username and password."; 
				session.setAttribute("loginErr", loginError);
			} else {
				// Check that the username and password matches a username and password
				// within the database. 
				User loginUser = dbc.login(username, password); 
				
				// Check if username exists. 
				if(loginUser != null) {			
					// Query returned a User. 
					
					System.out.println("	User " + username + " has logged in."); 
					
					user = loginUser; 
					
					// Need to fetch the user's gameList from the database
					List<Game> games = dbc.loadGamesInfo(user.getId()); 
					ArrayList<Game> gameList = new ArrayList<Game>(); 
					gameList.addAll(games); 
					user.setGameList(gameList);
									
					// User is now logged in, store user data into session. 
					session.setAttribute("user", user);
					session.setAttribute("loggedIn", true); 

				} else {
					// The Query returned null. Supply error message. 
					String loginError = "Username or password does not exist."; 
					session.setAttribute("loginErr", loginError);
				}
			}
			
		} else if (req.getParameter("createAccount") != null) {
			// The user wants to create an account. 
			// This will change the overlay so that it displays
			// account creation instead of login. 
			
			session.setAttribute("makeNewAccount", true); 
			session.setAttribute("loginErr", "");
			
		} else if (req.getParameter("attemptCreateAccount") != null){
			
			// The User has attempted to create an account. 
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
			else {
				System.out.println("	Attempting to create account for username: " + username); 
				
				User newUser = dbc.createAccount(username, password); 
				
				if(newUser == null) {
					// Account not created due to matching username. 
					String loginError = "This username already exists."; 
					session.setAttribute("loginErr", loginError);
				} else {
					// New User successfully created. 
					// User is now logged in, store user data into session. 
					newUser.setCreated(true);
					newUser.setCurrentGame(null);
					newUser.setGameList(new ArrayList<Game>());
					session.setAttribute("user", newUser);
					session.setAttribute("loggedIn", true); 
					session.setAttribute("makeNewAccount", false);
				}
			}
			
		} else if (req.getParameter("createCancel") != null) {
			// The user is cancelling account creation. 
			// Reload the login page. 
			session.setAttribute("loggedIn", false); 
			session.setAttribute("makeNewAccount", false);
			session.setAttribute("loginErr", "");
		} else if (req.getParameter("newGame") != null) {
			// User selected New Game: 
			Game newGame = dbc.newGame(user.getId()); 
			newGame.setPlayerNotCreated(true);
			user.setCurrentGame(newGame);
			
			ArrayList<Game> gameList = user.getGameList(); 
			
			if(gameList == null || gameList.isEmpty()) {
				gameList = new ArrayList<Game>();
				gameList.add(newGame); 
			} else {
				gameList.add(newGame); 
			}
			user.setGameList(gameList);
			session.setAttribute("user", user);
			session.setAttribute("model", newGame);
			session.setAttribute("playGameClicked", false);
			
		} else if (req.getParameter("logOut") != null) {
			// User wants to log out. 
			User u = new User(); 
			String loginError = ""; 
			
			session.setAttribute("user", u);
			session.setAttribute("loginErr", loginError);
			session.setAttribute("makeNewAccount", false);
			session.setAttribute("playGameClicked", false);
			
		} else if (req.getParameter("titlePage") != null) {
			System.out.println("TitlePage Servlet: TitlePage");
			
			resp.sendRedirect("/tbag_943/titlePage");
			return; 
		}
		
		// Check for which Game was selected in the list, if it was selected: 
		
		for(Game g : user.getGameList()) {
			if(req.getParameter(g.getIdString()) != null) {
				// User has selected this Game, so select this ID. 
				// With DB, will need to load rest of Game! 

				Game selected = dbc.loadGame(g.getId()); 
				
				user.setCurrentGame(selected);
				session.setAttribute("user", user);
				session.setAttribute("model", selected);
				session.setAttribute("playGameClicked", false);
			}
		}
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/titlePage.jsp").forward(req, resp);
	}

}
