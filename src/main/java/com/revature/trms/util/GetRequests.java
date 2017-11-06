package com.revature.trms.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.trms.database.dao.RequestDAO;
import com.revature.trms.database.dao.RequestDAOImpl;
import com.revature.trms.models.Request;

/**
 * Business logic for accessing requests.
 * @author Egan Dunning
 *
 */
public class GetRequests {

	/**
	 * Get the requests for an employee and their subordinates
	 * @param id employee id
	 * @return a list containing the employee's requests at index 1 and
	 * their subordinates requests at index 2.
	 */
	public static List<List<Request>> getRequests(int id) {
		
		List<List<Request>> requests = new ArrayList<List<Request>>();
		
		RequestDAO dao = new RequestDAOImpl();
		
		try {
			List<Request> empRequests = dao.getRequests(id);
			requests.add(0,empRequests);
			
			List<Request> subRequests = dao.getSubordinateRequests(id);
			requests.add(1, subRequests);
		} catch(SQLException e) {
			e.printStackTrace();
			return requests;
		}
		return requests;
	}
	
	/**
	 * Get the requests for the department head and their subordinates
	 * @param id the dept head's id
	 * @param deptId the dept id
	 * @return a list containing the dept head's requests at index 1 and
	 * their subordinates requests at index 2.
	 */
	public static List<List<Request>> getRequestsDepartmentHead(int id, int deptId) {
		
		List<List<Request>> requests = new ArrayList<List<Request>>();
		
		RequestDAO dao = new RequestDAOImpl();
		
		try {
			List<Request> empRequests = dao.getRequests(id);
			requests.add(0,empRequests);
			throw new NullPointerException("Not implemented, need dao method implementation");
			//List<Request> subRequests = null; //dao.getDepartmentRequests(id);
			//requests.add(1, subRequests);
		} catch(SQLException e) {
			e.printStackTrace();
			return requests;
		}
		//return requests;
	}
	
	/**
	 * Get the requests for the department head and their subordinates
	 * @param id the BenCo's id
	 * @return a list containing the BenCo's requests at index 1 and
	 * all other requests at index 2.
	 */
	public static List<List<Request>> getRequestsBenCo(int id) {

		List<List<Request>> requests = new ArrayList<List<Request>>();
		
		RequestDAO dao = new RequestDAOImpl();
		
		try {
			List<Request> empRequests = dao.getRequests(id);
			requests.add(0,empRequests);
			
			List<Request> all = dao.getRequests();
			
			//remove this employee's requests from the list of all requests
			for(int i = 0; i < all.size(); i++) {
				if(all.get(i).getEmployeeId() == id) {
					all.remove(i);
				}
			}
			
			requests.add(1, all);
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return requests;
	}
}
