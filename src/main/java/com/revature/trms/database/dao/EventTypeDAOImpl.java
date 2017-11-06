package com.revature.trms.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.revature.trms.database.ConnectionFactory;

public class EventTypeDAOImpl implements EventTypeDAO {

	ConnectionFactory cf;
	
	public EventTypeDAOImpl() {
		cf = ConnectionFactory.getInstance();
	}
	
	@Override
	public Map<Integer, Double> getReimbursementAmounts() throws SQLException{
		
		String sql = "select event_type_id, event_type_reimburse_per "
				+ "from event_type";
		
		try(Connection conn = cf.getConnection()) {
			PreparedStatement p = conn.prepareStatement(sql);
			ResultSet rs = p.executeQuery();
			
			Map<Integer, Double> reimburseMap = new HashMap<Integer, Double>();
			
			while(rs.next()) {
				reimburseMap.put(rs.getInt(1), rs.getDouble(2) / 100.0);
			}
			return reimburseMap;
		}
	}

}
