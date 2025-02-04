package pro.sky.school.hogvards.service;


import jakarta.persistence.EntityNotFoundException;
import pro.sky.school.hogvards.DTO.CreateStudentDto;
import pro.sky.school.hogvards.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentService {


        Student addStudent(CreateStudentDto createStudentDto);

        Optional<Student> findStudent(long id);

        Student editStudent(Long studentId, CreateStudentDto createStudentDto) throws EntityNotFoundException;

        boolean existsById(Long id);

        void deleteStudent(Long id);

        Collection<Student> findAll();

        List<Student> getStudentsByAgeBetween(int min, int max);

        List<Student> findByAgeBetween(int minAge, int maxAge);

        List<Student> getStudentsByFacultyId(Long id);
    }

