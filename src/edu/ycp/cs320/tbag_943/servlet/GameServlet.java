package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.tbag_943.classes.Game;

public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(false);
		
		if(session == null) {
			// need to make a new session. 
			session = req.getSession(); 
			// will also need to make a Game object for this new session! 
			Game game = new Game(); 
			game.addOutput("Welcome to 9:43!");
			game.addOutput("This text was added as a test in the servlet.");
			game.addOutput("Text was added to Game's outputLog, which was referenced by the JSP to post this all!");
			
			// Game data will be stored in the session, allowing data to be exchanged back and
			// forth in Servlet and JSP.
			session.setAttribute("model", game);
		} 
		
		System.out.println("GameServlet: doGet");
		
		
		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(); 
		System.out.println("GameServlet: doPost");
		
		
		// We will want to use the resp.sendRedirect method as it will provide an easy way to send the user
		// to the desired URL, which will consequently call the chosen servlet's doGet method.
		
		
		if(req.getParameter("title") != null) {
			
			System.out.println("GameServlet: titlePage");
			
			resp.sendRedirect("/tbag_943/titlepage");
			
		} else if(req.getParameter("credits") != null) {
			
			System.out.println("GameServlet: credits");
			
			resp.sendRedirect("/tbag_943/credits");
			
		} else if(req.getParameter("combat") != null) {
			
			System.out.println("GameServlet: combat");
			
			resp.sendRedirect("/tbag_943/combat");
			
		}
		// Once we have a Game model that persists each request, we can have it so the Servlet will
		// store Strings from the user input into the outputLog in Game. 
		if(req.getParameter("user") != null) {
			String in = req.getParameter("user"); 
			Game model = (Game) session.getAttribute("model"); 
			
			model.addOutput(in);
			session.setAttribute("model", model);
		}
		
		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
}
