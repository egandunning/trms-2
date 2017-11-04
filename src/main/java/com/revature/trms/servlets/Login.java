package com.revature.trms.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.trms.auth.AuthorizeEmployee;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	AuthorizeEmployee auth = new AuthorizeEmployee();
    	
		int empId = auth.login(request);
		
		PrintWriter pw = response.getWriter();
		
		if(empId != -1) {
			pw.write("Successful login\n");
			pw.write("Employee ID: " + request.getSession().getAttribute("employeeId"));
			response.setStatus(300);
			response.setHeader("Location", "index.html");
		} else {
			pw.write("{\"alert\": \"Failed to login.\"}");
			response.setStatus(300);
			response.setHeader("Location", "login.html");
		}
	}
}
