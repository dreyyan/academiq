# PROJECT STRUCTURE

university-management-system/
│
├── src/
│   └── ums/
│       ├── UniversityManagementSystem.java
│       │
│       ├── model/
│       │   ├── Person.java
│       │   ├── Enrollee.java
│       │   ├── Student.java
│       │   ├── Undergraduate.java
│       │   ├── Graduate.java
│       │   ├── Freshman.java
│       │   ├── Sophomore.java
│       │   ├── Junior.java
│       │   ├── Senior.java
│       │   ├── Master.java
│       │   ├── PhD.java
│       │   ├── Faculty.java
│       │   ├── Teacher.java
│       │   ├── Admin.java
│       │   ├── Librarian.java
│       │   ├── Department.java
│       │   └── Course.java
│       │
│       ├── repository/
│       │   └── DataStore.java
│       │
│       ├── service/
│       │   ├── StudentService.java
│       │   └── EnrollmentService.java
│       │
│       ├── ui/
│       │   ├── MainMenu.java
│       │   ├── StudentMenu.java
│       │   └── FacultyMenu.java
│       │
│       └── util/
│           ├── CSVReader.java
│           ├── InputValidator.java
│           ├── ConsoleUtils.java
│           └── Logger.java
│
├── data/
│   ├── students.csv
│   ├── faculty.csv
│   ├── courses.csv
│   └── enrollments.csv
│
├── bin/
│
├── compile.bat
├── run.bat
│
└── README.md