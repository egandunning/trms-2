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

import oracle.jdbc.OracleTypes;

public class EmployeeDAOImpl implements EmployeeDAO {

	ConnectionFactory cf;
	
	public EmployeeDAOImpl() {
		cf = ConnectionFactory.getInstance();
	}
	
	public void addEmployee(Employee e) throws SQLException {
		
		if(e == null) {
			LoggingService.getLogger().debug("empty employee passed to "
					+ "EmployeeDAOImpl.addEmployee()");
			return;
		}

		String sql = "{call insert_employee(?,?,?,?,?,?,?,?,?,?,?,0,0)}";
		
		try(Connection conn = cf.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, e.getFirstname());
			call.setString(2, e.getLastname());
			call.setString(3, e.getStreetAddress());
			call.setString(4, e.getCity());
			call.setString(5, e.getState());
			call.setString(6, e.getZip());
			call.setInt(7, e.getSuperId());
			call.setInt(8, e.getDepartmentId());
			call.setString(9, e.getEmail());
			call.setBytes(10, e.getPassword());
			call.setString(11, e.getTitle());
			
			call.executeUpdate();
			LoggingService.getLogger().info(e.getFirstname() + 
					" " + e .getLastname() + " added to employees");
		}
	}

	public List<Employee> getEmployees() throws SQLException {
		
		String sql = "{call read_all_employees(?)}";
		
		try(Connection conn = cf.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			
			call.registerOutParameter(1, OracleTypes.CURSOR);
			
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(1);
			
			ArrayList<Employee> employees = new ArrayList<Employee>();
			
			while(rs.next()) {
				Employee emp = new Employee();
				emp.setFirstname(rs.getString("e.employee_firstname"));
				emp.setLastname(rs.getString("e.employee_lastname"));
				emp.setStreetAddress(rs.getString("address_street_address"));
				emp.setCity(rs.getString("address_city"));
				emp.setState(rs.getString("address_state"));
				emp.setZip(rs.getString("address_zip"));
				emp.setSuperId(rs.getInt("super.employee_id"));
				emp.setSuperFirstname(rs.getString("super.employee_firstname"));
				emp.setSuperLastname(rs.getString("super.employee_lastname"));
				emp.setDepartment(rs.getString("department_name"));
				emp.setDepartmentId(rs.getInt("department_id"));
				emp.setEmail(rs.getString("e.employee_email"));
				emp.setTitle(rs.getString("employee_title_name"));
				employees.add(emp);
			}
			
			return employees;
		}

	}

	public List<Employee> getEmployees(int departmentId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Employee> getEmployees(String department) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Employee getEmployee(String email) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Employee getEmployee(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void modifyEmployee(String email, Employee e) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void modifyEmployee(int id, Employee e) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void deleteEmployee(String email) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void deleteEmployee(int id) throws SQLException {
		// TODO Auto-generated method stub

	}

}
