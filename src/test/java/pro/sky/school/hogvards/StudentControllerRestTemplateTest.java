package pro.sky.school.hogvards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.school.hogvards.controller.StudentController;
import pro.sky.school.hogvards.model.Faculty;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.FacultyRepository;
import pro.sky.school.hogvards.repositories.StudentRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Nested
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;
    @AfterEach
    public void cleanup() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }


    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testGetStudent() throws Exception {
        Assertions
                .assertThat(
                        this.restTemplate.getForObject("http://localhost:" + port + "/student",
                                String.class))
                .isNotEmpty();
    }


    @Test
    public void testPostStudent() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");
        faculty = facultyRepository.save(faculty);


        Student student = new Student();
        student.setName("Tanya Grotter");
        student.setAge(15);
        student.setFaculty(faculty);

        ResponseEntity<Student> response = this.restTemplate.postForEntity("http://localhost:"
                + port + "/student", student, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Student createdStudent = response.getBody();
        assertThat(createdStudent).isNotNull();
        assertThat(createdStudent.getName()).isEqualTo("Tanya Grotter");
        assertThat(createdStudent.getAge()).isEqualTo(15);
        assertThat(createdStudent.getFaculty().getId()).isEqualTo(faculty.getId());
        assertThat(createdStudent.getFaculty().getName()).isEqualTo("Hufflepuff");
    }


    @Test
    public void testPutStudent() {

        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("yellow");
        Faculty savedFaculty = facultyRepository.save(faculty);


        Student student = new Student();
        student.setName("Tanya Grotter");
        student.setAge(11);
        student.setFaculty(savedFaculty);
        Student savedStudent = studentRepository.save(student);


        assertThat(savedStudent.getId()).isGreaterThan(0);


        savedStudent.setName("Ron Weasley");
        savedStudent.setAge(12);
        this.restTemplate.put("http://localhost:" + port + "/student/" + savedStudent.getId(), savedStudent);


        Student updatedStudent = this.restTemplate.getForObject("http://localhost:" + port + "/student/"
                + savedStudent.getId(), Student.class);


        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getName()).isEqualTo("Ron Weasley");
    }



    @Test
    public void testDeleteStudent() {
        // Создаем факультет
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("red");
        facultyRepository.save(faculty);

        // Создаем студента
        Student student = new Student();
        student.setName("Hanna Abbot");
        student.setAge(16);
        student.setFaculty(faculty);

        // Сохраняем студента
        ResponseEntity<Student> savedStudentResponse = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/students", student, Student.class);
        Student savedStudent = savedStudentResponse.getBody();

        // Проверяем, что студент успешно сохранен
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();

        // Удаляем студента
        this.restTemplate.delete("http://localhost:" + port + "/students/" + savedStudent.getId());

        // Пытаемся получить удаленного студента
        ResponseEntity<Student> response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + savedStudent.getId(), Student.class);

        // Проверяем, что студент не найден
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void testGetStudentsByAgeBetween() {

        Faculty faculty = new Faculty();
        faculty.setName("Grifindor");
        faculty.setColor("red");
        facultyRepository.save(faculty);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Slyserin");
        faculty2.setColor("Green");
        facultyRepository.save(faculty2);

        Faculty faculty3 = new Faculty();
        faculty3.setName("Huflepuff");
        faculty3.setColor("yellow");
        facultyRepository.save(faculty3);


        Student student = new Student();
        student.setName("Garry Potter");
        student.setAge(16);
        student.setFaculty(faculty);
        studentRepository.save(student);

        Student student2 = new Student();
        student2.setName("Drako Malfoy");
        student2.setAge(15);
        student2.setFaculty(faculty2);
        studentRepository.save(student2);

        Student student3 = new Student();
        student3.setName("Polumna Lowegood");
        student3.setAge(18);
        student3.setFaculty(faculty3);
        studentRepository.save(student3);


        int minAge = 15;
        int maxAge = 17;


        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "/student/age-between?minAge=" + minAge + "&maxAge=" + maxAge,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );


        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Student> students = response.getBody();
        assertNotNull(students);


        Assertions.assertThat(students).isNotNull();
        Assertions.assertThat(students).hasSize(2);
        Assertions.assertThat(students).extracting("name").contains("Drako Malfoy", "Garry Potter");
    }

    @Test
    public void testGetStudentsOfFaculty() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");
        faculty = facultyRepository.save(faculty);


        Student student = new Student();
        student.setName("Tanya Grotter");
        student.setAge(15);
        student.setFaculty(faculty);
        studentRepository.save(student);


        ResponseEntity<List<Student>> response = this.restTemplate.exchange(
                "http://localhost:" + port + "/student/" + faculty.getId() + "/faculty"
                ,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

    }

}

