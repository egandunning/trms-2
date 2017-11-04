package com.revature.trms.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.trms.database.dao.RequestDAO;
import com.revature.trms.database.dao.RequestDAOImpl;

/**
 * Servlet implementation class RequestServlet
 */
@WebServlet("/Request")
public class RequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession currentSession = request.getSession(false);
		
		if(currentSession == null) {
			System.out.println("No session... Redirecting.");
			response.setStatus(300);
			response.setHeader("Location", "login.html");
			return;
		}
		
		PrintWriter pw = response.getWriter();
		RequestDAO dao = new RequestDAOImpl();
		
		try {
			dao.getRequests((Integer)(currentSession.getAttribute("employeeId")));
		} catch (SQLException e) {
			System.out.println("SQL error.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Session error.");
			e.printStackTrace();
		}
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
