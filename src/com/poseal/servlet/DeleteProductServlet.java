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

import com.poseal.utils.DBUtils;
import com.poseal.utils.MyUtils;

@WebServlet(urlPatterns= {"/deleteProduct"})
public class DeleteProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteProductServlet() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = MyUtils.getStoredConnection(req);
		
		String code = (String) req.getParameter("code");
		
		String errorString = null;
		
		try {
			DBUtils.deleteProduct(conn, code);
		} catch (SQLException e) {
			e.printStackTrace();
			errorString = e.getMessage();
		}
		
		if(errorString != null) {
			req.setAttribute("errorString", errorString);
			RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/WEB-INF/views/deleteProductErrorView.jsp");
			dispatcher.forward(req, resp);
		} else {
			resp.sendRedirect(req.getContextPath() + "/productList");
		}		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
