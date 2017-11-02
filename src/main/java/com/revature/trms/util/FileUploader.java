package com.revature.trms.util;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.servlet.http.Part;

import com.revature.logging.LoggingService;
import com.revature.trms.database.dao.AttachmentDAO;
import com.revature.trms.database.dao.AttachmentDAOImpl;
import com.revature.trms.models.Attachment;

/**
 * A class to handle file uploads.
 * @author Egan Dunning
 *
 */
public class FileUploader {

	/**
	 * Given a Part object, add the 
	 * @param filePart
	 * @return
	 */
	public static boolean upload(Part filePart, int requestId) {
		String filename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		
		System.out.println("file to upload: " + filename);
		
		AttachmentDAO dao = new AttachmentDAOImpl();
		
		Attachment attachment = new Attachment();
		attachment.setFilename(filename);
		attachment.setDirectory("test");
		attachment.setApprovalType("pending");
		attachment.setRequestId(requestId);
		
		try {
			dao.addAttachment(attachment);
			
			//Save file on server.
			filePart.write(filename);
			throw new SQLException();
			//return true;
		} catch (SQLException e) {
			System.out.println("Error saving file info in database.");
			LoggingService.getLogger().warn("Error saving file info in database.", e);
		} catch (IOException e) {
			System.out.println("Error saving file info in database.");
			LoggingService.getLogger().warn("Error saving file on server.", e);
			try {
				//AttachmentDAO.delete(filename);
				throw new SQLException();
			} catch (SQLException e2) {
				LoggingService.getLogger().warn("Error deleting file info from database.", e);
			}
		}
		return false;
	}
}
