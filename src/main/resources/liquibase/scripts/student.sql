-- liquibase formatted sql
CREATE TABLE student (
    id INT,
    name   TEXT,
    age  INT,
    faculty_id INT
);

-- changeset dmitriy:1
CREATE INDEX idx_student_name ON student (name);