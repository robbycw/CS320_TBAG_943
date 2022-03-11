package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class EndCreditsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("EndCredits Servlet: doGet");	
		
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
		
		// Add parameters as request attributes
		// this creates attributes named "first" and "second for the response, and grabs the
		// values that were originally assigned to the request attributes, also named "first" and "second"
		// they don't have to be named the same, but in this case, since we are passing them back
		// and forth, it's a good idea
		//req.setAttribute("first", req.getParameter("first"));
		//req.setAttribute("second", req.getParameter("second"));
		//req.setAttribute("third", req.getParameter("third"));
		
		// add result objects as attributes
		// this adds the errorMessage text and the result to the response
		//req.setAttribute("errorMessage", errorMessage);
		//req.setAttribute("result", model.getResult());
		//req.setAttribute("submit", req.getParameter("submit"));
		
		// We only need to access the model with these changes. 
		//req.setAttribute("sum", model);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/EndCredits.jsp").forward(req, resp);
	}
}
