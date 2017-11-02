package com.revature.trms.database.dao;

import java.sql.SQLException;
import java.util.List;

import com.revature.trms.models.Attachment;

public interface AttachmentDAO {

	public void addAttachment(Attachment a) throws SQLException;
	
	public List<Attachment> getAttachments() throws SQLException;
	
	public List<Attachment> getEmployeeAttachments(int employeeId) throws SQLException;
	
	public List<Attachment> getRequestAttachments(int requestId) throws SQLException;
	
	public void modifyAttachment(int oldId, Attachment a) throws SQLException;
	
	public void deleteAttachment(int id) throws SQLException;
}
