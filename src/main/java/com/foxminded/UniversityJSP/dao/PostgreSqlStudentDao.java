package com.foxminded.UniversityJSP.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.Group;
import com.foxminded.UniversityJSP.university.person.Student;

public class PostgreSqlStudentDao extends AbstractPostgreSqlDao<Student, Integer>{

	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	public PostgreSqlStudentDao(DaoFactory daoFactory) {
		super(daoFactory);		
	}

	@Override
	public String getSelectQuery() {
		return "SELECT id, fullName, group_id FROM \"Students\" ";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO \"Students\" (fullName, group_id) \n" +
                "VALUES (?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE \"Students\" \n" +
                "SET fullName = ?, group_id = ? \n" +
                "WHERE id = ?;";
	}

	@Override
	public List<String> getDeleteQuery() {
		List<String> sql = new ArrayList<>();		
		sql.add("DELETE FROM \"Students\" WHERE id= ?;");
		return sql;
	}

	public Student create(String name, Group group) throws PersistException {
		Student s = new Student(name, group);
		log.info("Creating new student " + name, "Group = " + group.toString());
        return persist(s);
	}	

	@Override
	protected List<Student> parseResultSet(ResultSet rs) throws PersistException{
		List<Student> result = new ArrayList<Student>();
		log.debug("Parse Result Set from DB to Object's List");
		
		try {
            while (rs.next()) {
            	if(log.isEnabled(Level.TRACE))
            		log.trace("Parse row " + rs.getInt("id") + " to Student Object");
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setFullName(rs.getString("fullName"));               
                student.setGroup(daoFactory.getGroupDao().getByPK(rs.getInt("group_id")));
                result.add(student);
            }
        } catch (Exception e) {
        	log.error("Cannot parse Object ", e);
            throw new PersistException(e);
        }
        return result;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement statement,
			Student object) throws PersistException {
        try {
        	log.debug("Prepare Statement for insert to DB");
            statement.setString(1, object.getFullName());
            statement.setInt(2, object.getGroup().getId());
        } catch (Exception e) {
        	log.error("Cannot create Statement for insert ", e);
            throw new PersistException(e);
        }		
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement,
			Student object) throws PersistException {
        try {
        	log.debug("Prepare Statement for update to DB");
            statement.setString(1, object.getFullName());
            statement.setInt(2, object.getGroup().getId());
            statement.setInt(3, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }		
	}	
}
