package com.revature.trms.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.logging.LoggingService;
import com.revature.trms.database.dao.DepartmentDAOImpl;
import com.revature.trms.models.Department;

/**
 * Servlet implementation class DepartmentServlet
 */
@WebServlet("/Department")
public class DepartmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In department doGet");
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<Department> depts = null;
		
		try {
			depts = new DepartmentDAOImpl().getDepartments();
		} catch (SQLException e) {
			LoggingService.getLogger().warn("Database error", e);
			System.out.println("db error in department servlet doGet");
		}
		
		System.out.println("Writing response text");
		response.getWriter().append(mapper.writeValueAsString(depts));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost not implemented in department servlet");
	}

}
