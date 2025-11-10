package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;

// [IMPORT] Enums
import ums.model.enums.Gender;

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
    public Person(String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email) {
        this.personId = personId;
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
    String getPersonId() { return this.personId; }
    String getFirstName() { return this.firstName; }
    String getMiddleName() { return this.middleName; }
    String getLastName() { return this.lastName; }
    LocalDate getDateOfBirth() { return this.dateOfBirth; }
    Gender getGender() { return this.gender; }
    String getAddress() { return this.address; }
    String getContactNumber() { return this.contactNumber; }
    String getEmail() { return this.email; }

    // * Setters
    void setFirstName(String firstName) { this.firstName = firstName; }
    void setMiddleName(String middleName) { this.middleName = middleName; }
    void setLastName(String lastName) { this.lastName = lastName; }
    void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    void setGender(Gender gender) { this.gender = gender; }
    void setAddress(String address) { this.address = address; }
    void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    void setEmail(String email) { this.email = email; }

    // * Methods
    // [ABSTRACT] Display person's information in a readable format
    abstract void displayInfo();

    // [ABSTRACT] Return a string of person's summary report
    abstract String generateReport();
}