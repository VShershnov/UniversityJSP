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

public class PostgreSqlGroupDao extends AbstractPostgreSqlDao<Group, Integer>{
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	public PostgreSqlGroupDao(DaoFactory daoFactory) {
		super(daoFactory);
	}

	@Override
	public String getSelectQuery() {
		return "SELECT id, name FROM \"Groups\"";
	}
	
	/**
	 * ¬озвращает sql запрос дл€ получени€ всех id св€занного обьекта из таблицы св€зей ForeignKey.
	 * <p/>
	 * SELECT dependentObj_id FROM [FK_Table] WHERE Instance_id = ?;
	 *  
	 */
	@Override
	public String getSelectDependentObjQuery(Class<?> K1, Class<?> T){
		String sqlCourse = "SELECT " + K1.getSimpleName() + "_id FROM " + T.getSimpleName() + "s_" 
				+ K1.getSimpleName() + "s WHERE " + T.getSimpleName() + "_id = ?"
				;
		String sqlStudent = "SELECT " + K1.getSimpleName() + "_id FROM " 
				+ K1.getSimpleName() + "s WHERE " + T.getSimpleName() + "_id = ?";
		
		return K1.getClass().equals(Student.class) ? sqlStudent : sqlCourse;
	}
	
	/**
	 * ¬озвращает sql запрос дл€ добавлени€ записи о id св€занного обьекта в таблицу св€зей ForeignKey.
	 * <p/>
	 * INSERT INTO PROFESSORS_COURSES (professor_id, course_id) VALUES (?, ?);
	 *  
	 */
	@Override
	public String getInsertDependentObjQuery(Class<?> K1, Class<?> T) {		
		String sqlCourse = "INSERT INTO " + T.getSimpleName() 
				+ "s_" + K1.getSimpleName() + "s (" + T.getSimpleName() + "_id, " 
				+ K1.getSimpleName() + "_id) VALUES (?, ?);";
		
		String sqlStudent = "UPDATE " + K1.getSimpleName() + "s SET " 
				+ T.getSimpleName() + "_id = ? WHERE " + K1.getSimpleName() + "_id = ?;";
		
		return K1.getClass().equals(Student.class) ? sqlStudent : sqlCourse;
	}

	/**
	 * ¬озвращает sql запрос дл€ добавлени€ записи о id св€занного обьекта в таблицу св€зей ForeignKey.
	 * <p/>
	 * INSERT INTO PROFESSORS_COURSES (professor_id, course_id) VALUES (?, ?);
	 *  
	 */
	@Override
	public String getDeleteDependentObjQuery(Class<?> K1, Class<?> T) {		
		String sqlCourse = "DELETE FROM " + T.getSimpleName() + "s_"  + K1.getSimpleName() + "s WHERE " 
				+ T.getSimpleName() + "_id= ? AND " + K1.getSimpleName() + "_id=? ;";
		String sqlStudent = "UPDATE " + K1.getSimpleName() + "s SET " 
				+ T.getSimpleName() + "_id = null WHERE " + T.getSimpleName() + "_id= ? AND " + K1.getSimpleName() + "_id=?;";
		
		return K1.getClass().equals(Student.class) ? sqlStudent : sqlCourse;
	}
	
	@Override
	public String getCreateQuery() {
		return "INSERT INTO \"Groups\" (name) \n" +
                "VALUES (?);";
	}	
	
	@Override
	public String getUpdateQuery() {
		 return "UPDATE \"Groups\" SET name = ? WHERE id= ?;";
	}

	@Override
	public List<String> getDeleteQuery() {
		List<String> sql = new ArrayList<>();		
		sql.add("DELETE FROM Groups_Students WHERE group_id = ?;");
		sql.add("DELETE FROM Groups_Courses WHERE group_id = ?;");
		sql.add("UPDATE \"ScheduleSlots\" SET group_id = null WHERE group_id = ?;");
		sql.add("UPDATE \"Students\" SET group_id = null WHERE group_id = ?;");
		sql.add("DELETE FROM \"Groups\" WHERE id= ?;");
		return sql;
	}
	
	public Group create(String name) throws PersistException{
		log.info("Creating new group " + name);
		Group g = new Group(null, name);
        return persist(g);
	}

	@Override
	protected List<Group> parseResultSet(ResultSet rs) throws PersistException {
		List<Group> result = new ArrayList<Group>();
		log.debug("Parse Result Set from DB to Object's List");
        try {
            while (rs.next()) {
            	if(log.isEnabled(Level.TRACE))
            		log.trace("Parse row " + rs.getInt("id") + " to Group Object");
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));               
                result.add(group);
            }
        } catch (Exception e) {
        	log.error("Cannot parse Object ", e);
            throw new PersistException(e);
        }
        return result;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement statement,
			Group object) throws PersistException {
		try {
			log.debug("Prepare Statement for insert to DB");
            statement.setString(1, object.getName());
        } catch (Exception e) {
        	log.error("Cannot create Statement for insert ", e);
        	throw new PersistException(e);
        }		
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement,
			Group object) throws PersistException {
		try {
			log.debug("Prepare Statement for update to DB");
			statement.setString(1, object.getName());            
            statement.setInt(2, object.getId());
        } catch (Exception e) {
        	log.error("Cannot create Statement for update ", e);
        	throw new PersistException(e);
        }		
	}	
}
