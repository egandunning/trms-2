package com.revature.trms.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.logging.LoggingService;
import com.revature.trms.database.dao.EmployeeDAOImpl;
import com.revature.trms.models.Employee;

/**
 * Servlet implementation class EmployeeInfo
 */
@WebServlet("/EmployeeInfo")
public class EmployeeInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int empId = 0;
		
		Employee emp = null;
		
		try {
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
		response.getWriter().append(data);
	}

}
