/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamma.cvd.calculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Erling Austvoll
 */

// Name lengths in this class are limited to 35 characters for each of first and
// last names, as per recommendation of the e-Government Interoperability
// Framework (e-GIF) Data Types Standards.
// Ref.: http://webarchive.nationalarchives.gov.uk/+/http:/www.cabinetoffice.gov.uk/media/254290/GDS%20Catalogue%20Vol%202.pdf

public class CVDPatient {
    
    public static final char MALE = 'M';
    public static final char FEMALE = 'F';
    
    private int patientId;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private char sex;
    private List<CVDRiskData> riskData;
    
    protected CVDPatient(String firstName, String lastName, LocalDate birthdate,
            char sex, int patientId) {
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null.");
        }
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("lastName cannot be null.");
        }
        
        this.firstName =
                firstName.substring(0, Math.min(firstName.length(), 35));
        this.lastName = lastName.substring(0, Math.min(lastName.length(), 35));
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
        this.patientId = patientId;
        this.riskData = new ArrayList<>();
    }

    public int getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public char getSex() {
        return sex;
    }

    protected void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    
    protected boolean setPatientName(String firstName, String lastName) {
        if (firstName.isEmpty()) {
            return false;
        }
        if (lastName.isEmpty()) {
            return false;
        }
        this.firstName =
                firstName.substring(0, Math.min(firstName.length(), 35));
        this.lastName = lastName.substring(0, Math.min(lastName.length(), 35));
        return true;
    }

    public List<CVDRiskData> getRiskData() {
        return Collections.unmodifiableList(this.riskData);
    }

    protected void setRiskData(List<CVDRiskData> riskData) {
        this.riskData = riskData;
    }

    protected boolean addToRiskData(CVDRiskData data) {
        int newTestId = 1;
        for (CVDRiskData riskDataEntry : this.riskData) {
            if (riskDataEntry.getTestId() >= newTestId) {
                newTestId = riskDataEntry.getTestId() + 1;
            }
        }
        data.setTestId(newTestId);
        return this.riskData.add(data);
    }
    
    protected boolean removeFromRiskData(CVDRiskData data) {
        return this.riskData.remove(data);
    }
}
