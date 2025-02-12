-- liquibase formatted sql

-- changeset dmitriy:1
CREATE INDEX idx_student_name ON student (name);

CREATE INDEX idx_faculty_name_color ON faculty (name, color);
