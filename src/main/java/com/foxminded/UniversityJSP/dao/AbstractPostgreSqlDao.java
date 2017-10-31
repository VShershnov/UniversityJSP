package com.foxminded.UniversityJSP.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Абстрактный класс предоставляющий базовую реализацию CRUD операций с использованием JDBC.
 *
 * @param <T>  тип объекта персистенции
 * @param <PK> тип первичного ключа
 */
public abstract class AbstractPostgreSqlDao<T extends Identified <PK>, PK extends Integer>{

	public AbstractPostgreSqlDao(DaoFactory daoFactory) {
	    this.daoFactory = daoFactory;
	}

	DaoFactory daoFactory;

	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
    /**
     * Возвращает sql запрос для получения всех записей.
     * <p/>
     * SELECT * FROM [Table]
     */
    public abstract String getSelectQuery();        
    
    /**
     * Возвращает sql запрос для вставки новой записи в базу данных.
     * <p/>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    public abstract String getCreateQuery();
    
    /**
     * Возвращает sql запрос для обновления записи.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();
    
    /**
     * Возвращает sql запрос для удаления записи из базы данных.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract List<String> getDeleteQuery();

    
    /**
     * Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.
     * @throws PersistException 
     */
    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;
    
    /**
     * Устанавливает аргументы insert запроса в соответствии со значением полей объекта object.
     */
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;

