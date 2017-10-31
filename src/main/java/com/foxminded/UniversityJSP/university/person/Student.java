package com.foxminded.UniversityJSP.university.person;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.person.Student;
import com.foxminded.UniversityJSP.university.Group;

public class Student extends UniversityPerson {
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	private Group group;
	
	public Student() {
	}
	
	public Student(int id, String name) {
		super.setId(id);
		super.setFullName(name);
	}
	
	public Student(String name) {
		super.setFullName(name);
	}
	
	public Student(String name, Group group) {
		this(name);
		this.group = group;
	}
	
	public Student(Integer id, String name, Group group) {
		this(id, name);
		this.group = group;
	}

	public Group getGroup() {
		return group;		
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void removeStudentFromStudentGroup(Student student) {
		log.info("Remove student " + toString() + " from student groups");
		group.getStudents().remove(student);
	}

	@Override
	public String toString() {
		return "Student" + getId() + " [FullName=" + getFullName()
				+ ", group=" + group + "]";
	}
}
