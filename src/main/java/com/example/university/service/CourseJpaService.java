package com.example.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import com.example.university.repository.*;
import com.example.university.model.*;

@Service
public class CourseJpaService implements CourseRepository {
	@Autowired
	private CourseJpaRepository courseJpaRepository;

	@Autowired
	private StudentJpaRepository studentJpaRepository;
	@Autowired
	private ProfessorJpaRepository professorJpaRepository;

	@Override
	public ArrayList<Course> getCourses() {
		List<Course> courseList = courseJpaRepository.findAll();
		ArrayList<Course> courses = new ArrayList<>(courseList);
		return courses;
	}

	@Override
	public Course getCourseById(int courseId) {
		try {
			Course course = courseJpaRepository.findById(courseId).get();
			return course;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Course addCourse(Course course) {
		List<Integer> studentIds = new ArrayList<>();
		for (Student student : course.getStudents()) {
			studentIds.add(student.getStudentId());
		}
		Professor professor = course.getProfessor();
		int professorId = professor.getProfessorId();
		try {
			List<Student> complete_students = studentJpaRepository.findAllById(studentIds);
			if (studentIds.size() != complete_students.size()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
			course.setStudents(complete_students);
			professor = professorJpaRepository.findById(professorId).get();
			course.setProfessor(professor);
			courseJpaRepository.save(course);
			return course;
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Course updateCourse(int courseId, Course course) {
		try {
			Course newCourse = courseJpaRepository.findById(courseId).get();
			if (course.getCourseName() != null) {
				newCourse.setCourseName(course.getCourseName());
			}
			if (course.getCredits() != 0) {
				newCourse.setCredits(course.getCredits());
			}
			if (course.getProfessor() != null) {
				Professor professor = course.getProfessor();
				int professorId = professor.getProfessorId();
				Professor newProfessor = professorJpaRepository.findById(professorId).get();
				newCourse.setProfessor(newProfessor);
			}
			if (course.getStudents() != null) {
				List<Integer> studentIds = new ArrayList<>();
				for (Student student : course.getStudents()) {
					studentIds.add(student.getStudentId());
				}
				List<Student> students = studentJpaRepository.findAllById(studentIds);
				if (studentIds.size() != students.size()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
				newCourse.setStudents(students);

			}
			courseJpaRepository.save(newCourse);
			return newCourse;
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void deleteCourse(int courseId) {
		try {
			courseJpaRepository.deleteById(courseId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		throw new ResponseStatusException(HttpStatus.NO_CONTENT);

	}

	@Override
	public Professor getCourseProfessor(int courseId) {
		try {
			Course course = courseJpaRepository.findById(courseId).get();
			return course.getProfessor();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public List<Student> getCourseStudents(int courseId) {
		try {
			Course course = courseJpaRepository.findById(courseId).get();
			return course.getStudents();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}
