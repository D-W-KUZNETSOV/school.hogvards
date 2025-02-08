create table person(id serial primary key
,name text,
age int,is_license boolean
);
create table auto(id_serial primary key
person_id INT
,brand text,
model text,
price varchar,
foreign key (person_id)references person(id)
);
