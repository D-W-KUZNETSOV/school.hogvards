CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name TEXT,
    age INTEGER,
    is_license BOOLEAN
);

CREATE TABLE auto (
    id SERIAL PRIMARY KEY,
    person_id INTEGER,
    brand TEXT,
    model TEXT,
    price VARCHAR,
    FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE TABLE person_auto (
    person_id INTEGER,
    auto_id INTEGER,
    PRIMARY KEY (person_id, auto_id),
    FOREIGN KEY (person_id) REFERENCES person(id),
    FOREIGN KEY (auto_id) REFERENCES auto(id)
);

