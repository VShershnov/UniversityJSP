package com.foxminded.UniversityJSP.university.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.dao.Identified;
import com.foxminded.UniversityJSP.university.Course;
import com.foxminded.UniversityJSP.university.Room;
import com.foxminded.UniversityJSP.university.person.Professor;
import com.foxminded.UniversityJSP.university.Group;

public class ScheduleSlot implements Identified<Integer> {
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	private Integer id;
	
	private TimeUnit time;

	private Room room;
	
	private Course course;
	
	private Professor professor;
	
	private Group group;
	
	
	public ScheduleSlot() {
	}

	public ScheduleSlot(Integer id, TimeUnit time, Room room) {
		this.id = id;
		this.room = room;
		this.time = time;
	}
	
	public ScheduleSlot(Integer id, TimeUnit time, Room room, Course course, Professor professor, Group group) {
		this(id, time, room);
		this.course = course;
		this.professor = professor;
		this.group = group;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public TimeUnit getTime() {
		return time;
	}

	public void setTime(TimeUnit time) {
		this.time = time;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}	

	@Override
	public String toString() {
		return "ScheduleSlot" + id + " [time=" + time + ", room" + room.getId() + ", course="
				+ course.getName() + ", professor=" + professor.getFullName()
				+ ", group=" + group.getName() + "]\n";
	}

	@Override
	public int hashCode() {
		log.trace("Use hashCode");
		final int prime = 31;
		int result = 1;
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((professor == null) ? 0 : professor.hashCode());
		result = prime * result + ((room == null) ? 0 : room.hashCode());
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
		ScheduleSlot other = (ScheduleSlot) obj;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (professor == null) {
			if (other.professor != null)
				return false;
		} else if (!professor.equals(other.professor))
			return false;
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}	
}
