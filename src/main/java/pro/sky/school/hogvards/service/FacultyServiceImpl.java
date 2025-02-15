package pro.sky.school.hogvards.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    private final FacultyRepository facultyRepository;


    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Optional<Faculty> findFaculty(Long id) {
        logger.info("Invoked method to find faculty by id: {}", id);

        Optional<Faculty> faculty = facultyRepository.findById(id);

        if (!faculty.isPresent()) {
            logger.warn("No faculty found with id: {}", id);
        } else {
            logger.debug("Faculty found: {}", faculty.get());
        }

        return faculty;
    }

    @Override
    public Collection<Faculty> findByColor(String color) {
        logger.info("Invoked method to find faculties by color: {}", color);

        Collection<Faculty> faculties = facultyRepository.findByColorIgnoreCase(color);

        logger.debug("Number of faculties found with color '{}': {}", color, faculties.size());

        return faculties;
    }


    @Override
    public Collection<Faculty> findByName(String name) {
        logger.info("Invoked method to find faculties by name: {}", name);

        Collection<Faculty> faculties = facultyRepository.findByNameIgnoreCase(name);

        logger.debug("Number of faculties found with name '{}': {}", name, faculties.size());

        return faculties;
    }


    @Override
    public Collection<Faculty> findAll() {
        logger.info("Invoked method to find all faculties.");

        Collection<Faculty> faculties = facultyRepository.findAll();

        logger.debug("Number of faculties found: {}", faculties.size());

        return faculties;
    }


    @Override
    public Faculty addFaculty(Faculty faculty) {
        logger.info("Invoked method to add a faculty: {}", faculty.getName());

        Faculty savedFaculty = facultyRepository.save(faculty);

        logger.debug("Faculty added with ID: {}", savedFaculty.getId());

        return savedFaculty;
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Invoked method to edit faculty with ID: {}", faculty.getId());

        Optional<Faculty> existingFaculty = facultyRepository.findById(faculty.getId());
        if (existingFaculty.isPresent()) {
            Faculty updatedFaculty = existingFaculty.get();
            updatedFaculty.setName(faculty.getName());
            updatedFaculty.setColor(faculty.getColor());

            Faculty savedFaculty = facultyRepository.save(updatedFaculty);
            logger.debug("Faculty updated successfully: {}", savedFaculty);

            return savedFaculty;
        } else {
            logger.warn("Faculty with ID: {} not found.", faculty.getId());
        }

        return null;
    }


    public boolean existsById(Long id) {
        logger.info("Checking existence of faculty with ID: {}", id);

        boolean exists = facultyRepository.existsById(id);

        if (exists) {
            logger.debug("Faculty with ID: {} exists.", id);
        } else {
            logger.warn("Faculty with ID: {} does not exist.", id);
        }

        return exists;
    }


    @Override
    public void deleteFaculty(long id) {
        logger.info("Attempting to delete faculty with ID: {}", id);

        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
            logger.info("Faculty with ID: {} has been successfully deleted.", id);
        } else {
            logger.warn("Faculty with ID: {} not found. Deletion not performed.", id);
        }
    }


    @Override
    public Collection<Student> getStudentsOfFaculty(Long id) {
        logger.info("Attempting to retrieve students for faculty with ID: {}", id);

        return facultyRepository.findById(id)
                .map(faculty -> {
                    logger.info("Faculty with ID: {} found. Retrieving students.", id);
                    return faculty.getStudents();
                })
                .orElseGet(() -> {
                    logger.warn("Faculty with ID: {} not found. Returning empty list.", id);
                    return List.of();
                });
    }
}



