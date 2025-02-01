package pro.sky.school.hogvards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.school.hogvards.model.Faculty;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.FacultyRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Optional<Faculty> findFaculty(Long id) {
        return facultyRepository.findById(id);
    }

    @Override
    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    @Override
    public Collection<Faculty> findByName(String name) {
        return facultyRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Collection<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        Optional<Faculty> existingFaculty = facultyRepository.findById(faculty.getId());
        if (existingFaculty.isPresent()) {
            Faculty updatedFaculty = existingFaculty.get();
            updatedFaculty.setName(faculty.getName());
            updatedFaculty.setColor(faculty.getColor());
            return facultyRepository.save(updatedFaculty);
        }
        return null;
    }


    public boolean existsById(Long id) {
        return facultyRepository.existsById(id);
    }

    @Override
    public void deleteFaculty(long id) {
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
        }
    }

    @Override
    public Collection<Student> getStudentsOfFaculty(Long id) {
        return facultyRepository.findById(id)
                .map(Faculty::getStudents)
                .orElseGet(List::of);
    }
}


