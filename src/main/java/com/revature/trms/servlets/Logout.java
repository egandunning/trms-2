package com.revature.trms.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			response.setStatus(300);//redirect to login page
			response.setHeader("Location", "login.html");
		} else {
			
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(Cookie c : cookies) {
					System.out.println("cookie found");
					c.setValue("");
					c.setPath("/");
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
			
			session.invalidate();
			response.setStatus(300);
			response.setHeader("Location", "login.html");
			
		}
	}

}
