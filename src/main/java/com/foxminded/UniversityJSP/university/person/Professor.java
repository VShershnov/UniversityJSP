package com.foxminded.UniversityJSP.university.person;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.person.Professor;
import com.foxminded.UniversityJSP.university.Course;

public class Professor extends UniversityPerson{
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());

	//Set of professor teaching courses
	private Set<Course> courses;
	
	public Professor() {
		this.courses = new HashSet<Course>();
	}
	
	public Professor(Integer id, String name) {
		super.setId(id);
		super.setFullName(name);
		this.courses = new HashSet<Course>();
	}
	
	public void addCourse(Course course){
		if (course!=null)
			courses.add(course);
	}
	
	public void removeCourse(Course course){
		log.info("Remove course " + course + " from professor " + toString());
		courses.remove(course);
	}
	
	public Set<Course> getCourses() {
		return courses;
	}

	public Course getCourse(Course course) {
		log.info("Get course " + course + " from professor " + toString());
		for (Course c: courses){
			if (c.equals(course))
				return c;
		}
		return null;
	}

	public void removeProfessorFromProfessorCourses(Professor prof) {
		log.info("Remove professor " + prof + " from professor courses");
		for (Course c: courses){
			c.getProfessors().remove(prof);
		}		
	}

	public String toString() {
		return "Professor" + getId() + " [FullName="
				+ getFullName() + ", Courses=" + courses.size() + "]";
	}	
}
