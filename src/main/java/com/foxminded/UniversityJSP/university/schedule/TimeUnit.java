package com.foxminded.UniversityJSP.university.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.dao.Identified;

public class TimeUnit implements Identified<Integer>{
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	private Integer id;
	private Integer day;
	private Integer time;
	private Integer month;
			
	public TimeUnit() {
	}

	public TimeUnit(Integer time, Integer day, Integer month) {
		this.day = day;
		this.time = time;
		this.month = month;
	}
	
	public TimeUnit(Integer id, Integer time, Integer day, Integer month) {
		this(time, day, month);
		this.id= id;
	}
	
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}	
	
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	@Override
	public String toString() {
		return "TimeUnit[" + time + day + month + "]";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	@Override
	public int hashCode() {
		log.trace("Use hashCode");
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		log.trace("Use equals");		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeUnit other = (TimeUnit) obj;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}	
}
