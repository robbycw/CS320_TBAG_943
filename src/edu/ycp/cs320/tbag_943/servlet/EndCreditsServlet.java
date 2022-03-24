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
		
		WinCondition model = new WinCondition();
		//EndCreditsController controller = new EndCreditsController();
		//controller.setModel(model);
		
		String condition = model.currentWinCondition();
		
		req.setAttribute("winCondition", model);
		
//		if(condition == "lost") {
//			
//		} else if(condition == "wonRooms") {
//			
//		} else if(condition == "bestCase") {
//			
//		}
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/EndCredits.jsp").forward(req, resp);
	}
}
