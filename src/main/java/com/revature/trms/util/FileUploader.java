package com.revature.trms.util;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.servlet.http.Part;

import com.revature.logging.LoggingService;

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
		
		try {
			//TODO: use the real AttachmentDAO, finish implementation
			//AttachmentDAO.add(filename,requestId,path)
			
			//Save file on server.
			filePart.write(filename);
			throw new SQLException();
			//return true;
		} catch (SQLException e) {
			LoggingService.getLogger().warn("Error saving file info in database.", e);
		} catch (IOException e) {
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
