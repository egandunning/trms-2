package com.revature.trms.database.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.logging.LoggingService;
import com.revature.trms.database.ConnectionFactory;
import com.revature.trms.models.Request;

import oracle.jdbc.OracleTypes;


public class RequestDAOImpl implements RequestDAO{

	ConnectionFactory cf;
	
	public RequestDAOImpl() {
		cf = ConnectionFactory.getInstance();
	}
		
	@Override
	public void addRequest(Request r) throws SQLException {
		// TODO Auto-generated method stub

		if(r == null) {
			LoggingService.getLogger().debug("empty employee passed to "
					+ "EmployeeDAOImpl.addEmployee()");
			return;
		}

		String sql = "{call insert_request(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			
			call.setInt(1, r.getEmployeeId());
			call.setDouble(2, r.getCost());
			call.setDate(3, new Date(1));
			call.setString(4, r.getStreetAddress());
			call.setString(5, r.getCity());
			call.setString(6, r.getState());
			call.setString(7, r.getZip());
			call.setString(8, r.getDescription());
			call.setInt(9, r.getEventType());
			call.setInt(10, r.getGradingFormat());
			call.setInt(11, r.getDaysMissed());
			call.setString(12, r.getJustification());
			call.setInt(13, 0);
			
			call.executeUpdate();
			LoggingService.getLogger().info("Employee " + r.getEmployeeId() + 
					" request for " + r.getCost() + " added to requests");
		}
	}

	@Override
	public List<Request> getRequests() throws SQLException {

		String sql = "{call read_all_requests(?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.registerOutParameter(1, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(1);
			
			ArrayList<Request> requests = new ArrayList<Request>();
			
			while(rs.next()) {
				Request req = requestFromResultSet(rs);
				requests.add(req);
			}
			return requests;
		}
		
	}
	
	

	@Override
	public List<Request> getRequests(int employeeId) throws SQLException {

		String sql = "{call read_requests_by_employee(?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, employeeId);
			call.registerOutParameter(2, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(2);
			
			ArrayList<Request> requests = new ArrayList<Request>();
			
			while(rs.next()) {
				Request req = requestFromResultSet(rs);
				requests.add(req);
			}
			return requests;
		}
		
	}
	
	@Override
	public List<Request> getSubordinateRequests(int employeeId) throws SQLException {
		
		String sql = "{call read_sub_requests_by_employee(?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, employeeId);
			call.registerOutParameter(2, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(2);
			
			ArrayList<Request> requests = new ArrayList<Request>();
			
			while(rs.next()) {
				Request req = requestFromResultSet(rs);
				requests.add(req);
			}
			return requests;
		}
	}
	
	@Override
	public List<Request> getDepartmentRequests(int departmentId) throws SQLException {
		
		String sql = "{call read_sub_requests_by_department(?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, departmentId);
			call.registerOutParameter(1, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(1);
			
			ArrayList<Request> requests = new ArrayList<Request>();
			
			while(rs.next()) {
				Request req = requestFromResultSet(rs);
				requests.add(req);
			}
			return requests;
		}
	}

	@Override
	public Request getRequest(int id) throws SQLException {

		String sql = "{call read_request_by_id(?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, id);
			call.registerOutParameter(2, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(2);
			
			Request req = null;
			if(rs.next()) {
				req = requestFromResultSet(rs);
			}
			
			return req;
		}
	}

	@Override
	public void modifyRequest(int id, Request r) throws SQLException {

		String sql = "{call update_request(?,?,?,?,?,?,?,?,?,?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, r.getId());
			call.setDouble(2, r.getCost());
			call.setString(3, r.getStreetAddress());
			call.setString(4, r.getCity());
			call.setString(5, r.getState());
			call.setString(6, r.getZip());
			call.setString(7, r.getDescription());
			call.setInt(8, r.getEventType());
			call.setInt(9, r.getGradingFormat());
			call.setInt(10, r.getDaysMissed());
			call.setString(11, r.getJustification());
			
			call.executeUpdate();
			LoggingService.getLogger().info("Request " + r.getId() + " ($" + r.getCost() + ") updated");
		}

	}
	
	@Override
	public void modifyRequestStatus(int id, int s) throws SQLException {

		String sql = "{call update_request_status(?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			
			call.setInt(2, s);
			
			call.executeUpdate();
			LoggingService.getLogger().info("Request " + id + " status updated to " + s);
		}

	}

	@Override
	public void deleteRequest(int id) throws SQLException {
		
		String sql = "{call delete_request_with_id(?)}";
		
		try (Connection conn = cf.getConnection()){
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, id);
			call.executeUpdate();
			LoggingService.getLogger().info("Deleted request " + id);
		}

	}
	
	private Request requestFromResultSet(ResultSet rs) throws SQLException {
		Request req = new Request();
		req.setId(rs.getInt(1));
		req.setEmployeeId(rs.getInt(2));
		req.setCost(rs.getDouble(3));
		req.setStatus(rs.getInt(4));
		req.setStreetAddress(rs.getString("address_street_address"));
		req.setCity(rs.getString("address_city"));
		req.setState(rs.getString("address_state"));
		req.setZip(rs.getString("address_zip"));
		req.setDescription(rs.getString(9));
		req.setEventType(rs.getInt(10));
		req.setGradingFormat(rs.getInt(11));
		req.setDaysMissed(rs.getInt(12));
		req.setJustification(rs.getString(13));
		return req;
	}
	
	

}
