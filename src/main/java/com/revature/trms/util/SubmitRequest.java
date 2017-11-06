package com.revature.trms.util;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.revature.trms.database.dao.EventTypeDAOImpl;
import com.revature.trms.database.dao.RequestDAO;
import com.revature.trms.database.dao.RequestDAOImpl;
import com.revature.trms.models.Request;

/**
 * Business logic for submitting requests.
 * @author Egan Dunning
 *
 */
public class SubmitRequest {

	private static Map<Integer, Double> reimburseMap = null;
	
	/**
	 * If the user submitting the request has fewer than 1000 in
	 * reimbursement requests for the year, add the reimbursement
	 * request to the db.
	 * @param req the request to add
	 * @return true if the request was submitted to the database
	 */
	public static boolean submit(Request req) {
		
		initMap();
		
		RequestDAO dao = new RequestDAOImpl();
		
		double requestAmount = req.getCost() * reimburseMap.get(req.getEventType());
		
		try {
			List<Request> requests = dao.getRequests(req.getEmployeeId());
			double reimbursementAmount = 0;
			
			//int year = LocalDateTime.now().getYear();
			for(Request r : requests) {
				
				//TODO: get date of request and compare to year.
				//if(r.getTime().getYear == year) {
					int eventType = r.getEventType();
					reimbursementAmount += reimburseMap.get(eventType) * r.getCost();
				//}
			}
			
			//check amount of the reimbursement requests
			if(reimbursementAmount + requestAmount >= 1000) {
				return false;
			}

			//add request to the db
			dao.addRequest(req);
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Populate the map from event type to reimbursement percentage
	 */
	private static void initMap() {
		if(reimburseMap == null) {
			try {
				reimburseMap = new EventTypeDAOImpl().getReimbursementAmounts();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
