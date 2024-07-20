package com.example.university.repository;

import java.util.*;
import com.example.university.model.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository {

    ArrayList<Professor> getProfessors();

    Professor getProfessorById(int professorId);

    Professor addProfessor(Professor professor);

    Professor updateProfessor(int professorId, Professor professor);

    void deleteProfessor(int professorId);

    List<Course> getProfessorCourses(int professorId);

}