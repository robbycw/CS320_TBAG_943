package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Index Servlet: doGet");
		
		req.getRequestDispatcher("/_view/index.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Index Servlet: doPost");
		
		// the format of doPost will resemble that of GuessingGame, as we must first determine which
		// button was pressed to know which Servlet to call. 
		/*
		if(req.getParameter("add") != null) {
			System.out.println("Index Servlet: addNumbers");
			req.getRequestDispatcher("/servlet/AddNumbersServlet.java").forward(req, resp);
		} else if(req.getParameter("multiply") != null) {
			System.out.println("Index Servlet: MultiplyNumbers");
			req.getRequestDispatcher("/servlet/MultiplyNumbersServlet.java").forward(req, resp);
		} else if(req.getParameter("guess") != null) {
			System.out.println("Index Servlet: Guessing Game");
			req.getRequestDispatcher("/servlet/GuessingGameServlet.java").forward(req, resp);
		}
		*/
		
		// We will want to use the resp.sendRedirect method as it will provide an easy way to send the user
		// to the desired URL, which will consequently call the chosen servlet's doGet method.
		
		// The issue with using req.getRequestDispatcher is that it only calls the JSP, not the doGet method. 
		// Hence, the user remains on the index URL until they start using their chosen web application. 
		
		if(req.getParameter("titlePage") != null) {
			System.out.println("Index Servlet: titlePage");
			//req.getRequestDispatcher("/_view/addNumbers.jsp").forward(req, resp);
			
			resp.sendRedirect("/tbag_943/titlePage");
			
		} else if(req.getParameter("multiply") != null) {
			System.out.println("Index Servlet: multiplyNumbers");
			//req.getRequestDispatcher("/_view/multiplyNumbers.jsp").forward(req, resp);
			
			resp.sendRedirect("/tbag_943/multiplyNumbers");
			
		} else if(req.getParameter("guess") != null) {
			System.out.println("Index Servlet: guessingGame");
			//req.getRequestDispatcher("/_view/guessingGame.jsp").forward(req, resp);
			
			resp.sendRedirect("/tbag_943/guessingGame");
			
		}
		 
		
	}
}
