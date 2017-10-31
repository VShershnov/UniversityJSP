package com.foxminded.UniversityJSP.servlet;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


import com.foxminded.UniversityJSP.dao.DaoFactory;

@WebListener
public class AppContextListener implements ServletContextListener{

	public void contextInitialized(ServletContextEvent servletContextEvent) {
    	ServletContext ctx = servletContextEvent.getServletContext();
    	try {
			DaoFactory daoFactory = new DaoFactory();
			ctx.setAttribute("DBConnection", daoFactory.getConnection());
			System.out.println("DB Connection initialized successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	Connection con = (Connection) servletContextEvent.getServletContext().getAttribute("DBConnection");
    	try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}