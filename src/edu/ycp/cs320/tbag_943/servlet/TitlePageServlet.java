package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TitlePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("TitlePage Servlet: doGet");	
		
		// call JSP to generate empty form
		req.getRequestDispatcher("/_view/titlePage.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("TitlePage Servlet: doPost");
		
		if(req.getParameter("game") != null) {
			
			System.out.println("TitlePage Servlet: Game");
			
			resp.sendRedirect("/tbag_943/game");
			
		} else if(req.getParameter("credits") != null) {
			
			System.out.println("TitlePage Servlet: Credits");
			
			resp.sendRedirect("/tbag_943/credits");
			
		} else if(req.getParameter("options") != null) {
			
			System.out.println("TitlePage Servlet: Options");
			
			resp.sendRedirect("/tbag_943/game");
			
		}
		
		
		// decode POSTed form parameters and dispatch to controller
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/titlePage.jsp").forward(req, resp);
	}

}
