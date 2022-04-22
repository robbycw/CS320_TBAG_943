package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.tbag_943.classes.*;
import edu.ycp.cs320.tbag_943.controller.*;


public class EndCreditsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(false); 
		
		if(session == null || session.getAttribute("loggedIn") == null) {
			System.out.println("User is not logged in. Redirecting to Title Page.");
			System.out.println("EndCreditsServlet: titlePage");
			
			resp.sendRedirect("/tbag_943/titlePage");
			return; 
		}

		System.out.println("EndCredits Servlet: doGet");	
		
		// Creating the variable that the jsp will access
		WinCondition model = new WinCondition();
		String condition = model.currentWinCondition();
		req.setAttribute("condition", condition);
		// call JSP to generate empty form
		req.getRequestDispatcher("/_view/EndCredits.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("EndCredits Servlet: doPost");
		
		// Retrieve the session. 
		HttpSession session = req.getSession(); 
				
		// Get the User class from the session. 
		User user = (User) session.getAttribute("user");
		
		if(req.getParameter("submit") != null) {
			System.out.println("Title Screen Servlet: ");
			req.getRequestDispatcher("/servlet/TitleScreen.java").forward(req, resp);
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
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/EndCredits.jsp").forward(req, resp);
	}
}
