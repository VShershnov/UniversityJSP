package com.foxminded.UniversityJSP.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.schedule.TimeUnit;

public class PostgreSqlTimeUnitDao extends AbstractPostgreSqlDao<TimeUnit, Integer> {
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());

	public PostgreSqlTimeUnitDao(DaoFactory daoFactory) {
		super(daoFactory);
	}

	@Override
	public String getSelectQuery() {
		return "SELECT id, time, day, month FROM \"TimeUnit\"";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO \"TimeUnit\" (time, day, month) \n" +
                "VALUES (?, ?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE \"TimeUnit\" SET month = ?, day = ?, time = ? WHERE id= ?;";
	}

	@Override
	public List<String> getDeleteQuery() {
		List<String> sql = new ArrayList<>();
		sql.add("UPDATE \"ScheduleSlots\" SET timeunit_id = null WHERE timeunit_id = ?;");
		sql.add("DELETE FROM \"TimeUnit\" WHERE id= ?;");
		
		return sql;
	}

	public TimeUnit create(Integer time, Integer day, Integer month) throws PersistException {
		log.info("Creating new timeuinit " + time + day + month);
		TimeUnit tu = new TimeUnit(time, day, month);
        return persist(tu);
	}
	
	@Override
	protected List<TimeUnit> parseResultSet(ResultSet rs) throws PersistException {
		List<TimeUnit> result = new ArrayList<TimeUnit>();
		log.debug("Parse Result Set from DB to Object's List");
		try {
            while (rs.next()) {
            	if(log.isEnabled(Level.TRACE))
            		log.trace("Parse row " + rs.getInt("id") + " to TimeUnit Object");
            	TimeUnit timeUnit = new TimeUnit();
            	timeUnit.setDay(rs.getInt("day"));
            	timeUnit.setMonth(rs.getInt("month"));
            	timeUnit.setTime(rs.getInt("time"));
            	timeUnit.setId(rs.getInt("id"));            	
                result.add(timeUnit);
            }
        } catch (Exception e) {
        	log.error("Cannot parse Object ", e);
            throw new PersistException(e);
        }
        return result;
	}


	@Override
	protected void prepareStatementForInsert(PreparedStatement statement,
			TimeUnit object) throws PersistException {
		try {           
			log.debug("Prepare Statement for insert to DB");
			statement.setInt(1, object.getTime());
			statement.setInt(2, object.getDay());
			statement.setInt(3, object.getMonth());            
        } catch (Exception e) {
        	log.error("Cannot create Statement for insert ", e);
        	throw new PersistException(e);
        }		
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement,
			TimeUnit object) throws PersistException {
		try {
			log.debug("Prepare Statement for update to DB");
			statement.setInt(1, object.getMonth());
			statement.setInt(2, object.getDay());
			statement.setInt(3, object.getTime());
            statement.setInt(4, object.getId());
        } catch (Exception e) {
        	log.error("Cannot create Statement for update ", e);
        	throw new PersistException(e);
        }				
	}
}
