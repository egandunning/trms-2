package com.revature.daotest;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.trms.database.dao.EventTypeDAO;
import com.revature.trms.database.dao.EventTypeDAOImpl;

public class EventTypeDAOTest {

	private static EventTypeDAO dao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dao = new EventTypeDAOImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() throws Exception {
		Map<Integer, Double> reimburseMap = dao.getReimbursementAmounts();
		assertTrue("couldnt retrieve map", reimburseMap.size() != 0);
		System.out.println(reimburseMap.get(0));
		assertTrue("map set up incorrectly", reimburseMap.get(0) > .7999999 &&
				reimburseMap.get(0) < .8000001);
		assertTrue("map set up incorrectly", reimburseMap.get(1) > .5999999 &&
				reimburseMap.get(1) < .6000001);
		assertTrue("map set up incorrectly", reimburseMap.get(2) > .7499999 &&
				reimburseMap.get(2) < .7500001);
	}

}
