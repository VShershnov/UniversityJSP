package com.foxminded.UniversityJSP.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.person.Professor;

public class PostgreSqlProfessorDao extends AbstractPostgreSqlDao<Professor, Integer> {
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());

	public PostgreSqlProfessorDao(DaoFactory daoFactory) {
		super(daoFactory);
	}

	@Override
	public String getSelectQuery() {
		return "SELECT id, fullname FROM \"Professors\"";
	}

	/**
	 * ¬озвращает sql запрос дл€ получени€ всех id св€занного обьекта из таблицы св€зей ForeignKey.
	 * <p/>
	 * SELECT dependentObj_id FROM [FK_Table] WHERE Instance_id = ?;
	 *  
	 */
	@Override
	public String getSelectDependentObjQuery(Class<?> K1, Class<?> T){
		return "SELECT " + K1.getSimpleName() + "_id FROM " + T.getSimpleName() + "s_" 
						+ K1.getSimpleName() + "s WHERE " + T.getSimpleName() + "_id = ?";
	}
	
	/**
	 * ¬озвращает sql запрос дл€ добавлени€ записи о id св€занного обьекта в таблицу св€зей ForeignKey.
	 * <p/>
	 * INSERT INTO PROFESSORS_COURSES (professor_id, course_id) VALUES (?, ?);
	 *  
	 */
	@Override
	public String getInsertDependentObjQuery(Class<?> K1, Class<?> T) {		
		return "INSERT INTO " + T.getSimpleName() + "s_" 
				+ K1.getSimpleName() + "s (" + T.getSimpleName() + "_id, " + K1.getSimpleName() + "_id) VALUES (?, ?);";
	}
	
	/**
	 * ¬озвращает sql запрос дл€ добавлени€ записи о id св€занного обьекта в таблицу св€зей ForeignKey.
	 * <p/>
	 * INSERT INTO PROFESSORS_COURSES (professor_id, course_id) VALUES (?, ?);
	 *  
	 */
	@Override
	public String getDeleteDependentObjQuery(Class<?> K1, Class<?> T) {		
		return "DELETE FROM " + T.getSimpleName() + "s_"  + K1.getSimpleName() + "s WHERE " 
				+ T.getSimpleName() + "_id=? AND " + K1.getSimpleName() + "_id=? ;";
	}
	
	@Override
	public String getCreateQuery() {
		return "INSERT INTO \"Professors\" (fullname) \n" +
                "VALUES (?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE \"Professors\" SET fullname = ? WHERE id= ?;";
	}

	@Override
	public List<String> getDeleteQuery() {
		List<String> sql = new ArrayList<>();		
		sql.add("DELETE FROM Professors_Courses WHERE professor_id = ?;");
		sql.add("UPDATE \"ScheduleSlots\" SET professor_id = null WHERE professor_id = ?;");
		sql.add("DELETE FROM \"Professors\" WHERE id= ?;");
		return sql;
	}

	public Professor create(String name) throws PersistException{
		log.info("Creating new professor " + name);
		Professor prof = new Professor(null, name);
        return persist(prof);
	}
	
	@Override
	protected List<Professor> parseResultSet(ResultSet rs)
			throws PersistException {
		List<Professor> result = new ArrayList<Professor>();
		log.debug("Parse Result Set from DB to Object's List");
        try {
            while (rs.next()) {
            	if(log.isEnabled(Level.TRACE))
            		log.trace("Parse row " + rs.getInt("id") + " to Professor Object");
            	Professor prof = new Professor();
            	prof.setId(rs.getInt("id"));
            	prof.setFullName(rs.getString("fullname"));               
                result.add(prof);
            }
        } catch (Exception e) {
        	log.error("Cannot parse Object ", e);
            throw new PersistException(e);
        }
        return result;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement statement,
			Professor object) throws PersistException {
		try {
			log.debug("Prepare Statement for insert to DB");
            statement.setString(1, object.getFullName());
        } catch (Exception e) {
        	log.error("Cannot create Statement for insert ", e);
            throw new PersistException(e);
        }	
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement,
			Professor object) throws PersistException {
		try {
			log.debug("Prepare Statement for update to DB");
			statement.setString(1, object.getFullName());            
            statement.setInt(2, object.getId());
        } catch (Exception e) {
        	log.error("Cannot create Statement for update ", e);
            throw new PersistException(e);
        }	
	}
}
