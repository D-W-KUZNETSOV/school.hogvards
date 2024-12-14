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

    @SpyBean
    private StudentServiceImpl studentServiceImpl;

    @SpyBean
    private FacultyServiceImpl facultyServiceImpl;

    @InjectMocks
    private StudentController studentController;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testPostStudent() throws Exception {


        Faculty faculty = new Faculty();
        faculty.setName("Grifindor");
        faculty.setColor("Red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        JSONObject studentObjekt = new JSONObject();
        studentObjekt.put("name", "Garry Potter");
        studentObjekt.put("age", 15);
        studentObjekt.put("facultyId", 1);


        Student student = new Student();
        student.setId(1L);
        student.setAge(15);
        student.setName("Garry Potter");
        student.setFaculty(faculty);

        when(studentRepository.save(any(Student.class))).thenReturn(student);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Garry Potter"))
                .andExpect(jsonPath("$.age").value(15))
                .andExpect(jsonPath("$.faculty.name").value("Grifindor"));

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    public void testGetStudent() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Grifindor");
        faculty.setColor("Red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        JSONObject studentObjekt = new JSONObject();
        studentObjekt.put("name", "Garry Potter");
        studentObjekt.put("age", 15);
        studentObjekt.put("facultyId", 1L);


        Student student = new Student();
        student.setId(1L);
        student.setAge(15);
        student.setName("Garry Potter");
        student.setFaculty(faculty);

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
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Grifindor");
        faculty.setColor("Red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        JSONObject studentObjekt = new JSONObject();
        studentObjekt.put("id", 1L);
        studentObjekt.put("name", "Garry Potter");
        studentObjekt.put("age", 15);
        studentObjekt.put("facultyId", 1L);

        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setAge(14);
        updatedStudent.setName("Harry Potter");
        updatedStudent.setFaculty(faculty);

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

        Student student = new Student();
        student.setId(1L);
        student.setName("Garry Potter");
        student.setAge(15);
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindStudentsByAgeBetween() throws Exception {

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


        Student student1 = new Student();
        student1.setName("Garry Potter");
        student1.setAge(16);
        student1.setFaculty(faculty);
        studentRepository.save(student1);

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

        when(studentRepository.findByAgeBetween(14, 16))
                .thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age-between?minAge=14&maxAge=16")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Garry Potter"))
                .andExpect(jsonPath("$[1].name").value("Drako Malfoy"));
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



