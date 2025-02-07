package pro.sky.school.hogvards.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import pro.sky.school.hogvards.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAgeBetween(int min, int max);

    List<Student> findByFacultyId(Long facultyId);


    @Query(value = "SELECT COUNT(s) FROM Student s")
    long countAllStudents();

    @Query(value = "SELECT AVG(s.age) FROM Student s")
    Integer findAverageAge();

    @Query(value = "SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findTop5ByOrderByIdDesc(Pageable pageable);
}


