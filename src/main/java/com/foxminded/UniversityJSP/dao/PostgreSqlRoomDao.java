package com.foxminded.UniversityJSP.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.Room;

public class PostgreSqlRoomDao extends AbstractPostgreSqlDao<Room, Integer>{
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	public PostgreSqlRoomDao(DaoFactory daoFactory) {
		super(daoFactory);
	}

	@Override
	public String getSelectQuery() {
		return "SELECT id, capacity, address FROM \"Rooms\"";
	}

	@Override
	public String getCreateQuery() {
		return "INSERT INTO \"Rooms\" (capacity, address) \n" +
                "VALUES (?, ?);";
	}

	@Override
	public String getUpdateQuery() {
		 return "UPDATE \"Rooms\" SET capacity = ?, address = ? WHERE id= ?;";
	}

	@Override
	public List<String> getDeleteQuery() {
		List<String> sql = new ArrayList<>();
		sql.add("UPDATE \"ScheduleSlots\" SET room_id = null WHERE room_id = ?;");
		sql.add("DELETE FROM \"Rooms\" WHERE id= ?;");
		return sql;
	}

	public Room create(Integer capacity, String address) throws PersistException {
		log.info("Creating new room with capacity=" + capacity + " , address=" + address);
		Room r = new Room(null, capacity, address);
        return persist(r);
	}
	
	@Override
	protected List<Room> parseResultSet(ResultSet rs) throws PersistException {
		List<Room> result = new ArrayList<Room>();
		log.debug("Parse Result Set from DB to Object's List");
		try {
            while (rs.next()) {
            	if(log.isEnabled(Level.TRACE))
            		log.trace("Parse row " + rs.getInt("id") + " to Room Object");
            	Room room = new Room();
            	room.setId(rs.getInt("id"));
            	room.setCapacity(rs.getInt("capacity"));
            	room.setAddress(rs.getString("address")); 
                result.add(room);
            }
        } catch (Exception e) {
        	log.error("Cannot parse Object ", e);
            throw new PersistException(e);
        }
        return result;
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement statement, Room object) throws PersistException {
		try {           
			log.debug("Prepare Statement for insert to DB");
			statement.setInt(1, object.getCapacity());
            statement.setString(2, object.getAddress());
        } catch (Exception e) {
        	log.error("Cannot create Statement for insert ", e);
        	throw new PersistException(e);
        }		
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement,
			Room object) throws PersistException {
		try {
			log.debug("Prepare Statement for update to DB");
			statement.setInt(1, object.getCapacity());            
            statement.setString(2, object.getAddress());
            statement.setInt(3, object.getId());
        } catch (Exception e) {
        	log.error("Cannot create Statement for update ", e);
        	throw new PersistException(e);
        }		
	}	
}
