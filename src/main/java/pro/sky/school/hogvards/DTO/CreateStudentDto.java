package pro.sky.school.hogvards.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateStudentDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be a positive number")
    private int age;

    @NotNull(message = "Faculty ID is required")
    private Long facultyId;

    public CreateStudentDto(String name, int age, Long facultyId) {
        this.name = name;
        this.age = age;
        this.facultyId = facultyId;
    }

    public CreateStudentDto() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }
}




