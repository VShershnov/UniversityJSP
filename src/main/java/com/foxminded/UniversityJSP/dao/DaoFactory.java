package com.foxminded.UniversityJSP.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DaoFactory{
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	//URL адрес
	private String url = "jdbc:postgresql://[::1]:5432/University?user=postgres&password=1111";
	private String driver = "org.postgresql.Driver";//Имя драйвера
   	
	public Connection getConnection() throws PersistException{
		Connection connection = null;
		try {
			log.trace("Create connection");
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
        	log.error("Cannot set connection ", e);
        	throw new PersistException(e);
        }
        return  connection;		
	}

	public PostgreSqlGroupDao getGroupDao() throws PersistException{		
		return new PostgreSqlGroupDao(this);
	}

	public PostgreSqlStudentDao getStudentDao() throws PersistException{
		return new PostgreSqlStudentDao(this);
	}
	
	public PostgreSqlProfessorDao getProfessorDao() throws PersistException{
		return new PostgreSqlProfessorDao(this);
	}

	public PostgreSqlRoomDao getRoomDao() throws PersistException{
		return new PostgreSqlRoomDao(this);
	}
	
	public PostgreSqlCourseDao getCourseDao() throws PersistException{
		return new PostgreSqlCourseDao(this);
	}
	
	public PostgreSqlScheduleSlotDao getScheduleSlotDao() throws PersistException{
		return new PostgreSqlScheduleSlotDao(this);
	}	
	
	public PostgreSqlTimeUnitDao getTimeUnitDao() throws PersistException{
		return new PostgreSqlTimeUnitDao(this);
	}
	
	public DaoFactory(){
		try {
			log.trace("register jdbc driver");
            Class.forName(driver);//Регистрируем драйвер
        } catch (ClassNotFoundException e) {
        	log.error("Cannot find jdbc driver ", e);
            e.printStackTrace();
        }
    }
}
