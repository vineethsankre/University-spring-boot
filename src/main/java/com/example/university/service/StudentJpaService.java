package com.example.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import com.example.university.repository.*;
import com.example.university.model.*;

@Service
public class StudentJpaService implements StudentRepository {
	@Autowired
	private StudentJpaRepository studentJpaRepository;
	private CourseJpaRepository courseJpaRepository;

	@Override
	public ArrayList<Student> getStudents() {
		List<Student> studentList = studentJpaRepository.findAll();
		ArrayList<Student> students = new ArrayList<>(studentList);
		return students;
	}

	@Override
	public Student getStudentById(int studentId) {
		try {
			Student student = studentJpaRepository.findById(studentId).get();
			return student;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public Student addStudent(Student student) {
		try {
			List<Integer> courseIds = new ArrayList<>();
			for (Course course : student.getCourses()) {
				courseIds.add(course.getCourseId());
			}
			List<Course> courses = courseJpaRepository.findAllById(courseIds);
			if (courseIds.size() != courses.size()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
			for (Course course : courses) {
				course.getStudents().add(student);
			}
			Student savedStudent = studentJpaRepository.save(student);
			courseJpaRepository.saveAll(courses);
			return savedStudent;
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public Student updateStudent(int studentId, Student student) {
		try {
			Student newStudent = studentJpaRepository.findById(studentId).get();
			if (student.getStudentName() != null) {
				newStudent.setStudentName(student.getStudentName());
			}
			if (student.getEmail() != null) {
				newStudent.setEmail(student.getEmail());
			}
			studentJpaRepository.save(newStudent);
			return newStudent;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void deleteStudent(int studentId) {
		try {
			Student student = studentJpaRepository.findById(studentId).get();
			List<Course> courses = student.getCourses();
			for (Course course : courses) {
				course.getStudents().remove(student);
			}
			courseJpaRepository.saveAll(courses);
			studentJpaRepository.deleteById(studentId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		throw new ResponseStatusException(HttpStatus.NO_CONTENT);

	}

	@Override
	public List<Course> getStudentCourses(int studentId) {
		try {
			Student student = studentJpaRepository.findById(studentId).get();
			return student.getCourses();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}
