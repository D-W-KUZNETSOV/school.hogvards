package pro.sky.school.hogvards.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.StudentRepository;
import pro.sky.school.hogvards.service.FacultyServiceImpl;
import pro.sky.school.hogvards.service.StudentServiceImpl;

import java.util.List;
import java.util.Optional;


@RestController
    @RequestMapping("/student")
    public class StudentController {

        private final StudentServiceImpl studentServiceImpl;

        @Autowired
        private FacultyServiceImpl facultyServiceImpl;
        @Autowired
        private StudentRepository studentRepository;

        public StudentController(StudentServiceImpl studentServiceImpl) {
            this.studentServiceImpl = studentServiceImpl;
        }

        @GetMapping("/{id}")
        public ResponseEntity<Student> findStudentById(@PathVariable Long id) {
            Optional<Student> student = studentServiceImpl.findStudent(id);
            return student.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).build());
        }

        @PostMapping
        public ResponseEntity<Student> createStudent(@RequestBody Student student) {
            Student createdStudent = studentServiceImpl.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    existingStudent.setName(updatedStudent.getName());
                    existingStudent.setAge(updatedStudent.getAge());
                    existingStudent.setFaculty(updatedStudent.getFaculty());
                    Student savedStudent = studentRepository.save(existingStudent);
                    return ResponseEntity.ok(savedStudent);
                })
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


        @GetMapping("/age-between")
        public ResponseEntity<List<Student>> getStudentsByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
            List<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);
            return ResponseEntity.ok(students);
        }

        @GetMapping("/{id}/faculty")
        public ResponseEntity<List<Student>> getStudentsOfFaculty(@PathVariable Long id) {
            List<Student> students = studentServiceImpl.getStudentsByFacultyId(id);
            if (students.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(students);
        }
    }

