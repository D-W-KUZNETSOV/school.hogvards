package pro.sky.school.hogvards.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.school.hogvards.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {


    List<Faculty> findByColorIgnoreCase(String color);

    List<Faculty> findByNameIgnoreCase(String name);


    boolean existsByName(String name);

}
