package com.revature.daotest;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.trms.database.dao.DepartmentDAO;
import com.revature.trms.database.dao.DepartmentDAOImpl;
import com.revature.trms.models.Department;

public class DepartmentDAOTest {

	private static Department dept1;
	private static Department dept2;
	private static DepartmentDAO dao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		dao = new DepartmentDAOImpl();
		
		dept1 = new Department();
		dept1.setId(3);
		dept1.setName("Test dept 1");
		
		dept2 = new Department();
		dept2.setId(4);
		dept2.setName("Test dept 2");
		
		dao.addDepartment(dept1);
		dao.addDepartment(dept2);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dao.deleteDepartment(3);
		dao.deleteDepartment(4);
	}

	@Test
	public void testReadDepartments() throws Exception {
		List<Department> depts = dao.getDepartments();
		System.out.println(depts);
		
		Department foundDept = dao.getDepartment(4);
		System.out.println(foundDept.getName());
		
		assertTrue("No depts found", depts.size() != 0);
		assertTrue("No dept found",
				foundDept.getName().equals(dept2.getName()));
	}
	
	@Test
	public void testModifyDepartment() throws Exception {
		dao.modifyDepartment(3, "new dept name");
		
		Department foundDept = dao.getDepartment(3);
		System.out.println(foundDept.getName());
		
		assertTrue("Dept not modified", foundDept.getName().equals("new dept name"));
	}

}
