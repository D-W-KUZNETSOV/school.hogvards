package pro.sky.school.hogvards.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.school.hogvards.DTO.CreateStudentDto;
import pro.sky.school.hogvards.exception.ResourceNotFoundException;
import pro.sky.school.hogvards.exception.StudentNotFoundException;
import pro.sky.school.hogvards.model.Faculty;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.StudentRepository;
import pro.sky.school.hogvards.service.AvatarService;
import pro.sky.school.hogvards.service.FacultyServiceImpl;
import pro.sky.school.hogvards.service.StudentServiceImpl;

import jakarta.validation.Valid;


import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;
    private final AvatarService avatarService;
    private final FacultyServiceImpl facultyServiceImpl;
    private final StudentRepository studentRepository;

    public StudentController(StudentServiceImpl studentServiceImpl, AvatarService avatarService,
                             FacultyServiceImpl facultyServiceImpl, StudentRepository studentRepository) {
        this.studentServiceImpl = studentServiceImpl;
        this.avatarService = avatarService;
        this.facultyServiceImpl = facultyServiceImpl;
        this.studentRepository = studentRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@Valid @RequestBody CreateStudentDto createStudentDto) {

        Faculty faculty = facultyServiceImpl.findFaculty(createStudentDto.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found"));

        Student student = new Student();
        student.setName(createStudentDto.getName());
        student.setAge(createStudentDto.getAge());
        student.setFaculty(faculty);

        System.out.println("Saving student: " + student);

        return studentServiceImpl.addStudent(createStudentDto);
    }


    @GetMapping("/{id}")
    public Student findStudentById(@PathVariable Long id) {
        return studentServiceImpl.findStudent(id)
                .orElseThrow(() -> new StudentNotFoundException(id)); // Используем собственное исключение
    }

    @PutMapping("/{id}")
    public Student editStudent(@PathVariable Long id, @RequestBody @Valid CreateStudentDto createStudentDto) {
        return studentServiceImpl.editStudent(id, createStudentDto);
    }

    @GetMapping("name/{name}")
    List<Student> findByName(@PathVariable String name) {
        return (List<Student>) studentRepository.findByname(name);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }

    @GetMapping("/age-between")
    public List<Student> getStudentsByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public List<Student> getStudentsOfFaculty(@PathVariable Long id) {
        List<Student> students = studentServiceImpl.getStudentsByFacultyId(id);
        if (students.isEmpty()) {
            throw new StudentNotFoundException(id);
        }
        return students;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/students/count")
    public Long getCountOfStudents() {
        return studentRepository.countAllStudents();
    }

    @GetMapping("/students/average-age")
    @ResponseStatus(HttpStatus.OK)
    public Integer getAverageAge() {
        return studentRepository.findAverageAge();
    }

    @GetMapping("/students/latest")
    public List<Student> getLatestStudents(@RequestParam(defaultValue = "5") Integer count) {
        return studentRepository.findTop5ByOrderByIdDesc((Pageable) PageRequest.of(0, count));
    }

}


