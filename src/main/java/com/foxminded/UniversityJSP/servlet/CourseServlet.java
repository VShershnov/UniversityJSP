package com.foxminded.UniversityJSP.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.ldap.Rdn;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.Course;



@WebServlet(name = "Courses", urlPatterns = { "/courseServlet" })
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection con = (Connection) getServletContext().getAttribute("DBConnection");
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = con.prepareStatement("select * from \"Courses\"");				
				rs = ps.executeQuery();
				PrintWriter out= response.getWriter();
				if(rs != null && rs.next()){
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
					rd.include(request, response);
					while (rs.next()) {		            	
		            	Course course = new Course();
		            	course.setId(rs.getInt("id"));
		            	course.setDuration(rs.getInt("duration"));
		            	course.setName(rs.getString("name"));
						log.info("Course found with details=" + course);									
						out.println("<p> " + course + "</p>");
					}					
				}else{
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");					
					log.error("Course not found");
					out.println("<font color=red>No Course found, please register first.</font>");					
				}
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("Database connection problem");
				throw new ServletException("DB Connection problem.");
			}finally{
				try {
					rs.close();
					ps.close();
				} catch (SQLException e) {
					log.error("SQLException in closing PreparedStatement or ResultSet");
				}			
			}
		}
	}