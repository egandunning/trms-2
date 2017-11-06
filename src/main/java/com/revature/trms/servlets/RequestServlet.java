package com.revature.trms.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.trms.database.dao.EmployeeDAO;
import com.revature.trms.database.dao.EmployeeDAOImpl;
import com.revature.trms.models.Employee;
import com.revature.trms.models.Request;
import com.revature.trms.util.GetRequests;
import com.revature.trms.util.SubmitRequest;

/**
 * Servlet implementation class RequestServlet
 */
@WebServlet("/Request")
public class RequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Based on employee type, returns a list of request lists. the first element
	 * in the list is the list of the users own requests, the second element in
	 * the list is the list of requests that the user can approve.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession currentSession = request.getSession(false);
		
		if(currentSession == null) {
			System.out.println("No session... Redirecting.");
			response.setStatus(300);
			response.setHeader("Location", "login.html");
			return;
		}
		
		int empId = (Integer)currentSession.getAttribute("employeeId");
		
		PrintWriter pw = response.getWriter();
		
		EmployeeDAO dao = new EmployeeDAOImpl();
		
		List<List<Request>> requests = null;
		
		try {
			Employee emp = dao.getEmployee(empId);
			if(emp.getDepartment().equals("benco")) {
				requests = GetRequests.getRequestsBenCo(empId);
			} else if(emp.getTitle().equalsIgnoreCase("department head")) {
				requests = GetRequests.getRequestsDepartmentHead
						(empId, emp.getDepartmentId());
			} else {
				requests = GetRequests.getRequests(empId);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		ObjectMapper mapper = new ObjectMapper();
		pw.write(mapper.writeValueAsString(requests));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int empId = 0;
		
		try {
			empId = (Integer)request.getSession().getAttribute("employeeId");
		} catch (Exception e) {
			System.out.println("exception in post request");
			e.printStackTrace();
		}
		
		String requestData = request.getParameter("request");
		System.out.println("Request data: " + requestData);
		
		ObjectMapper mapper = new ObjectMapper();
		
		Request req = mapper.readValue(requestData, Request.class);
		
		req.setEmployeeId(empId);
		
		
		if(SubmitRequest.submit(req)) {
			System.out.println("successfully added reimbursement request");
		} else {
			System.out.println("failed to add reimbursement request");
		}
	}

}
