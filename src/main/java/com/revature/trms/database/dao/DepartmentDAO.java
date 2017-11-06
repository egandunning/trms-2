package com.revature.trms.database.dao;

import java.sql.SQLException;
import java.util.List;

import com.revature.trms.models.Department;

public interface DepartmentDAO {

	public void addDepartment(Department dept) throws SQLException;
	
	public List<Department> getDepartments() throws SQLException;
	
	public Department getDepartment(int id) throws SQLException;
	
	public void modifyDepartment(int id, String name) throws SQLException;
	
	public void deleteDepartment(int id) throws SQLException;
}
