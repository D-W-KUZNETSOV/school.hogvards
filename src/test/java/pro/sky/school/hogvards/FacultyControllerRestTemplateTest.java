package pro.sky.school.hogvards;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.school.hogvards.controller.FacultyController;
import pro.sky.school.hogvards.model.Faculty;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.AvatarRepository;
import pro.sky.school.hogvards.repositories.FacultyRepository;
import pro.sky.school.hogvards.repositories.StudentRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FacultyControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void testGetFaculty() throws Exception {
        Assertions.assertThat(
                        this.restTemplate.getForObject("http://localhost:" + port + "/faculty",
                                String.class))
                .isNotEmpty();

    }

    @Test
    void testPostFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Umpalumpa");
        faculty.setColor("pink");
        Assertions.assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty",
                        faculty,
                        Faculty.class))
                .isNotNull();
    }

    @Test
    void testPutFaculty() throws Exception {

        Faculty savedFaculty = new Faculty();
        savedFaculty.setName("Grifindor");
        savedFaculty.setColor("red");
        savedFaculty = facultyRepository.save(savedFaculty);


        Faculty existingFaculty = this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + savedFaculty.getId()
                , Faculty.class);

        assertThat(existingFaculty).isNotNull();


        existingFaculty.setName("Huflepuf");
        existingFaculty.setColor("yellow");
        this.restTemplate.put("http://localhost:" + port + "/faculty/" + savedFaculty.getId(), existingFaculty);


        Faculty updatedFaculty = this.restTemplate.getForObject("http://localhost:" + port + "/faculty/" + savedFaculty.getId()
                , Faculty.class);

        assertThat(updatedFaculty).isNotNull();
        assertThat(updatedFaculty.getName()).isEqualTo("Huflepuf");
        assertThat(updatedFaculty.getColor()).isEqualTo("yellow");
    }


    @Test
    public void testDeleteFaculty_NotFound() {
        Long nonExistentId = 999L;

        ResponseEntity<Void> response = restTemplate.exchange(
                "/faculty/faculty/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                nonExistentId
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testDeleteFaculty() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setName("Moushill");
        faculty.setColor("purple");
        faculty = facultyRepository.save(faculty);

        assertThat(faculty).isNotNull();
        assertThat(faculty.getId()).isNotNull();

        this.restTemplate.delete("http://localhost:" + port + "/h2-console/faculty/faculty/{id}", faculty.getId());

        ResponseEntity<Faculty> response = this.restTemplate.getForEntity("http://localhost:" + port + "/h2-console/faculty/faculty/{id}", Faculty.class, faculty.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetStudentsByFacultyId() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setName("Huflepuf");
        faculty.setColor("yellow");
        faculty = facultyRepository.save(faculty);


        Student student1 = new Student();
        student1.setName("Hanna Abbot");
        student1.setAge(15);
        student1.setFacultyId(faculty.getId());
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Drako Malfoy");
        student2.setAge(16);
        student2.setFacultyId(faculty.getId());
        studentRepository.save(student2);


        ResponseEntity<List<Student>> response = this.restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + faculty.getId() + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }


}

