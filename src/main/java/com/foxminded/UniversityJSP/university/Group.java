package com.foxminded.UniversityJSP.university;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.UniversityJSP.university.Course;
import com.foxminded.UniversityJSP.university.Group;
import com.foxminded.UniversityJSP.dao.Identified;
import com.foxminded.UniversityJSP.university.person.Student;


public class Group implements Identified<Integer>{
	
	private final  Logger log = LogManager.getLogger(this.getClass().getPackage().getName());
	
	private Integer id;
	private String name;
	
	private Set<Student> students;
	private Set<Course> courses;
	
	
	public Group() {
		this.students = new HashSet<Student>();
		this.courses = new HashSet<Course>();
	}
	
	public Group(Integer id, String name) {
		this.id = id;
		this.name = name;
		this.students = new HashSet<Student>();
		this.courses = new HashSet<Course>();
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Student> getStudents() {
		return students;
	}
	
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	
	public void addStudent(Student student){
		if (student!=null)
			students.add(student);		
	}
	
	public void addCourse(Course course) {
		if (course!=null)
			courses.add(course);		
	}
	
	public Set<Course> getCourses() {
		return courses;
	}
	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	public Student getStudent(Student student) {
		for (Student s: students){
			if (s.equals(student))
				return s;
		}
		return null;
	}	
	
	@Override
	public String toString() {
		return "Group" + id + " [name=" + name + ", Students=" + students.size()
				+ ", Courses=" + courses.size() + "]";
	}

	public void removeStudent(Student student) {
		log.info("Remove student " + student + " from Group" + toString());
		if (students.contains(student)) {
			students.remove(student);
		}
	}	
	
	public void removeCourse(Course course) {
		log.info("Remove course " + course + " from Group" + toString());
		if(courses.contains(course)){
			for (Group g : course.getGroups()) {
				g.getCourses().remove(course);
			}
			courses.remove(course);
		}
	}

	public Course getCourse(Course course) {
		for (Course c: courses){
			if (c.equals(course))
				return c;
		}
		return null;
	}

	public void removeGroupFromGroupStudents() {
		log.info("Remove group " + toString() + " from group students (set group = null");
		for (Student s: students){
			s.setGroup(null);
		}		
	}

	public void removeGroupFromGroupCourses(Group group) {
		log.info("Remove group " + group + " from group courses");
		for (Course c: courses){
			c.getGroups().remove(group);
		}		
	}

	@Override
	public int hashCode() {
		log.trace("Use hashCode");
		final int prime = 31;
		int result = 1;
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
		Group other = (Group) obj;
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
