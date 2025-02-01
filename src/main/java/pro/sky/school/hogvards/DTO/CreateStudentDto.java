package pro.sky.school.hogvards.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CreateStudentDto {

    private String name;
    private int age;



    @NotNull(message = "Faculty ID is required")
    @Min(value = 1, message = "Faculty ID must be a positive number")
    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(@NotNull(message = "Faculty ID is required") @Min(value = 1, message = "Faculty ID must be a positive number") int facultyId) {
        this.facultyId = facultyId;
    }

    @NotNull(message = "Faculty ID is required")
    @Min(value = 1, message = "Faculty ID must be a positive number")
    private int facultyId;


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


