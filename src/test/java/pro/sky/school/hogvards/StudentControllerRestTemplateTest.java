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
import org.springframework.test.context.ActiveProfiles;
import pro.sky.school.hogvards.DTO.CreateStudentDto;
import pro.sky.school.hogvards.controller.StudentController;
import pro.sky.school.hogvards.model.Faculty;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.AvatarRepository;
import pro.sky.school.hogvards.repositories.FacultyRepository;
import pro.sky.school.hogvards.repositories.StudentRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
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

    @Autowired
    private AvatarRepository avatarRepository;


    @AfterEach
    public void cleanup() {
        avatarRepository.deleteAll();
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
    public void testCreateStudent() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Blue");
        facultyRepository.save(faculty);

        CreateStudentDto createStudentDto = new CreateStudentDto();
        createStudentDto.setName("Tanya Grotter");
        createStudentDto.setAge(15);
        createStudentDto.setFacultyId(faculty.getId());

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "/student",
                createStudentDto,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Student createdStudent = response.getBody();
        assertThat(createdStudent).isNotNull();
        assertThat(createdStudent.getName()).isEqualTo("Tanya Grotter");
        assertThat(createdStudent.getAge()).isEqualTo(15);
        assertThat(createdStudent.getFacultyId()).isEqualTo(faculty.getId());
    }


    @Test
    public void testPutStudent() {

        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("yellow");
        Faculty savedFaculty = facultyRepository.save(faculty);


        CreateStudentDto createStudentDto = new CreateStudentDto();
        createStudentDto.setName("Tanya Grotter");
        createStudentDto.setAge(15);
        createStudentDto.setFacultyId(savedFaculty.getId());

        Student student = new Student();
        student.setName(createStudentDto.getName());
        student.setAge(createStudentDto.getAge());
        student.setFacultyId(createStudentDto.getFacultyId());

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

        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("red");
        Faculty savedFaculty = facultyRepository.save(faculty);


        CreateStudentDto createStudentDto = new CreateStudentDto();
        createStudentDto.setName("Tanya Grotter");
        createStudentDto.setAge(15);
        createStudentDto.setFacultyId(savedFaculty.getId());

        Student student = new Student();
        student.setName(createStudentDto.getName());
        student.setAge(createStudentDto.getAge());
        student.setFacultyId(createStudentDto.getFacultyId());

        Student savedStudent = studentRepository.save(student);
        assertThat(savedStudent.getId()).isGreaterThan(0);


        ResponseEntity<Student> savedStudentResponse = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/students", student, Student.class);
        savedStudent = savedStudentResponse.getBody();


        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();


        this.restTemplate.delete("http://localhost:" + port + "/students/" + savedStudent.getId());


        ResponseEntity<Student> response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + savedStudent.getId(), Student.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void testGetStudentsByAgeBetween() {

        Faculty faculty = new Faculty();
        faculty.setName("Grifindor");
        faculty.setColor("red");
        Faculty savedFaculty = facultyRepository.save(faculty);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Slyserin");
        faculty2.setColor("Green");
        Faculty savedFaculty2 = facultyRepository.save(faculty2);

        Faculty faculty3 = new Faculty();
        faculty3.setName("Huflepuff");
        faculty3.setColor("yellow");
        Faculty savedFaculty3 = facultyRepository.save(faculty3);


        CreateStudentDto createStudentDto = new CreateStudentDto();
        createStudentDto.setName("Tanya Grotter");
        createStudentDto.setAge(15);
        createStudentDto.setFacultyId(savedFaculty.getId());

        Student student = new Student();
        student.setName(createStudentDto.getName());
        student.setAge(createStudentDto.getAge());
        student.setFacultyId(createStudentDto.getFacultyId());

        Student savedStudent = studentRepository.save(student);
        assertThat(savedStudent.getId()).isGreaterThan(0);


        CreateStudentDto createStudentDto2 = new CreateStudentDto();
        createStudentDto2.setName("Garry Potter");
        createStudentDto2.setAge(17);
        createStudentDto2.setFacultyId(savedFaculty.getId());

        Student student2 = new Student();
        student2.setName(createStudentDto2.getName());
        student2.setAge(createStudentDto2.getAge());
        student2.setFacultyId(createStudentDto2.getFacultyId());

        Student savedStudent2 = studentRepository.save(student2);
        assertThat(savedStudent2.getId()).isGreaterThan(0);

        CreateStudentDto createStudentDto3 = new CreateStudentDto();
        createStudentDto3.setName("Ron Wesley ");
        createStudentDto3.setAge(12);
        createStudentDto3.setFacultyId(savedFaculty.getId());

        Student student3 = new Student();
        student3.setName(createStudentDto3.getName());
        student3.setAge(createStudentDto3.getAge());
        student3.setFacultyId(createStudentDto3.getFacultyId());

        Student savedStudent3 = studentRepository.save(student3);
        assertThat(savedStudent3.getId()).isGreaterThan(0);

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
        Assertions.assertThat(students).extracting("name").contains("Tanya Grotter", "Garry Potter");
    }

    @Test
    public void testGetStudentsOfFaculty() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setName("Grifindor");
        faculty.setColor("red");
        Faculty savedFaculty = facultyRepository.save(faculty);


        CreateStudentDto createStudentDto = new CreateStudentDto();
        createStudentDto.setName("Tanya Grotter");
        createStudentDto.setAge(15);
        createStudentDto.setFacultyId(savedFaculty.getId());

        Student student = new Student();
        student.setName(createStudentDto.getName());
        student.setAge(createStudentDto.getAge());
        student.setFacultyId(createStudentDto.getFacultyId());

        Student savedStudent = studentRepository.save(student);
        assertThat(savedStudent.getId()).isGreaterThan(0);


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

