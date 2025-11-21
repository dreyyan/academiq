package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Enums
import ums.model.enums.Gender;

import ums.util.CSV;

public abstract class Person {
    // * Attributes
    private String personId;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private String contactNumber;
    private String email;

    // * Constructor (Parameterized)
    public Person(String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email) {
        this.personId = CSV.generatePersonId();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    // * Getters
    public String getPersonId() { return this.personId; }
    public String getFirstName() { return this.firstName; }
    public String getMiddleName() { return this.middleName; }
    public String getLastName() { return this.lastName; }
    public LocalDate getDateOfBirth() { return this.dateOfBirth; }
    public Gender getGender() { return this.gender; }
    public String getAddress() { return this.address; }
    public String getContactNumber() { return this.contactNumber; }
    public String getEmail() { return this.email; }

    // * Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setGender(Gender gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setEmail(String email) { this.email = email; }

    // * Methods
    // [METHOD] Get full name
    public String getFullName() {
        return getFirstName() + " " + getMiddleName().charAt(0) + ". " + getLastName();
    }

    // [METHOD] Display student's profile information
    public List<String> getProfileInformation() {
        List<String> info = new ArrayList<>(); // Stores student's profile information

        info.add(getFirstName());
        info.add(getMiddleName());
        info.add(getLastName());
        info.add(getDateOfBirth().toString());
        info.add(getGender().toString());
        info.add(getAddress());
        info.add(getContactNumber());
        info.add(getEmail());

        return info;
    }

    // [ABSTRACT] Return a string of person's summary report
    abstract String generateReport();
}