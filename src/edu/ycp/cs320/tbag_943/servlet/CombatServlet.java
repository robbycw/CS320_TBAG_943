package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.tbag_943.controller.NumbersController;
import edu.ycp.cs320.tbag_943.model.Numbers;

public class CombatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("Combat Servlet: doGet");	
		
		// call JSP to generate empty form
		req.getRequestDispatcher("/_view/combat.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Combat Servlet: doPost");
		

		// holds the error message text, if there is any
		String errorMessage = null;
		
		
		req.setAttribute("errorMessage", errorMessage);
		//req.setAttribute("result", model.getResult());
		
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
