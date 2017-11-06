package com.revature.trms.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.logging.LoggingService;
import com.revature.trms.database.ConnectionFactory;
import com.revature.trms.models.Department;

public class DepartmentDAOImpl implements DepartmentDAO {

	ConnectionFactory cf;
	
	public DepartmentDAOImpl() {
		cf = ConnectionFactory.getInstance();
	}
	
	@Override
	public void addDepartment(Department dept) throws SQLException {
		
		if(dept == null) {
			LoggingService.getLogger()
				.info("Attempted to add empty department to the DB");
			return;
		}
		
		String sql = "insert into department (department_id, department_name) "
				+ "values (?,?)";
		
		try(Connection conn = cf.getConnection()) {
			
			PreparedStatement p = conn.prepareStatement(sql);
			p.setInt(1, dept.getId());
			p.setString(2, dept.getName());
			
			if(p.executeUpdate() == 1) {
				LoggingService.getLogger()
					.info("Department " + dept.getName() + " added to DB.");
			}
			
		}

	}

	@Override
	public List<Department> getDepartments() throws SQLException {

		String sql = "select department_id, department_name "
				+ "from department";
		
		try(Connection conn = cf.getConnection()) {
			PreparedStatement p = conn.prepareStatement(sql);
			ResultSet rs = p.executeQuery();
			
			List<Department> depts = new ArrayList<Department>();
			
			while(rs.next()) {
				Department dept = new Department();
				dept.setId(rs.getInt(1));
				dept.setName(rs.getString(2));
				depts.add(dept);
			}
			return depts;
		}
	}

	@Override
	public Department getDepartment(int id) throws SQLException {

		String sql = "select department_name from "
				+ "department where department_id = ?";
		
		try(Connection conn = cf.getConnection()) {
			PreparedStatement p = conn.prepareStatement(sql);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			
			Department dept = new Department();
			
			if(rs.next()) {
				dept.setId(id);
				dept.setName(rs.getString(1));
			}
			
			return dept;
			
		}
	}

	@Override
	public void modifyDepartment(int id, String newName) throws SQLException {

		String sql = "update department set "
				+ "department_name=? where department_id=?";
		
		try(Connection conn = cf.getConnection()) {
			PreparedStatement p = conn.prepareStatement(sql);
			p.setString(1, newName);
			p.setInt(2, id);
			if(p.executeUpdate() == 1) {
				LoggingService.getLogger().info("Department " + id
						+ " name changed to " + newName);
			}
		}

	}

	@Override
	public void deleteDepartment(int id) throws SQLException {
		
		String sql = "delete from department where department_id=?";
		
		try(Connection conn = cf.getConnection()) {
			PreparedStatement p = conn.prepareStatement(sql);
			p.setInt(1, id);
			if(p.executeUpdate() == 1) {
				LoggingService.getLogger()
					.info("Department " + id + " deleted.");
			}
		}

	}

}
