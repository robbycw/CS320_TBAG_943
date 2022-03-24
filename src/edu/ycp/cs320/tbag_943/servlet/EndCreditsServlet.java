package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ycp.cs320.tbag_943.classes.*;
import edu.ycp.cs320.tbag_943.controller.*;


public class EndCreditsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

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
		
		if(req.getParameter("submit") != null) {
			System.out.println("Title Screen Servlet: ");
			req.getRequestDispatcher("/servlet/TitleScreen.java").forward(req, resp);
		}
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/EndCredits.jsp").forward(req, resp);
	}
}
