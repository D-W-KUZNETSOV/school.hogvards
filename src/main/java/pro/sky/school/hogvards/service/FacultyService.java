package pro.sky.school.hogvards.service;


import pro.sky.school.hogvards.model.Faculty;
import pro.sky.school.hogvards.model.Student;

import java.util.Collection;
import java.util.Optional;

public interface FacultyService {

        Optional<Faculty> findFaculty(Long id);

        Collection<Faculty> findByColor(String color);

        Collection<Faculty> findByName(String name);

        Collection<Faculty> findAll();

        Faculty addFaculty(Faculty faculty);

        Faculty editFaculty(Faculty faculty);

        void deleteFaculty(long id);

        Collection<Student> getStudentsOfFaculty(Long id);

        boolean existsById(Long id);

    }


