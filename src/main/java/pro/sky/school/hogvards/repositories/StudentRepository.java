package pro.sky.school.hogvards.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.school.hogvards.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAgeBetween(int min, int max);


    List<Student> findByFacultyId(Long facultyId);
}