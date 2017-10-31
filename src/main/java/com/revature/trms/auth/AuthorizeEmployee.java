package com.revature.trms.auth;

import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import com.revature.crypto.Hash;
import com.revature.logging.LoggingService;
import com.revature.trms.database.dao.EmployeeDAO;
import com.revature.trms.database.dao.EmployeeDAOImpl;

public class AuthorizeEmployee {

	/**
	 * Checks if the user's email and password match a record
	 * in the database.
	 * @param request The HttpServletRequest with the login info. The
	 * request should have the parameters "email" and "password".
	 * @return The id of the logged in employee as the
	 * attribute "employeeId". If login fails, returns -1.
	 */
	public int login(HttpServletRequest request) {
		
		String email = request.getParameter("email");
		char[] password;
		try {
			password = request.getParameter("password").toCharArray();
		} catch (NullPointerException e) {
			LoggingService.getLogger().warn("No password parameter in request", e);
			return -1;
		}
		
		boolean validUser = login(email, password);
		if(validUser) {
			request.getSession(true);
			try {
				int id = new EmployeeDAOImpl().getEmployee(email).getId();
				request.getSession(true);
				request.getSession().setAttribute("employeeId", id);
				LoggingService.getLogger().info("Employee " + id + " logged in.");
				return id;
				
			} catch (SQLException e) {
				LoggingService.getLogger().warn("Exception getting employee "
						+ "id in AuthorizeEmployee.login(HttpServletRequest)",
						e);
				
			} catch (NullPointerException e) {
				LoggingService.getLogger().warn("Couldn't find employee "
						+ "with email " + email, e);
			}
		}
		LoggingService.getLogger().info("Invalid login attempt for " + email);
		return -1;
		
	}
	
	public boolean login(String email, char[] password) {
		
		EmployeeDAO dao = new EmployeeDAOImpl();
		byte[] storedPass = null;
		try {
			storedPass = dao.getEmployeePassword(email);

			if(Arrays.equals(storedPass, Hash.pbkdf2(password))) {
				//zero out password
				for(int i = 0; i < password.length; i++) {
					password[i] = 0;
				}
				//found user with matching password
				return true;
			}
		} catch (SQLException e) {
			LoggingService.getLogger().warn("Exception in AuthorizeEmployee.login()", e);
		} finally {
			//zero out char array
			for(int i = 0; i < password.length; i++) {
				password[i] = 0;
			}
		}
		
		//couldn't find user with matching password
		return false;
	}
}
