# AcademIQ
_A Comprehensive University Management System via Object-Oriented Design_

**AcademIQ** is a Java-based command-line application that streamlines university operations through robust student, staff, and course management. Built with clean OOP principles and in-memory data structures, it features a menu-driven CLI for administrators, academic staff, and students to handle enrollment, grading, scheduling, and reporting — all within a secure, modular, and extensible architecture.

The primary purpose of **AcademIQ** is to serve as a centralized, efficient platform for managing academic workflows while demonstrating best-practice OOP design: encapsulation, inheritance, polymorphism, abstraction, and composition.

## FEATURES
Interactive CLI Menu – Navigate subsystems (SIS, ASIS, CMS, etc.) via numbered options with real-time feedback.  
In-Memory Data Management – Fast, lightweight persistence using `ArrayList`, `HashMap`, and custom collections.  
OOP-Driven Architecture – Full implementation of `Person`, `Staff`, `Student`, and specialized subclasses with polymorphic `displayInfo(): void` and `generateReport(): String`.  
Comprehensive Subsystems – SIS, ASIS, CMS, Grading, Enrollment, and Reporting modules fully linked via composition.  
Secure Data Handling – Private attributes with getters/setters; validation on GPA, enrollment status, and capacity.

## FUTURE IMPLEMENTATIONS
AI Recommendation Engine – Suggest courses based on GPA, prerequisites, and academic history.  
Real-Time Analytics Dashboard – Visualize enrollment trends, faculty load, and at-risk students.  
Role-Based Access Control – Admin, AcademicStaff, and Student login views.  
File-Based Persistence – Save/load data using JSON or CSV.  
Web Interface – Migrate CLI to Spring Boot + Thymeleaf or React frontend.

## UPDATES
Core UML model finalized with `Staff` hierarchy and `displayInfo(): void` standardization.  
CLI menu system with subsystem navigation implemented.  
In-memory data layer with `List` and `Map` collections in progress.  
Ongoing refinements to input validation, error handling, and report formatting.

## PROJECT DETAILS
Author: dreyyan, Dominique0912, yuuhnna  
Started: November 2025  
Finished:

## TECH STACK
Language: Java 17+  
Framework: Standard Library  
Libraries: None (pure Java)  
Data: In-memory collections (`ArrayList`, `HashMap`)

## INSTALLATION
### Prerequisites
- Java 17 or higher
- `git` (optional)

### Run the Application
```bash
git clone https://github.com/yourusername/academiq.git
cd academiq
javac -d out src/main/java/com/academiq/*.java
java -cp out com.academiq.Main