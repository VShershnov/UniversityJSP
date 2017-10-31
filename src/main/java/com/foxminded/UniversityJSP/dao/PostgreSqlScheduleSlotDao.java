package com.foxminded.UniversityJSP.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.schedule.ScheduleSlot;
import com.foxminded.UniversityJSP.university.schedule.TimeUnit;
import com.foxminded.UniversityJSP.university.Course;
import com.foxminded.UniversityJSP.university.Group;
import com.foxminded.UniversityJSP.university.Room;
import com.foxminded.UniversityJSP.university.person.Professor;

public class PostgreSqlScheduleSlotDao extends
		AbstractPostgreSqlDao<ScheduleSlot, Integer> {
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());

	public PostgreSqlScheduleSlotDao(DaoFactory daoFactory) {
		super(daoFactory);		
	}

	@Override
	public String getSelectQuery() {
		return "SELECT id, room_id, course_id, professor_id, group_id, timeunit_id FROM \"ScheduleSlots\"";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO \"ScheduleSlots\" (room_id, course_id, professor_id, group_id, timeunit_id) \n" +
                "VALUES (?, ?, ?, ?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE \"ScheduleSlots\" SET room_id = ?, course_id = ?, professor_id = ?, group_id = ?, timeunit_id = ? WHERE id= ?;";
	}

	@Override
	public List<String> getDeleteQuery() {
		List<String> sql = new ArrayList<>();
		sql.add("DELETE FROM \"ScheduleSlots\" WHERE id= ?;");
		return sql;
	}

	public ScheduleSlot create(TimeUnit time, Room room, Course course, Professor professor, Group group) throws 
			PersistException {
		log.info("Creating new ScheduleSlot " + time.toString() + " room = "+ room.getAddress() +
				" course = " + course.getName());
		ScheduleSlot ss = new ScheduleSlot(null, time, room, course, professor, group);
        return persist(ss);
	}
	
	@Override
	protected List<ScheduleSlot> parseResultSet(ResultSet rs)
			throws PersistException {
		List<ScheduleSlot> result = new ArrayList<>();
		log.debug("Parse Result Set from DB to Object's List");
		try {
            while (rs.next()) {
            	if(log.isEnabled(Level.TRACE))
            		log.trace("Parse row " + rs.getInt("id") + " to ScheduleSlot Object");
            	ScheduleSlot ss = new ScheduleSlot();
            	ss.setId(rs.getInt("id"));
            	ss.setRoom(daoFactory.getRoomDao().getByPK(rs.getInt("room_id")));
            	ss.setCourse(daoFactory.getCourseDao().getByPK(rs.getInt("course_id")));
            	ss.setProfessor(daoFactory.getProfessorDao().getByPK(rs.getInt("professor_id")));
            	ss.setGroup(daoFactory.getGroupDao().getByPK(rs.getInt("group_id")));
            	ss.setTime(daoFactory.getTimeUnitDao().getByPK(rs.getInt("timeunit_id")));            	
                result.add(ss);
            }
        } catch (Exception e) {
        	log.error("Cannot parse Object ", e);
            throw new PersistException(e);
        }
        return result;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement statement,
			ScheduleSlot object) throws PersistException {
		try {           
			log.debug("Prepare Statement for insert to DB");
			statement.setInt(1, object.getRoom().getId());
			statement.setInt(2, object.getCourse().getId());
			statement.setInt(3, object.getProfessor().getId());
			statement.setInt(4, object.getGroup().getId());
			statement.setInt(5, object.getTime().getId());
        } catch (Exception e) {
        	log.error("Cannot create Statement for insert ", e);
        	throw new PersistException(e);
        }
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement,
			ScheduleSlot object) throws PersistException {
		try {
			log.debug("Prepare Statement for update to DB");
			statement.setInt(1, object.getRoom().getId());
			statement.setInt(2, object.getCourse().getId());
			statement.setInt(3, object.getProfessor().getId());
			statement.setInt(4, object.getGroup().getId());
			statement.setInt(5, object.getTime().getId());
            statement.setInt(6, object.getId());
        } catch (Exception e) {
        	log.error("Cannot create Statement for update ", e);
        	throw new PersistException(e);
        }
	}
}
