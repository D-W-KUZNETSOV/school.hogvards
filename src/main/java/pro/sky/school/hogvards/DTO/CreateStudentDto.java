package pro.sky.school.hogvards.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CreateStudentDto {

    private String name;
    private int age;
    private String faculty;

    public String getFaculty() {
        return faculty;
    }

    @NotNull(message = "Faculty ID is required")
    @Min(value = 1, message = "Faculty ID must be a positive number")
    private Long facultyId;

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}



