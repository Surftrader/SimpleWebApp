package com.poseal.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poseal.beans.Product;
import com.poseal.utils.DBUtils;
import com.poseal.utils.MyUtils;

import javax.servlet.RequestDispatcher;

@WebServlet(urlPatterns= {"/createProduct"})
public class CreateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public CreateProductServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
		dispatcher.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = MyUtils.getStoredConnection(req);
		
		String code = (String) req.getParameter("code");
		String name = (String) req.getParameter("name");
		String priceStr = (String) req.getParameter("price");
		float price = 0;
		
		try {
			price = Float.parseFloat(priceStr);
		} catch (Exception e) {
		}
		
		Product product = new Product(code, name, price);
		String errorString = null;
		String regex = "\\w+";
		
		if(code == null || !code.matches(regex)) {
			errorString = "Product Coge invalid!";
		}
		
		if (errorString == null) {
			try {
				DBUtils.insertProduct(conn, product);
			} catch (SQLException e) {
				e.printStackTrace();
				errorString = e.getMessage();
			}
		}
		
		req.setAttribute("errorString", errorString);
		req.setAttribute("product", product);
		
		if (errorString != null) {
            RequestDispatcher dispatcher = req.getServletContext()
                    .getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
            dispatcher.forward(req, resp);
        } else {
        	resp.sendRedirect(req.getContextPath() + "/productList");
        }
		
	}
}
