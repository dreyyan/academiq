package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Enums
import ums.model.enums.Gender;

public abstract class Person {
    // * Attributes
    private static int personCount = 0;
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
        this.personId = generatePersonId();
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
    // [METHOD] Get full name
    public String getFullName() {
        return getFirstName() + " " + getMiddleName().charAt(0) + ". " + getLastName();
    }

    // [METHOD] Generate person ID
    private static String generatePersonId() {
        personCount++;
        return "P" + String.format("%05d", personCount); // Format: P00001
    }
    // [METHOD] Display student's information
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