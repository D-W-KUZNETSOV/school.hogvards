package pro.sky.school.hogvards.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    public FacultyController(FacultyRepository facultyRepository, FacultyServiceImpl facultyServiceImpl,
                             StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.facultyServiceImpl = facultyServiceImpl;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/{id}")
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
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty addFaculty(@Valid @RequestBody Faculty faculty) {
        if (facultyRepository.existsByName(faculty.getName())) {
            throw new IllegalArgumentException("Faculty with this name already exists");
        }
        return facultyRepository.save(faculty);
    }

    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        if (faculty == null) {
            throw new IllegalArgumentException("Faculty cannot be null");
        }
        faculty.setId(id);
        return facultyServiceImpl.editFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFaculty(@PathVariable Long id) {
        if (!facultyServiceImpl.existsById(id)) {
            throw new IllegalArgumentException("Faculty not found");
        }
        facultyServiceImpl.deleteFaculty(id);
    }

    @GetMapping("/{id}/students")
    public List<Student> getStudentsByFacultyId(@PathVariable Long id) {
        List<Student> students = studentRepository.findByFacultyId(id);
        if (students.isEmpty()) {
            throw new IllegalArgumentException("No students found for this faculty");
        }
        return students;
    }
}


