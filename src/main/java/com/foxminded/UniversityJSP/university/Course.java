package com.foxminded.UniversityJSP.university;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.Course;
import com.foxminded.UniversityJSP.university.Group;
import com.foxminded.UniversityJSP.dao.Identified;
import com.foxminded.UniversityJSP.university.person.Professor;

public class Course implements Identified<Integer>, Serializable{
	
	private static final long serialVersionUID = 6297385302078200511L;
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());

	//Course id and duration in hours
	private Integer id;
	private Integer duration;
	
	private String name;
		
	private Set<Group> groups;
	private Set<Professor> professors;
			
	public Course() {
		this.groups = new HashSet<Group>();
		this.professors = new HashSet<Professor>();
	}

	public Course(Integer id, String name, Integer duration) {
		this.id = id;
		this.name = name;
		this.duration = duration;
		this.groups = new HashSet<Group>();
		this.professors = new HashSet<Professor>();
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Set<Group> getGroups() {
		return groups;
	}
	
	public Group getGroup(Group group) {
		for (Group g: groups){
			if (g.equals(group))
				return g;
		}
		return null;
	}
	
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	public Set<Professor> getProfessors() {
		return professors;
	}
	public void setProfessors(Set<Professor> professors) {
		this.professors = professors;
	}
		
	public void addProfessor(Professor prof) {
		if (prof!=null)
			professors.add(prof);
	}
	
	public void removeProfessor(Professor prof) {
		professors.remove(prof);
	}
	
	public void addGroup(Group group){
		if (group!=null)
			groups.add(group);
	}
	
	public void removeGroup(Group group){
		groups.remove(group);
	}
	
	@Override
	public String toString() {
		return "Course" + id + " [name=" + name + ", duration=" + duration
				+ ", groups=" + groups.size() + ", professors=" + professors.size() + "]";
	}

	public void removeCourseFromCourseGroups(Course course) {
		log.info("Remove course " + course + " from course groups");
		for (Group g: groups){
			g.getCourses().remove(course);
		}		
	}

	public void removeCourseFromCourseProfessors(Course course) {
		log.info("Remove course " + course + " from course professors");
		for (Professor p: professors){
			p.getCourses().remove(course);
		}		
	}

	@Override
	public int hashCode() {
		log.trace("Use hashCode");
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Course other = (Course) obj;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	
}
