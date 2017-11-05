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
import com.revature.trms.database.dao.EmployeeDAO;
import com.revature.trms.database.dao.EmployeeDAOImpl;
import com.revature.trms.models.Employee;

/**
 * Servlet implementation class Employee
 */
@WebServlet("/Employee")
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("in employee doGet");
		
		int empId = 0;
		
		Employee emp = null;
		
		try {
			//empId = Integer.parseInt(request.getParameter("employeeId"));
			empId = (Integer)request.getSession().getAttribute("employeeId");
			emp = new EmployeeDAOImpl().getEmployee(empId);
		} catch (IllegalStateException e) {
			LoggingService.getLogger().warn("invalid employee id.", e);
			response.getWriter().write("{\"alert\" : \"Invalid employee id\"}");
			return;
		} catch (SQLException e) {
			LoggingService.getLogger().warn("invalid employee id.", e);
			response.getWriter().write("{\"alert\" : \"Invalid employee id\"}");
			return;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String data = mapper.writeValueAsString(emp);
		
		System.out.println("data: " + data);
		
		response.setStatus(300);
		response.setHeader("Location", "employee.html");
		response.setHeader("Set-Cookie", data);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String employeeString = request.getParameter("employee");
		
		System.out.println("Employee data: " + employeeString);
		
		ObjectMapper mapper = new ObjectMapper();
		Employee emp = mapper.readValue(employeeString, Employee.class);
		
		//hash pass
		emp.setPassword(Hash.pbkdf2(emp.getPlainPassword().toCharArray()));
		
		try {
			EmployeeDAO dao = new EmployeeDAOImpl();
			dao.addEmployee(emp);
			HttpSession newSession = request.getSession();
			newSession.setAttribute("employeeId", dao.getEmployee(emp.getEmail()));
			response.getWriter().write("{\"info\" : \"Registration complete.\"}");
		} catch(SQLException e) {
			e.printStackTrace();
			LoggingService.getLogger().warn("Database error.", e);
			response.getWriter().write("{\"alert\" : \"Database error.\"}");
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		System.out.println("Employee: " + employeeString);
		Employee emp = mapper.readValue(employeeString, Employee.class);
		
		//attempt to modify employee
		try {
			new EmployeeDAOImpl().modifyEmployee(empId, emp);
			response.getWriter().write("{\"info\" : \"Registration complete.\"}");
		} catch(SQLException e) {
			LoggingService.getLogger().warn("Database error.", e);
			response.getWriter().write("{\"alert\" : \"Database error.\"}");
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
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
		
		//attempt to modify employee
		try {
			new EmployeeDAOImpl().deleteEmployee(empId);
			response.getWriter().write("{\"info\" : \"Registration complete.\"}");
		} catch(SQLException e) {
			LoggingService.getLogger().warn("Database error.", e);
			response.getWriter().write("{\"alert\" : \"Database error.\"}");
		}
	}
}
