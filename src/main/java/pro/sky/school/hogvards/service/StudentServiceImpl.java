package pro.sky.school.hogvards.service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.school.hogvards.DTO.CreateStudentDto;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);


    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    public Student addStudent(CreateStudentDto createStudentDto) {
        logger.info("Was invoked method for  addStudent");
        if (createStudentDto.getName() == null || createStudentDto.getName().isEmpty()) {
            logger.error("Attempted to create a null student");
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }
        if (createStudentDto.getAge() <= 0) {
            logger.error("Attempted to create a null age");
            throw new IllegalArgumentException("Student age must be a positive number");
        }

        Student student = new Student();
        student.setName(createStudentDto.getName());
        student.setAge(createStudentDto.getAge());
        student.setFacultyId(createStudentDto.getFacultyId());

        logger.debug("Creating student with name: {}", student.getName());
        return studentRepository.save(student);
    }


    @Override
    public Optional<Student> findStudent(long id) {
        logger.info("Was invoked method for find student by id: {}", id);
        Optional<Student> student = studentRepository.findById(id);
        if (!student.isPresent()) {
            logger.warn("There is not student with id = {}", id);
        }
        return student;
    }

    @Override
    public Student editStudent(Long studentId, CreateStudentDto createStudentDto) throws EntityNotFoundException {
        logger.info("Invoked method to update student with id: {}", studentId);

        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + studentId + " does not exist."));

        if (createStudentDto.getName() == null || createStudentDto.getName().isEmpty()) {
            logger.error("Attempted to update student with a null or empty name");
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }
        if (createStudentDto.getAge() <= 0) {
            logger.error("Attempted to update student with a non-positive age");
            throw new IllegalArgumentException("Student age must be a positive number");
        }

        existingStudent.setName(createStudentDto.getName());
        existingStudent.setAge(createStudentDto.getAge());
        existingStudent.setFacultyId(createStudentDto.getFacultyId());

        logger.debug("Updating student with id: {} and name: {}", studentId, existingStudent.getName());

        return studentRepository.save(existingStudent);
    }


    @Override
    public boolean existsById(Long id) {
        logger.info("Was invoked method for search student with id: {}", id);
        return studentRepository.existsById(id);
    }

    @Override
    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student with id: {}", id);

        if (!studentRepository.existsById(id)) {
            logger.error("Attempted to delete a student with id = {} that does not exist", id);
            throw new NoSuchElementException("Student not found");
        }

        studentRepository.deleteById(id);
        logger.debug("Student with id: {} has been successfully deleted", id);
    }

    @Override
    public Collection<Student> findAll() {
        logger.info("Was invoked method to find all students");

        Collection<Student> students = studentRepository.findAll();

        logger.debug("Number of students found: {}", students.size());

        return students;
    }

    @Override
    public List<Student> getStudentsByAgeBetween(int min, int max) {
        logger.info("Was invoked method to get students between ages {} and {}", min, max);

        List<Student> students = studentRepository.findByAgeBetween(min, max);

        logger.debug("Number of students found between ages {} and {}: {}", min, max, students.size());

        return students;
    }

    @Override
    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Invoked method to find students between ages {} and {}", minAge, maxAge);

        List<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);

        logger.debug("Number of students found between ages {} and {}: {}", minAge, maxAge, students.size());

        return students;
    }

    public List<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Invoked method to get students by faculty ID: {}", facultyId);

        List<Student> students = studentRepository.findByFacultyId(facultyId);

        logger.debug("Number of students found for faculty ID {}: {}", facultyId, students.size());

        return students;
    }


    @Override
    public List<Student> findByName(String name) {
        logger.info("Invoked method to find students by name: {}", name);

        List<Student> students = studentRepository.findByName(name);

        logger.debug("Number of students found with name '{}': {}", name, students.size());

        return students;
    }

    @Override
    public List<String> getAllStudentsNamesStartingWithA() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .filter(it -> it.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .toList();


    }

    @Override
    public double getMiddleAge() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

}

