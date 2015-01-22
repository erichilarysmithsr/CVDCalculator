/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamma.cvd.calculator;

import java.time.LocalDate;

/**
 *
 * @author Erling Austvoll
 */
public class CVDPatient {
    
    public static final char MALE = 'M';
    public static final char FEMALE = 'F';
    
    private int patientId;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private char sex;

    public CVDPatient(String firstName, String lastName, LocalDate birthdate,
            char sex) {
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null.");
        }
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("lastName cannot be null.");
        }
        
        // Name lengths are limited to 35 characters for each of first and last
        // names, as per recommendation of the e-Government Interoperability
        // Framework (e-GIF) Data Types Standards.
        // Ref.: http://webarchive.nationalarchives.gov.uk/+/http:/www.cabinetoffice.gov.uk/media/254290/GDS%20Catalogue%20Vol%202.pdf
        this.firstName = firstName.substring(0, 35);
        this.lastName = lastName.substring(0, 35);
        if (birthdate.isBefore(LocalDate.now())) {
            this.birthdate = birthdate;
        } else {
            throw new IllegalArgumentException("Invalid birthdate.");
        }
        if (sex == MALE || sex == FEMALE) {
            this.sex = sex;
        } else {
            throw new IllegalArgumentException("Invalid sex.");
        }
    }
    
    public void setPatientName(String firstName, String lastName) {
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null.");
        }
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("lastName cannot be null.");
        }

        // Name lengths are limited to 35 characters for each of first and last
        // names, as per recommendation of the e-Government Interoperability
        // Framework (e-GIF) Data Types Standards.
        // Ref.: http://webarchive.nationalarchives.gov.uk/+/http:/www.cabinetoffice.gov.uk/media/254290/GDS%20Catalogue%20Vol%202.pdf
        this.firstName = firstName.substring(0, 35);
        this.lastName = lastName.substring(0, 35);
    }
}
