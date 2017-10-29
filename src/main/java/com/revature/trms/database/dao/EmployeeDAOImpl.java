package com.revature.trms.database.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
	
	@Override
	public void addEmployee(Employee e) throws SQLException {
		
		if(e == null) {
			LoggingService.getLogger().debug("empty employee passed to "
					+ "EmployeeDAOImpl.addEmployee()");
			return;
		}

		String sql = "{call insert_employee(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
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
			call.setInt(12, 0);
			call.setInt(13, 0);
			
			call.executeUpdate();
			LoggingService.getLogger().info(e.getFirstname() + 
					" " + e .getLastname() + " added to employees");
		}
	}

	@Override
	public List<Employee> getEmployees() throws SQLException {
		
		String sql = "{call read_all_employees(?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.registerOutParameter(1, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(1);
			
			ArrayList<Employee> employees = new ArrayList<Employee>();
			
			while(rs.next()) {
				Employee emp = employeeFromResultSet(rs);
				employees.add(emp);
			}
			return employees;
		}

	}

	@Override
	public List<Employee> getEmployees(int departmentId) throws SQLException {

		String sql = "{call read_employees_by_dept(?, ?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, departmentId);
			call.registerOutParameter(2, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(2);
			
			ArrayList<Employee> employees = new ArrayList<Employee>();
			
			while(rs.next()) {
				Employee emp = employeeFromResultSet(rs);
				employees.add(emp);
			}
			return employees;
		}
	}

	@Override
	public List<Employee> getEmployees(String department) throws SQLException {

		String sql = "{call read_employees_by_dept_name(?, ?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, department);
			call.registerOutParameter(2, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(2);
			
			ArrayList<Employee> employees = new ArrayList<Employee>();
			
			while(rs.next()) {
				Employee emp = employeeFromResultSet(rs);
				employees.add(emp);
			}
			return employees;
		}
	}

	@Override
	public Employee getEmployee(String email) throws SQLException {

		String sql = "{call read_employee_by_email(?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, email);
			call.registerOutParameter(2, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(2);
			
			Employee emp = null;
			if(rs.next()) {
				emp = employeeFromResultSet(rs);
			}
			
			return emp;
		}
	}

	@Override
	public Employee getEmployee(int id) throws SQLException {

		String sql = "{call read_employee_by_id(?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, id);
			call.registerOutParameter(2, OracleTypes.CURSOR);
			call.executeQuery();
			
			ResultSet rs = (ResultSet)call.getObject(2);
			
			Employee emp = null;
			if(rs.next()) {
				emp = employeeFromResultSet(rs);
			}
			
			return emp;
		}
	}
	
	@Override
	public byte[] getEmployeePassword(String email) throws SQLException {
		
		String sql = "select employee_password from employee "
				+ "where employee_email = ?";
		
		try(Connection conn = cf.getConnection()) {
			PreparedStatement p = conn.prepareStatement(sql);
			p.setString(1, email);
			
			ResultSet rs = p.executeQuery();
			
			if(rs.next()) {
				return rs.getBytes(1);
			}
		}
		
		return null;
	}
	
	@Override
	public void modifyEmployee(String email, Employee e) throws SQLException {

		String sql = "{call update_employee_with_Email(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, email);
			call.setString(2, e.getFirstname());
			call.setString(3, e.getLastname());
			call.setString(4, e.getStreetAddress());
			call.setString(5, e.getCity());
			call.setString(6, e.getState());
			call.setString(7, e.getZip());
			call.setInt(8, e.getSuperId());
			call.setInt(9, e.getDepartmentId());
			call.setString(10, e.getEmail());
			call.setBytes(11, e.getPassword());
			call.setString(12, e.getTitle());
			call.setInt(13, 0);
			call.setInt(14, 0);
			
			call.executeUpdate();
			LoggingService.getLogger().info("Employee with email " + email + " updated.");
		}

	}

	@Override
	public void modifyEmployee(int id, Employee e) throws SQLException {
		
		String sql = "{call update_employee_with_id(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try(Connection conn = cf.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, id);
			call.setString(2, e.getFirstname());
			call.setString(3, e.getLastname());
			call.setString(4, e.getStreetAddress());
			call.setString(5, e.getCity());
			call.setString(6, e.getState());
			call.setString(7, e.getZip());
			call.setInt(8, e.getSuperId());
			call.setInt(9, e.getDepartmentId());
			call.setString(10, e.getEmail());
			call.setBytes(11, e.getPassword());
			call.setString(12, e.getTitle());
			call.setInt(13, 0);
			call.setInt(14, 0);
			
			call.executeUpdate();
			LoggingService.getLogger().info("Employee " + id + " updated.");
		}

	}

	@Override
	public void deleteEmployee(String email) throws SQLException {

		String sql = "{call delete_employee_with_email(?)}";
		
		try (Connection conn = cf.getConnection()){
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, email);
			call.executeUpdate();
			LoggingService.getLogger().info("Deleted employee with email " + email);
		}

	}

	@Override
	public void deleteEmployee(int id) throws SQLException {
		
		String sql = "{call delete_employee_with_id(?)}";
		
		try (Connection conn = cf.getConnection()){
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, id);
			call.executeUpdate();
			LoggingService.getLogger().info("Deleted employee " + id);
		}

	}

	/**
	 * Get an employee object from a result set.
	 * @param rs The result set returned from a stored procedure.
	 * @return Employee object
	 * @throws SQLException
	 */
	private Employee employeeFromResultSet(ResultSet rs) throws SQLException {
		Employee emp = new Employee();
		emp.setId(rs.getInt(1));
		emp.setFirstname(rs.getString(2));
		emp.setLastname(rs.getString(3));
		emp.setStreetAddress(rs.getString("address_street_address"));
		emp.setCity(rs.getString("address_city"));
		emp.setState(rs.getString("address_state"));
		emp.setZip(rs.getString("address_zip"));
		emp.setSuperId(rs.getInt(8));
		emp.setSuperFirstname(rs.getString(9));
		emp.setSuperLastname(rs.getString(10));
		emp.setDepartment(rs.getString(11));
		emp.setDepartmentId(rs.getInt(12));
		emp.setEmail(rs.getString(13));
		emp.setTitle(rs.getString("employee_title_name"));
		return emp;
	}
}
