package pro.sky.school.hogvards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.school.hogvards.controller.StudentController;
import pro.sky.school.hogvards.model.Faculty;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.FacultyRepository;
import pro.sky.school.hogvards.repositories.StudentRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pro.sky.school.hogvards.service.AvatarServiceImpl;
import pro.sky.school.hogvards.service.FacultyServiceImpl;
import pro.sky.school.hogvards.service.StudentServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class StudentControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarServiceImpl avatarService;

    @SpyBean
    private StudentServiceImpl studentServiceImpl;

    @SpyBean
    private FacultyServiceImpl facultyServiceImpl;

    @InjectMocks
    private StudentController studentController;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testPostStudent() throws Exception {

        Faculty faculty = createFaculty("Gryffindor", "Red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);


        JSONObject studentObject = new JSONObject();
        studentObject.put("name", "Garry Potter");
        studentObject.put("age", 15);
        studentObject.put("facultyId", 1);


        Student student = createStudent(1L, "Garry Potter", 15, faculty);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Garry Potter"))
                .andExpect(jsonPath("$.age").value(15))
                .andExpect(jsonPath("$.faculty.name").value("Gryffindor"));

        verify(studentRepository).save(any(Student.class));
    }

    private Faculty createFaculty(String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        return faculty;
    }

    private Student createStudent(Long id, String name, int age, Faculty faculty) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);
        return student;
    }

    @Test
    public void testGetStudent() throws Exception {

        Faculty faculty = createFaculty("Grifindor", "Red");


        Student student = createStudent(1L, "Garry Potter", 15, faculty);


        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garry Potter"))
                .andExpect(jsonPath("$.age").value(15))
                .andExpect(jsonPath("$.faculty.name").value("Grifindor"));


        verify(studentRepository).findById(1L);
    }


    @Test
    public void testPutStudent() throws Exception {

        Faculty faculty = createFaculty("Grifindor", "Red");


        Student updatedStudent = createStudent(1L, "Harry Potter", 14, faculty);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(updatedStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/1")
                        .content(objectMapper.writeValueAsString(updatedStudent))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(14))
                .andExpect(jsonPath("$.faculty.name").value("Grifindor"));

        verify(studentRepository).findById(1L);
        verify(studentRepository).save(any(Student.class));
    }


    @Test
    public void testDeleteStudent() throws Exception {

        Student student = createStudent(1L, "Garry Potter", 15, null);


        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));


        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


        verify(studentRepository, times(1)).deleteById(1L);
    }


    @Test
    public void testFindStudentsByAgeBetween() throws Exception {

        Faculty faculty1 = createFaculty("Grifindor", "red");
        Faculty faculty2 = createFaculty("Slyserin", "green");
        Faculty faculty3 = createFaculty("Huflepuff", "yellow");


        Student student1 = createStudent("Garry Potter", 16, faculty1);
        Student student2 = createStudent("Drako Malfoy", 15, faculty2);
        Student student3 = createStudent("Polumna Lowegood", 18, faculty3);


        when(studentRepository.findByAgeBetween(14, 16))
                .thenReturn(Arrays.asList(student1, student2));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age-between?minAge=14&maxAge=16")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[1].name").value(student2.getName()));
    }

    private Student createStudent(String name, int age, Faculty faculty) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);
        studentRepository.save(student);
        return student;
    }


    @Test
    public void testGetStudentsOfFaculty() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");
        faculty = facultyRepository.save(faculty);


        Student student = new Student();
        student.setId(1L);
        student.setName("Garry Potter");
        student.setAge(15);
        student.setFaculty(faculty);
        studentRepository.save(student);


        when(studentRepository.findByFacultyId(1L)).thenReturn(Arrays.asList(student));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1/faculty")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Garry Potter"));
    }


}



