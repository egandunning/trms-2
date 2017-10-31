package com.revature.trms.servlets;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
public class Upload extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		System.out.println("INSIDE /Upload");
		System.out.println("test getting param : " + request.getParameter("test"));
		
		Part filePart = request.getPart("file");
		//String path = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		//String content = filePart.getInputStream().toString();
		
		filePart.write("newFile.txt");
		
		
		//System.out.println("Filename: " + path);
		//System.out.println("data: " + content);
		if(request.getSession(false) == null) {
			//TODO: send redirect
			//response.sendRedirect("");//redirect to login page
			return;
		}
		
		
		
	}

}
