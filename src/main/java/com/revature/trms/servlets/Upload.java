package com.revature.trms.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
		
		HttpSession currentSession = request.getSession(false);
		
		if(currentSession == null) {
			System.out.println("No session... Redirecting.");
			response.setStatus(300);
			response.setHeader("Location", "login.html");
			return;
		}
		
		//TODO: handle the alert message, set message to empty
		
		//Get tuition reimbursement request id
		int requestId = -1;
		
		try {
			requestId = Integer.parseInt(request.getParameter("requestId"));
		} catch (NumberFormatException e) {
			LoggingService.getLogger().warn("No requestId parameter sent to"
					+ " Upload.doPost.", e);
			currentSession.setAttribute("alert", "Choose a request first.");
			response.setStatus(300);
			response.setHeader("Location", "upload.html");
			PrintWriter out = response.getWriter();
			out.write("{\"alert\" : \"Invalid tuition reimbursement request\"}");
			return;
		}
		
		//Redirect if no request found
		if(requestId == -1) {
			currentSession.setAttribute("alert", "Choose a request first.");
			response.setStatus(300);
			response.setHeader("Location", "upload.html");
			PrintWriter out = response.getWriter();
			out.write("{\"alert\" : \"Choose a request first.\"}");
		}
		
		//Upload file
		Part filePart = request.getPart("file");
		if(FileUploader.upload(filePart, requestId)) {
			currentSession.setAttribute("alert", "Successfully uploaded attachment.");
			PrintWriter out = response.getWriter();
			out.write("{\"alert\" : \"Successfully uploaded attachment.\"}");
		} else {
			currentSession.setAttribute("alert", "Failed to upload attachment.");
			PrintWriter out = response.getWriter();
			out.write("{\"alert\" : \"Failed to upload attachment.\"}");
		}
	}
}
