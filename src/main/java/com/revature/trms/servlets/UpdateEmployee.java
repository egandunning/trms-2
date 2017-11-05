package com.revature.trms.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.crypto.Hash;
import com.revature.logging.LoggingService;
import com.revature.trms.database.dao.EmployeeDAOImpl;
import com.revature.trms.models.Employee;

/**
 * Servlet implementation class UpdateEmployee
 */
@WebServlet("/UpdateEmployee")
public class UpdateEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession currentSession = request.getSession(false);
		
		if(currentSession == null) {
			response.setStatus(300);
			response.addHeader("Location", "login.html");
			return;
		}
		
		int empId = 0;
		
		
		//check employee id associated with session
		try {
			empId = Integer.parseInt(currentSession.getAttribute("employeeId").toString());
		} catch (NumberFormatException e) {
			LoggingService.getLogger().warn("Invalid session");
			response.setStatus(300);
			response.addHeader("Location", "login.html");
			return;
		}
		
		//get employee json object
		ObjectMapper mapper = new ObjectMapper();
		
		String employeeString = request.getParameter("employee");
		
		Employee emp = mapper.readValue(employeeString, Employee.class);
		
		//hash pass
		emp.setPassword(Hash.pbkdf2(emp.getPlainPassword().toCharArray()));
		
		//attempt to modify employee
		try {
			new EmployeeDAOImpl().modifyEmployee(empId, emp);
			response.getWriter().write("{\"info\" : \"Registration complete.\"}");
		} catch(SQLException e) {
			LoggingService.getLogger().warn("Database error.", e);
			response.getWriter().write("{\"alert\" : \"Database error.\"}");
		}
	}

}
