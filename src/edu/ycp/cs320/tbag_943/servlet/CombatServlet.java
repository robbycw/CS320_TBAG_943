package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.tbag_943.classes.User;

public class CombatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HttpSession session = req.getSession(false); 
		
		if(session == null || session.getAttribute("loggedIn") == null) {
			System.out.println("User is not logged in. Redirecting to Title Page.");
			System.out.println("CombatServlet: titlePage");
			
			resp.sendRedirect("/tbag_943/titlePage");
			return; 
		}
		
		System.out.println("Combat Servlet: doGet");	
		
		// call JSP to generate empty form
		req.getRequestDispatcher("/_view/combat.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Combat Servlet: doPost");
		
		// Retrieve the session. 
		HttpSession session = req.getSession(); 
		
		// Get the User class from the session. 
		User user = (User) session.getAttribute("user");

		// holds the error message text, if there is any
		String errorMessage = null;
		
		
		req.setAttribute("errorMessage", errorMessage);
		//req.setAttribute("result", model.getResult());
		
		if (req.getParameter("logOut") != null) {
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
		req.getRequestDispatcher("/_view/combat.jsp").forward(req, resp);
	}

	// gets double from the request with attribute named s
	private Double getDoubleFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return Double.parseDouble(s);
		}
	}
}
