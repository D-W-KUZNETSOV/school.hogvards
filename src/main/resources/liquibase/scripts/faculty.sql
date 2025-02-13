-- liquibase formatted sql

-- changeset dmitriy:1
CREATE TABLE faculty (
    id INT,
    name TEXT,
    color TEXT
);

-- changeset dmitriy:2
-- Удаляем индекс
DROP INDEX IF EXISTS idx_faculty_name_color;

-- changeset dmitriy:3
-- Создаем индекс
CREATE INDEX idx_faculty_name_color ON faculty (name, color);

