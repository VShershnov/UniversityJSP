package com.foxminded.UniversityJSP.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.Course;

public class PostgreSqlCourseDao extends AbstractPostgreSqlDao<Course, Integer>{

	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	public PostgreSqlCourseDao(DaoFactory daoFactory) {
		super(daoFactory);		
	}

	@Override
	public String getSelectQuery() {
		return "SELECT id, name, duration FROM \"Courses\"";
	}
	
	@Override
	public String getCreateQuery() {
		return "INSERT INTO \"Courses\" (name, duration) \n" +
                "VALUES (?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE \"Courses\" SET name = ?, duration = ? WHERE id= ?;";
	}

	@Override
	public List<String> getDeleteQuery() {
		List<String> sql = new ArrayList<>();		
		sql.add("DELETE FROM Professors_Courses WHERE course_id = ?;");
		sql.add("DELETE FROM Groups_Courses WHERE course_id = ?;");
		sql.add("UPDATE \"ScheduleSlots\" SET course_id = null WHERE course_id = ?;");
		sql.add("DELETE FROM \"Courses\" WHERE id= ?;");
		return sql;
	}

	public Course create(String name, Integer duration) throws PersistException {
		log.info("Creating new course " + name + " , duration=" + duration);
		Course c = new Course(null, name, duration);
        return persist(c);
	}	
	
	@Override
	protected List<Course> parseResultSet(ResultSet rs) throws PersistException {
		List<Course> result = new ArrayList<Course>();
		log.debug("Parse Result Set from DB to Object's List");
		try {
            while (rs.next()) {
            	if(log.isEnabled(Level.TRACE))
            		log.trace("Parse row " + rs.getInt("id") + " to Course Object");
            	Course course = new Course();
            	course.setId(rs.getInt("id"));
            	course.setDuration(rs.getInt("duration"));
            	course.setName(rs.getString("name")); 
                result.add(course);
            }
        } catch (Exception e) {
        	log.error("Cannot parse Object ", e);
            throw new PersistException(e);
        }
        return result;
	}	

	@Override
	protected void prepareStatementForInsert(PreparedStatement statement,
			Course object) throws PersistException {
		try {           
			log.debug("Prepare Statement for insert to DB");
			statement.setInt(2, object.getDuration());
            statement.setString(1, object.getName());
        } catch (Exception e) {
        	log.error("Cannot create Statement for insert ", e);
        	throw new PersistException(e);
        }		
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement,
			Course object) throws PersistException {
		try {
			log.debug("Prepare Statement for update to DB");
			statement.setInt(2, object.getDuration());
            statement.setString(1, object.getName());
            statement.setInt(3, object.getId());
        } catch (Exception e) {
        	log.error("Cannot create Statement for update ", e);
        	throw new PersistException(e);
        }		
	}
}
