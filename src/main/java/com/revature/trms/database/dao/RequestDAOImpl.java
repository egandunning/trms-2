package com.revature.trms.database.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.logging.LoggingService;
import com.revature.trms.database.ConnectionFactory;
import com.revature.trms.models.Employee;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Request getRequest(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifyRequest(int id, Request r) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRequest(int id) throws SQLException {
		// TODO Auto-generated method stub
		
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
