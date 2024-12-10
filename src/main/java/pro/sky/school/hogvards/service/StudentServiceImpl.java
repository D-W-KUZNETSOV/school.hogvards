package pro.sky.school.hogvards.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {


    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addStudent(Student student) {
        if (student.getFaculty() == null) {
            throw new IllegalArgumentException("Faculty cannot be null");
        }
        if (student.equals(student.getName())) {
            throw new IllegalArgumentException("A student with that name already exists");
        }

        return studentRepository.save(student);
    }


    @Override
    public Optional<Student> findStudent(long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student putStudent(Student student) throws EntityNotFoundException {
        if (!studentRepository.existsById(student.getId())) {
            throw new EntityNotFoundException("Student with id " + student.getId() + " does not exist.");
        }
        return studentRepository.save(student);
    }

    @Override
    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Collection<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getStudentsByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> getStudentsByFacultyId(Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }
}

