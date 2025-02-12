SELECT s.name, s.age
FROM student s
LEFT JOIN faculty f ON (s.faculty_id = f.id)
WHERE f.name = 'Grifindor';

SELECT s.*, a.*
FROM student s
JOIN avatar a ON (s.id = a.student_id);


