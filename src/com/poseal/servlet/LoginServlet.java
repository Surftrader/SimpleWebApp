package com.poseal.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poseal.beans.UserAccount;
import com.poseal.utils.DBUtils;
import com.poseal.utils.MyUtils;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public LoginServlet() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
		dispatcher.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		String rememberMeStr = req.getParameter("rememberMe");
		boolean remember = "Y".equals(rememberMeStr);
		
		UserAccount user = null;
		boolean hasError = false;
		String errorString = null;
		
		if (userName == null || password == null || userName.length() == 0 || password.length() == 0) {
			hasError = true;
			errorString = "Required username and password!";
		} else {
			Connection conn = MyUtils.getStoredConnection(req);
			try {
				user = DBUtils.findUser(conn, userName, password);
				if(user == null) {
					hasError = true;
					errorString = "User Name or password invalid";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				hasError = true;
				errorString = e.getMessage();
			}
		}
		
		if(hasError) {
			user = new UserAccount();
			user.setUserName(userName);
			user.setPassword(password);
			
			req.setAttribute("errorString", errorString);
			req.setAttribute("user", user);
			
			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB/views/loginView.jsp");
			dispatcher.forward(req, resp);
		} else {
			HttpSession session = req.getSession();
			MyUtils.storeLoginedUser(session, user);
			
			if(remember) {
				MyUtils.storeUserCookie(resp, user);
			} else {
				MyUtils.deleteUserCookie(resp);
			}
			resp.sendRedirect(req.getContextPath() + "/userInfo");
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
