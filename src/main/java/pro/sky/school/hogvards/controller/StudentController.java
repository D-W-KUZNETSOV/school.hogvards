package pro.sky.school.hogvards.controller;

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

        @PutMapping("{id}")
        public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
            student.setId(id);
            return studentServiceImpl.putStudent(student);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
            if (studentServiceImpl.existsById(id)) {
                studentServiceImpl.deleteStudent(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }


        @GetMapping("/age-between")
        public ResponseEntity<List<Student>> getStudentsByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
            List<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);
            return ResponseEntity.ok(students);
        }

        @GetMapping("/{id}/students")
        public ResponseEntity<List<Student>> getStudentsByFacultyId(@PathVariable Long id) {
            List<Student> students = studentServiceImpl.getStudentsByFacultyId(id);
            if (students.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(students);
        }
    }

