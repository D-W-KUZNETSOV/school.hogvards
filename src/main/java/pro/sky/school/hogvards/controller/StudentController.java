package pro.sky.school.hogvards.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.school.hogvards.DTO.CreateStudentDto;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.StudentRepository;
import pro.sky.school.hogvards.service.AvatarService;
import pro.sky.school.hogvards.service.FacultyServiceImpl;
import pro.sky.school.hogvards.service.StudentServiceImpl;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;
    private final AvatarService avatarService;

    @Autowired
    private FacultyServiceImpl facultyServiceImpl;
    @Autowired
    private StudentRepository studentRepository;

    public StudentController(StudentServiceImpl studentServiceImpl, AvatarService avatarService) {
        this.studentServiceImpl = studentServiceImpl;
        this.avatarService = avatarService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody CreateStudentDto createStudentDto) {
        Student student = studentServiceImpl.addStudent(createStudentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @GetMapping("/{id}")
    public Student findStudentById(@PathVariable Long id) {
        return studentServiceImpl.findStudent(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id " + id));
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    existingStudent.setName(updatedStudent.getName());
                    existingStudent.setAge(updatedStudent.getAge());
                    existingStudent.setFaculty(updatedStudent.getFaculty());
                    return studentRepository.save(existingStudent);
                })
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id " + id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/age-between")
    public List<Student> getStudentsByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public List<Student> getStudentsOfFaculty(@PathVariable Long id) {
        List<Student> students = studentServiceImpl.getStudentsByFacultyId(id);
        if (students.isEmpty()) {
            throw new EntityNotFoundException("No students found for faculty with id " + id);
        }
        return students;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(
            @PathVariable Long studentId,
            @RequestParam MultipartFile avatar
    ) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }
}


