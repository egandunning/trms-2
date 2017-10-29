package com.revature.daotest;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.crypto.Hash;
import com.revature.trms.database.dao.EmployeeDAO;
import com.revature.trms.database.dao.EmployeeDAOImpl;
import com.revature.trms.models.Employee;

public class EmployeeDAOTest {

	EmployeeDAO empDAO;
	
	Employee e;
	
	@Before
	public void setUp() throws Exception {
		
		empDAO = new EmployeeDAOImpl();
		
		e = new Employee();
		e.setFirstname("FnameTest");
		e.setLastname("LnameTest");
		e.setStreetAddress("123 Merry Ln");
		e.setCity("Pittsburgh");
		e.setState("PA");
		e.setZip("32165");
		e.setDepartment("sales");
		e.setDepartmentId(0);
		e.setEmail("testEmp1@revature.com");
		e.setPassword(Hash.pbkdf2("password".toCharArray()));
		e.setTitle("sales associate");
	
		try {
			empDAO.addEmployee(e);
		} catch (SQLException ex) {
			System.out.println("employee already added");
		}

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws SQLException {
		List<Employee> emps = empDAO.getEmployees();
		assertTrue("no emps found.", emps.size() == 1);
		
	}
	
	@Test
	public void testGetById() throws SQLException {
		Employee retrival = empDAO.getEmployee(4);
		assertTrue("emp not found", retrival.getFirstname().equals("FnameTest"));
	}
	
	@Test
	public void testGetByEmail() throws SQLException {
		Employee retrival = empDAO.getEmployee("testEmp1@revature.com");
		assertTrue("emp not found", retrival.getFirstname().equals("FnameTest"));
	}
	
	@Test
	public void testGetByDept() throws SQLException {
		List<Employee> emps = empDAO.getEmployees(0);
		assertTrue("no employees in sales dept found", emps.size() > 0);
	}
	
	@Test
	public void testGetByDeptName() throws SQLException {
		List<Employee> emps = empDAO.getEmployees("sales");
		assertTrue("no employees in sales dept found", emps.size() > 0);
	}

	@Test
	public void testUpdateById() throws SQLException {
		e.setFirstname("Updated");
		e.setLastname("Updated");
		e.setSuperId(4);
		empDAO.modifyEmployee(4, e);
		Employee modified = empDAO.getEmployee(4);
		assertTrue("Employee didnt get modified", 
				modified.getFirstname().equals(e.getFirstname())
				&& modified.getLastname().equals(e.getLastname()));
		e.setFirstname("FnameTest");
		empDAO.modifyEmployee(4, e);
	}
	
	@Test
	public void testUpdateByEmail() throws SQLException {
		e.setFirstname("Updated");
		e.setLastname("Updated");
		e.setSuperId(4);
		empDAO.modifyEmployee("testEmp1@revature.com", e);
		Employee modified = empDAO.getEmployee(4);
		assertTrue("Employee didnt get modified", 
				modified.getFirstname().equals(e.getFirstname())
				&& modified.getLastname().equals(e.getLastname()));
		e.setFirstname("FnameTest");
		empDAO.modifyEmployee(4, e);
	}
	
	@Test
	public void testDuplicateInfo() throws SQLException {
		Employee e2 = new Employee();
		e2.setFirstname("FnameTest2");
		e2.setLastname("LnameTest2");
		e2.setStreetAddress("123 Merry Ln");
		e2.setCity("Pittsburgh");
		e2.setState("PA");
		e2.setSuperId(4);
		e2.setZip("32165");
		e2.setDepartment("sales");
		e2.setDepartmentId(0);
		e2.setEmail("testEmp2@revature.com");
		e2.setPassword(Hash.pbkdf2("password".toCharArray()));
		e2.setTitle("sales associate");
		empDAO.addEmployee(e2);
		assertTrue("Couldnt add employee",
				empDAO.getEmployee("testEmp2@revature.com")
				.getFirstname().equals("FnameTest2"));
	}
}
