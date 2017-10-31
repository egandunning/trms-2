package com.revature.trms.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.revature.logging.LoggingService;
import com.revature.trms.util.FileUploader;

/**
 * Uploads a file to the web server. The file is stored in
 * [path to Eclipse workspace]\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\work\Catalina\localhost\TRMS
 * @author Egan Dunning
 *
 */
@MultipartConfig
public class Upload extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		LoggingService.getLogger().trace("Upload.doGet");
		
		//TODO: redirect if session is invalid
		
		Part filePart = request.getPart("file");
		if(FileUploader.upload(filePart)) {
			//TODO: success msg
		} else {
			//TODO: failure msg
		}
		
		
		
	}

}
