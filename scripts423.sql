select s.name,s.age from student s
left join faculty f on(s.faculty_id=f.id)

select * from student s
join avatar a on (s.id=a.student_id)