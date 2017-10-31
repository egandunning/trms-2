package com.revature.authtest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.trms.auth.AuthorizeEmployee;

public class AuthorizeEmployeeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void loginTest() {
		AuthorizeEmployee ae = new AuthorizeEmployee();
		
		assertTrue("couldn't login user",
				ae.login("testEmp1@revature.com", "password".toCharArray()));
		
		assertFalse("invalid login succeeded.",
				ae.login("testEmp1@revature.com", "wrong password".toCharArray()));
		
		assertFalse("invalid login succeeded.",
				ae.login("nonexistent email", "password".toCharArray()));
	}

}
