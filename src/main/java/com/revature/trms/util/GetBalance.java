package com.revature.trms.util;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.revature.trms.database.dao.EventTypeDAOImpl;
import com.revature.trms.database.dao.RequestDAO;
import com.revature.trms.database.dao.RequestDAOImpl;
import com.revature.trms.models.Request;

import com.revature.trms.database.dao.EmployeeDAO;
import com.revature.trms.database.dao.EmployeeDAOImpl;
import com.revature.trms.models.Employee;



public class GetBalance {

	public static double getPendingBalance(Employee emp)
	{
		
		return 0;
		
	}
	public static double getApprovedBalance(Employee emp)
	{
		
		return 0;
		
	}
	public static double getTotalBalance(Employee emp)
	{
		double total = (1000 - getApprovedBalance(emp) - getPendingBalance(emp));
		return total;
	}
}
