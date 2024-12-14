package pro.sky.school.hogvards.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.school.hogvards.model.Faculty;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.FacultyRepository;
import pro.sky.school.hogvards.repositories.StudentRepository;
import pro.sky.school.hogvards.service.FacultyServiceImpl;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyRepository facultyRepository;
    private final FacultyServiceImpl facultyServiceImpl;


    private final StudentRepository studentRepository;

    public FacultyController(FacultyRepository facultyRepository, FacultyServiceImpl facultyServiceImpl
            , StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.facultyServiceImpl = facultyServiceImpl;
        this.studentRepository = studentRepository;
    }


    @GetMapping("{id}")
    public Faculty getFacultyInfo(@PathVariable Long id) {
        return facultyServiceImpl.findFaculty(id).orElse(null);
    }

    @GetMapping
    public Collection<Faculty> findFaculties(@RequestParam(required = false) String color,
                                             @RequestParam(required = false) String name) {
        if (color != null && !color.isBlank()) {
            return facultyServiceImpl.findByColor(color);
        }
        if (name != null && !name.isBlank()) {
            return facultyServiceImpl.findByName(name);
        }
        return facultyServiceImpl.findAll();
    }

    @PostMapping
    public ResponseEntity<Faculty> addFaculty(@Valid @RequestBody Faculty faculty) {
        if (facultyRepository.existsByName(faculty.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        Faculty savedFaculty = facultyRepository.save(faculty);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFaculty);
    }

    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable Long id,
                                 @RequestBody Faculty faculty) {
        faculty.setId(id);
        return facultyServiceImpl.editFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        if (!facultyServiceImpl.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        facultyServiceImpl.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudentsByFacultyId(@PathVariable Long id) {
        List<Student> students = studentRepository.findByFacultyId(id);

        if (students.isEmpty()) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если студентов нет
        }

        return ResponseEntity.ok(students); // Возвращаем 200 с найденными студентами
    }

}

