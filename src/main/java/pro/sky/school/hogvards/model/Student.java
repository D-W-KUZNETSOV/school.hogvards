package pro.sky.school.hogvards.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.util.Objects;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private int age;

    @NotNull(message = "Faculty ID is required") // Поле обязательно
    @Min(value = 1, message = "Faculty ID must be a positive number") // ID должен быть положительным
    @Column(name = "faculty_id", nullable = false) // Столбец в базе данных не может быть null
    private Long facultyId;

    @ManyToOne
    @JoinColumn(name = "faculty_id", insertable = false, updatable = false)

    private Faculty faculty;




    public Student(long id, String name, int age, Long facultyId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.facultyId = facultyId;
    }


    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", facultyId=" + facultyId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && age == student.age && facultyId == student.facultyId
                && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, facultyId);
    }

    // Геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public @NotNull(message = "Faculty ID is required") @Min(value = 1, message = "Faculty ID must be a positive number") Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(@NotNull(message = "Faculty ID is required") @Min(value = 1, message = "Faculty ID must be a positive number") Long facultyId) {
        this.facultyId = facultyId;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }
}


