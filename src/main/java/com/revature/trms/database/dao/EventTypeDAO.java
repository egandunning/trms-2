package com.revature.trms.database.dao;

import java.sql.SQLException;
import java.util.Map;

public interface EventTypeDAO {

	public Map<Integer, Double> getReimbursementAmounts() throws SQLException;
}