    /**
     * Устанавливает аргументы update запроса в соответствии со значением полей объекта object.
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;

    
    public T getByPK(int key) throws PersistException {    	
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE id = ?";
        
        log.info("Get object from DB by key "+ key);
        
        log.debug("Open connection & Create prepared statement");
        try (Connection connection = daoFactory.getConnection();
        	PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, key);
            
            log.debug("Get result set");
            ResultSet rs = statement.executeQuery();
            
            log.debug("Parse result set");
            list = parseResultSet(rs);
            
        log.debug("statement&connection closed");   
        } catch (Exception e) {
        	log.debug("statement&connection closed");
        	log.error("Cannot find Object by id ", e);
            throw new PersistException(e);
        }
        
        if (list == null || list.size() == 0) {
        	log.info("returned object = null");
            return null;
        }
        if (list.size() > 1) {
        	String s = "Received more than one record.";
        	log.error(s);
            throw new PersistException(s);
        }
        return list.iterator().next();
    }
    
    
    public List<T> getAll() throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        
        log.info("Get all objects from DB");
        
        log.debug("Open connection & Create prepared statement & Get result set");
        try (Connection connection = daoFactory.getConnection();
        	PreparedStatement statement = connection.prepareStatement(sql);
        	ResultSet rs = statement.executeQuery()) {
        	
        	log.debug("Parse result set");
            list = parseResultSet(rs);
            
            log.debug("resultset & statement & connection closed");   
        } catch (Exception e) {
        	log.debug("resultset & statement & connection closed");  
        	log.error("Cannot return any Object ", e);
            throw new PersistException(e);
        }
        return list;
    }    
    
    public T persist(T object) throws PersistException {
        T persistInstance;
        int id = -1;
        
        // Добавляем запись
        String sql = getCreateQuery();
        
        log.info("Create Object " + object.toString());
        
        log.debug("Open connection");
        try (Connection connection = daoFactory.getConnection()) {
        	log.debug("Create prepared statement");
        	PreparedStatement statement = connection.prepareStatement(sql, new String[] {"id"});
        	prepareStatementForInsert(statement, object);
        	
        	log.debug("Get count of inserted result set");
	        int count = statement.executeUpdate();
	        
	        if (count != 1){
	        	String s = "On persist inserted more then 1 record: ";
	        	log.error(s + count);
	        	throw new PersistException(s + count);
	        }	        	
	         
	        ResultSet gk = statement.getGeneratedKeys();
	        if(gk.next()) {
	            // Получаем поле id
	            id = gk.getInt("id");
	        }
	        
	        log.debug("statement closed");
	        statement.close();
	        
	        persistInstance = getByPK(id);	       
	        
        log.debug("connection closed");
        } catch (Exception e) {
        	log.debug("connection closed");
        	log.error("Cannot insert Object ", e);
            throw new PersistException(e);            
        }
        return persistInstance;
    }
   
    public void delete(T object) throws PersistException {
        List<String> sql = getDeleteQuery();
        
        log.info("Delete Object " + object.toString());
        log.debug("Open connection & Create prepared statement");
        try (Connection connection = daoFactory.getConnection()) {
        	PreparedStatement statement = null;        	
        	connection.setAutoCommit(false);
        	 
        	try {
	            for (int i =0; i<sql.size(); i++){
	            	statement = connection.prepareStatement(sql.get(i));
	            	statement.setObject(1, object.getId());
	            	log.debug("add " + i + " query to transaction. Get count of deleted result set");
	                statement.executeUpdate();
	            }
	            
	            log.debug("Execute transaction");
	        	connection.commit();	                
	        } catch (Exception e) {
	        	log.debug("Rollback transaction");
	        	connection.rollback();
	        	log.error("Cannot exec transaction delete Object " + object.toString(), e);
	            throw new PersistException(e);
	        }
	        finally{
	        	connection.setAutoCommit(true);
	        	log.debug("statement closed");
		        statement.close();
	        }
        	
        log.debug("connection closed");
        } catch (Exception e) {   
        	log.debug("connection closed");
        	log.error("Cannot delete Object " + object.toString(), e);
            throw new PersistException(e);
        }
    }    
   
    public void update(T object) throws PersistException {
        String sql = getUpdateQuery();
        
        log.info("Update " + object.getClass().getSimpleName() + " by " + object.toString());
        
        log.debug("Open connection & Create prepared statement");
        try (Connection connection = daoFactory.getConnection();
        	PreparedStatement statement = connection.prepareStatement(sql);) {
	            prepareStatementForUpdate(statement, object); // заполнение аргументов запроса оставим на совесть потомков
	            
	            log.debug("Get count of updates result set");
	            int count = statement.executeUpdate();
	            if (count != 1){
	            	String s = "On update modify more then 1 record: ";
	        		log.error(s);
	                throw new PersistException(s + count);
	            }
	    
	    log.debug("statement & connection closed");
        } catch (Exception e) {
        	log.debug("statement & connection closed");
        	log.error("Cannot update Object " + object.toString(), e);
            throw new PersistException(e);
        }
    }
    
    public List<Integer> getInstanceAllDependentObj(T object, Class<?> K1) throws PersistException {
		List<Integer> list;
	    String sql = getSelectDependentObjQuery(K1, object.getClass());
		
	    log.info("Get all " + K1.getSimpleName() +" ids related to Instance " + object.toString() + " from DB");
	    
	    try (Connection connection = daoFactory.getConnection();
	        	PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setInt(1, object.getId());
	            
	            log.debug("Get result set");
	            ResultSet rs = statement.executeQuery();
	            
	            log.debug("Parse result set");
	            list = parseDependentResultSet(rs, K1);
	            
	        log.debug("statement&connection closed");   
	        } catch (Exception e) {
	        	log.debug("statement&connection closed");
	        	log.error("Cannot find Instance for dependent Object ", e);
	            throw new PersistException(e);
	        }        
		return list;
	}

	public void addInstanceDependentObj (T object, Object dependentObj) throws PersistException{
    	String sql = getInsertDependentObjQuery(dependentObj.getClass(), object.getClass());
    	
    	log.info("Add " + object.getClass().getSimpleName() + " " + dependentObj.toString());
    	
    	if(!IsObjContainsDependentObj(object, dependentObj)){    	
	    	log.debug("Open connection & Create prepared statement");
	        try (Connection connection = daoFactory.getConnection();
	        	PreparedStatement statement = connection.prepareStatement(sql);) {
	        	prepareStatementForInsertDeleteDependentObj(statement, dependentObj, object);
	        		
	        		log.debug("Get count of inserted result set");
	    	        int count = statement.executeUpdate();
	    	        
	    	        if (count != 1){
	    	        	String s = "On persist inserted more then 1 record: ";
	    	        	log.error(s + count);
	    	        	throw new PersistException(s + count);
	    	        }
	        
	        log.debug("statement & connection closed");
		    } catch (Exception e) {
		    	log.debug("statement & connection closed");
		    	log.error("Cannot insert Object ", e);
		        throw new PersistException(e);            
		    }
    	} else 
    		log.info(object.toString() + " already contains " + dependentObj.toString());
	}
	
	public void removeInstanceDependentObj (T object, Object dependentObj) throws PersistException{
    	String sql = getDeleteDependentObjQuery(dependentObj.getClass(), object.getClass());
    	
    	log.info("Remove " + dependentObj.toString() + " from " + object.toString());
    	
    	if(IsObjContainsDependentObj(object, dependentObj)){    	
	    	log.debug("Open connection & Create prepared statement");
	        try (Connection connection = daoFactory.getConnection();
	        	PreparedStatement statement = connection.prepareStatement(sql);) {
	        		prepareStatementForInsertDeleteDependentObj(statement, dependentObj, object);
	        		
	        		log.debug("Get count of deleted result set");
	                int count = statement.executeUpdate();
	               	if (count != 1){
	                	String s = "On delete modify more then 1 record: ";
	            		log.error(s);
	                    throw new PersistException(s + count);
	               	} 	        
	    	        
	        log.debug("statement & connection closed");
		    } catch (Exception e) {
		    	log.debug("statement & connection closed");
		    	log.error("Cannot delete Object ", e);
		        throw new PersistException(e);            
		    }
    	} else 
    		log.info(object.toString() + " does not contain " + dependentObj.toString());
	}

	private boolean IsObjContainsDependentObj(T object, Object dependentObj) throws PersistException {
		if (getInstanceAllDependentObj(object, dependentObj.getClass()).contains(((Identified<PK>) dependentObj).getId()))
			return true;
		return false;
	}

	private void prepareStatementForInsertDeleteDependentObj(
			PreparedStatement statement, Object dependentObj, T object) throws PersistException {
		try {           
			log.debug("Prepare Statement for insert/delete to DB");
			statement.setInt(2, ((Identified<PK>) dependentObj).getId());
            statement.setInt(1, object.getId());
        } catch (Exception e) {
        	log.error("Cannot create Statement for insert/delete ", e);
        	throw new PersistException(e);
        }		
	}

	private List<Integer> parseDependentResultSet(ResultSet rs, Class<?> K1) throws PersistException {
		List<Integer> result = new ArrayList<Integer>();
		log.debug("Parse Result Set from DB to Dependent Object ids List");
		try {
	        while (rs.next()) {
	        	if(log.isEnabled(Level.TRACE))
	        		log.trace("Parse row " + rs.getInt("id") + " to Dependent Object id");            	
	            result.add(rs.getInt(K1.getSimpleName() + "_id"));
	        }
	    } catch (Exception e) {
	    	log.error("Cannot parse Object ", e);
	        throw new PersistException(e);
	    }
	    return result;
	}

	/**
	 * Возвращает sql запрос для добавления записи о id связанного обьекта в таблицу связей ForeignKey.
	 * <p/>
	 * INSERT INTO PROFESSORS_COURSES (professor_id, course_id) VALUES (?, ?);
	 *  
	 */
	public String getInsertDependentObjQuery(Class<?> K1, Class<?> T) {		
		return "INSERT INTO " + K1.getSimpleName() + "s_" 
				+ T.getSimpleName() + "s (" + T.getSimpleName() + "_id, " + K1.getSimpleName() + "_id) VALUES (?, ?);";
	}

	/**
	 * Возвращает sql запрос для добавления записи о id связанного обьекта в таблицу связей ForeignKey.
	 * <p/>
	 * INSERT INTO PROFESSORS_COURSES (professor_id, course_id) VALUES (?, ?);
	 *  
	 */
	public String getDeleteDependentObjQuery(Class<?> K1, Class<?> T) {		
		return "DELETE FROM " + K1.getSimpleName() + "s_"  + T.getSimpleName() + "s WHERE " 
				+ T.getSimpleName() + "_id=? AND " + K1.getSimpleName() + "_id=? ;";
	}
	
	/**
	 * Возвращает sql запрос для получения всех id связанного обьекта из таблицы связей ForeignKey.
	 * <p/>
	 * SELECT dependentObj_id FROM [FK_Table] WHERE Instance_id = ?;
	 *  
	 */    
	public String getSelectDependentObjQuery(Class<?> K1, Class<?> T){
		return "SELECT " + K1.getSimpleName() + "_id FROM " + K1.getSimpleName() + "s_" 
						+ T.getSimpleName() + "s WHERE " + T.getSimpleName() + "_id = ?";
	}
}
