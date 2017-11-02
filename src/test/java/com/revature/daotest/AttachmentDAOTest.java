package com.revature.daotest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.trms.database.dao.AttachmentDAO;
import com.revature.trms.database.dao.AttachmentDAOImpl;
import com.revature.trms.models.Attachment;

public class AttachmentDAOTest {

	private static Attachment a;
	private static Attachment a2;
	private static AttachmentDAO dao;
	private static int id = 0;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dao = new AttachmentDAOImpl();
		System.out.println("Setting up...");
		a = new Attachment();
		a.setFilename("testFile");
		a.setDirectory("testDir");
		a.setRequestId(3);
		a.setApprovalType("pending");
		
		a2 = new Attachment();
		a2.setFilename("testFile2");
		a2.setDirectory("testDir2");
		a2.setRequestId(3);
		a2.setApprovalType("pending");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCreate() throws SQLException {
		try {
			dao.addAttachment(a);
		} catch (Exception e) {
			System.out.println("Attachment already added");
		}
		id = dao.getAttachments().get(0).getId();
		assertTrue("Couldn't add attachment.", id != 0);
	}
	
	@Test
	public void testReadAll() throws SQLException {
		try{
			dao.addAttachment(a2);
		} catch (Exception e) {
			System.out.println("Attachment already added");
		}
		assertTrue("Couldn't find any attachments.",
				dao.getAttachments().size() != 0);
	}

	@Test
	public void testReadByUser() throws SQLException {
		assertTrue("Couldn't find any attachments for user.",
				dao.getEmployeeAttachments(4).size() != 0);
	}
	
	@Test
	public void testReadByDept() throws SQLException {
		assertTrue("Couldn't find any attachments for request.",
				dao.getRequestAttachments(3).size() != 0);
	}
	
	@Test
	public void testUpdate() throws SQLException {
		dao.modifyAttachment(id, a2);
		List<Attachment> attachments = dao.getAttachments();
		for(Attachment att : attachments) {
			if(att.getId() == id) {
				if(!att.getFilename().equals(a2.getFilename())) {
					fail("Couldn't delete attachment.");
				}
				
			}
		}
	}
	
	@Test
	public void testDelete() throws SQLException {
		dao.deleteAttachment(id);
		List<Attachment> attachments = dao.getAttachments();
		for(Attachment att : attachments) {
			if(att.getId() == id) {
				fail("Couldn't delete attachment.");
			}
		}
	}
}
