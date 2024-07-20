create table if not exists professor (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name TEXT,
  department TEXT
);

create table if not exists course (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name TEXT,
  credits INTEGER,
  professorId INTEGER,
  FOREIGN KEY (professorId) REFERENCES professor(id)
);

create table if not exists student (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT,
    email TEXT
);

create table if not exists course_student (
  courseId INTEGER,
  studentId INTEGER,
  PRIMARY KEY (courseId, studentId),
  FOREIGN KEY (courseId) REFERENCES course(id),
  FOREIGN KEY (studentId) REFERENCES student(id) 
);