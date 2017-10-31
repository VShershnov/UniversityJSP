package com.foxminded.UniversityJSP.university.schedule;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.schedule.Schedule;
import com.foxminded.UniversityJSP.university.schedule.ScheduleSlot;
import com.foxminded.UniversityJSP.university.Course;
import com.foxminded.UniversityJSP.university.Group;
import com.foxminded.UniversityJSP.university.Room;
import com.foxminded.UniversityJSP.university.person.Professor;
import com.foxminded.UniversityJSP.university.person.Student;

public class Schedule {

	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	private List<ScheduleSlot> scheduleSlots;	
	
	public Schedule() {
		this.scheduleSlots = new ArrayList<ScheduleSlot>();
	}	

	public Schedule(List<ScheduleSlot> scheduleSlots) {
		this.scheduleSlots = scheduleSlots;		
	}
	
	public void addSchedulerSlot (ScheduleSlot scheduleSlot){
		scheduleSlots.add(scheduleSlot);
	}
	
	public void removeSchedulerSlot (ScheduleSlot scheduleSlot){
		scheduleSlots.remove(scheduleSlot);
	}
	
	public List<ScheduleSlot> getScheduleSlots() {
		return scheduleSlots;
	}

	public void setScheduleSlots(List<ScheduleSlot> scheduleSlots) {
		this.scheduleSlots = scheduleSlots;
	}

	public ScheduleSlot getScheduleSlot(ScheduleSlot scheduleSlot) {
		for (ScheduleSlot ss: scheduleSlots){
			if (ss.equals(scheduleSlot))
				return ss;
		}
		return null;
	}

	@Override
	public String toString() {
		return "ScheduleSlots=" + scheduleSlots.size();
	}

	public void removeGroupFromScheduleSlots(Group group) {
		log.info("Remove(set null) group " + group + " from all ScheduleSlots");
		for(ScheduleSlot ss: scheduleSlots){			
			if(ss.getGroup()!=null && ss.getGroup().equals(group))
				ss.setGroup(null);			
		}		
	}

	public void removeCourseFromScheduleSlots(Course course) {
		log.info("Remove(set null) course " + course + " from all ScheduleSlots");
		for(ScheduleSlot ss: scheduleSlots){			
			if(ss.getCourse()!=null && ss.getCourse().equals(course))
				ss.setCourse(null);			
		}		
	}

	public void removeProfessorFromScheduleSlots(Professor prof) {
		log.info("Remove(set null) professor " + prof + " from all ScheduleSlots");
		for(ScheduleSlot ss: scheduleSlots){			
			if(ss.getProfessor()!=null && ss.getProfessor().equals(prof))
				ss.setProfessor(null);			
		}		
	}

	public void removeRoomFromScheduleSlots(Room room) {
		log.info("Remove(set null) room " + room + " from all ScheduleSlots");
		for(ScheduleSlot ss: scheduleSlots){
			if(ss.getRoom()!=null && ss.getRoom().equals(room))
				ss.setRoom(null);				
		}		
	}
	
	public Schedule getDailyScheduleForProfessor(Integer day, Professor prof){
		Schedule schedule = new Schedule();
		log.info("Form Professor " + prof + "daily Schedule, day " + day);
		for(ScheduleSlot ss: scheduleSlots){			
			if(ss.getProfessor()!=null && ss.getProfessor().equals(prof) && ss.getTime().getDay().equals(day))
				schedule.addSchedulerSlot(ss);			
		}		
		return schedule;		
	}
	
	public Schedule getDailyScheduleForStudent(Integer day, Student student){
		Schedule schedule = new Schedule();
		log.info("Form Student " + student + "daily Schedule, day " + day);
		for(ScheduleSlot ss: scheduleSlots){			
			if(ss.getGroup()!=null && student.equals(ss.getGroup().getStudent(student)) && ss.getTime().getDay().equals(day))
						schedule.addSchedulerSlot(ss);			
		}		
		return schedule;	
	}
	
	public Schedule getMonthlyScheduleForProfessor(Integer month, Professor prof){
		Schedule schedule = new Schedule();
		log.info("Form Professor " + prof + "monthly Schedule, month " + month);
		for(ScheduleSlot ss: scheduleSlots){			
			if(ss.getProfessor()!=null && ss.getProfessor().equals(prof) && ss.getTime().getMonth().equals(month))
				schedule.addSchedulerSlot(ss);			
		}		
		return schedule;		
	}
	
	public Schedule getMonthlyScheduleForStudent(Integer month, Student student){
		Schedule schedule = new Schedule();
		log.info("Form Student " + student + "monthly Schedule, month " + month);
		for(ScheduleSlot ss: scheduleSlots){			
			if(ss.getGroup()!=null && student.equals(ss.getGroup().getStudent(student)) && ss.getTime().getMonth().equals(month))
				schedule.addSchedulerSlot(ss);			
		}		
		return schedule;			
	}
}
