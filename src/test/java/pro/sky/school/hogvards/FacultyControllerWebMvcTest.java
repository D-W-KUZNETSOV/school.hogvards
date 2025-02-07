package pro.sky.school.hogvards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.school.hogvards.model.Faculty;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.AvatarRepository;
import pro.sky.school.hogvards.repositories.FacultyRepository;
import pro.sky.school.hogvards.repositories.StudentRepository;
import pro.sky.school.hogvards.service.AvatarService;
import pro.sky.school.hogvards.service.FacultyServiceImpl;
import pro.sky.school.hogvards.service.StudentServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarService avatarService;

    @MockBean
    AvatarRepository avatarRepository;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @SpyBean
    private StudentServiceImpl studentService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testPostFaculty() throws Exception {

        JSONObject facultyObjekt = new JSONObject();
        facultyObjekt.put("name", "Grifindor");
        facultyObjekt.put("color", "red");

        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Grifindor");
        faculty.setColor("red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Grifindor"))
                .andExpect(jsonPath("$.color").value("red"));

        verify(facultyRepository).save(any(Faculty.class));

    }

    @Test
    public void testGetFaculty() throws Exception {
        JSONObject facultyObjekt = new JSONObject();
        facultyObjekt.put("name", "Grifindor");
        facultyObjekt.put("color", "red");

        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Grifindor");
        faculty.setColor("red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Grifindor"))
                .andExpect(jsonPath("$.color").value("red"));


        verify(facultyRepository).findById(1L);
    }

    @Test
    public void testPutFaculty() throws Exception {

        JSONObject facultyObjekt = new JSONObject();
        facultyObjekt.put("name", "Gryfindor");
        facultyObjekt.put("color", "red");

        String jsonContent = "{\"id\":1,\"name\":\"Gryfindor\",\"color\":\"red\"}";

        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryfindor");
        faculty.setColor("red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));


        Faculty updateFaculty = new Faculty(1L, "Gryfindor", "red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(updateFaculty));


        Faculty updatedFaculty = new Faculty(1L, "Gryfindor", "red");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryfindor"))
                .andExpect(jsonPath("$.color").value("red"));

        verify(facultyRepository).save(faculty);
        ;
    }

    @Test
    public void testDeleteFaculty() throws Exception {


        JSONObject facultyObjekt = new JSONObject();
        facultyObjekt.put("name", "Grifindor");
        facultyObjekt.put("color", "red");

        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Grifindor");
        faculty.setColor("red");


        when(facultyRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    public void testStudentsByFacultyId() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Huflepuf");
        faculty.setColor("yellow");
        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));


        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Hanna Abbot");
        student1.setAge(15);
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Drako Malfoy");
        student2.setAge(16);
        student2.setFaculty(faculty);


        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findByFacultyId(faculty.getId())).thenReturn(students);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Hanna Abbot"))
                .andExpect(jsonPath("$[1].name").value("Drako Malfoy"));
    }
}









