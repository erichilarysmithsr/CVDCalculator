package gamma.cvd.calculator;

import java.time.LocalDate;

/**
 * This risk data calculator is created on the basis of the two charts, male and
 * female in Appendix A in the coursework document. The score-to-risk
 * calculation changes based on whether the patient is male or female, and
 * whether LDL or cholesterol data is provided. LDL/cholesterol and HDL levels
 * can be measured either as mmol/L or mg/dl, there are therefore four
 * constructor methods to allow any combination of these.
 * @author Erling Austvoll
 */
public class CVDRiskData {
    
    public static final char MALE = 'M';
    public static final char FEMALE = 'F';
    public static final String LDL = "Low-density lipoprotein";
    public static final String CHOL = "Cholesterol";
    
    private int testId;
    private char sex;
    private int age;
    private String cholesterolType;
    private float cholesterolMmolL;
    private float hdlMmolL;
    private int bloodPressureSystolicMmHg;
    private int bloodPressureDiastolicMmHg;
    private boolean isDiabetic;
    private boolean isSmoker;
    private LocalDate testDate;

    public CVDRiskData(char sex, int age, String cholesterolType,
            float cholesterolMmolL, float hdlMmolL,
            int bloodPressureSystolicMmHg, int bloodPressureDiastolicMmHg,
            boolean isDiabetic, boolean isSmoker, LocalDate testDate) {
        if (sex == MALE || sex == FEMALE) {
            this.sex = sex;
        } else {
            throw new IllegalArgumentException("Invalid sex.");
        }
        if (age >= 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Invalid age.");
        }
        if (cholesterolType.equalsIgnoreCase(LDL) ||
                cholesterolType.equalsIgnoreCase(CHOL)) {
                this.cholesterolType = cholesterolType;
        } else {
            throw new IllegalArgumentException("Invalid cholesterol type.");
        }
        if (cholesterolMmolL > 0f) {
            this.cholesterolMmolL = cholesterolMmolL;
        } else {
            throw new IllegalArgumentException("Invalid cholesterol value.");
        }
        if (hdlMmolL > 0f) {
            this.hdlMmolL = hdlMmolL;
        } else {
            throw new IllegalArgumentException("Invalid HDL value.");
        }
        if (bloodPressureSystolicMmHg > 0) {
            this.bloodPressureSystolicMmHg = bloodPressureSystolicMmHg;
        } else {
            throw new IllegalArgumentException("Invalid systolic bloodpressure"
                    + " value.");
        }
        if (bloodPressureDiastolicMmHg > 0) {
            this.bloodPressureDiastolicMmHg = bloodPressureDiastolicMmHg;
        } else {
            throw new IllegalArgumentException("Invalid diastolic bloodpressure"
                    + " value.");
        }
        this.isDiabetic = isDiabetic;
        this.isSmoker = isSmoker;
        this.testDate = testDate;
    }
    
    public CVDRiskData(char sex, int age, String cholesterolType, 
            int cholesterolMgDl, float hdlMmolL,
            int bloodPressureSystolicMmHg, int bloodPressureDiastolicMmHg,
            boolean isDiabetic, boolean isSmoker, LocalDate testDate) {
        if (sex == MALE || sex == FEMALE) {
            this.sex = sex;
        } else {
            throw new IllegalArgumentException("Invalid sex.");
        }
        if (age >= 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Invalid age.");
        }
        if (cholesterolType.equalsIgnoreCase(LDL) ||
                cholesterolType.equalsIgnoreCase(CHOL)) {
                this.cholesterolType = cholesterolType;
        } else {
            throw new IllegalArgumentException("Invalid cholesterol type.");
        }
        if (cholesterolMgDl > 0) {
            this.cholesterolMmolL = (float)cholesterolMgDl * 0.0259f;
        } else {
            throw new IllegalArgumentException("Invalid cholesterol value.");
        }
        if (hdlMmolL > 0f) {
            this.hdlMmolL = hdlMmolL;
        } else {
            throw new IllegalArgumentException("Invalid HDL value.");
        }
        if (bloodPressureSystolicMmHg > 0) {
            this.bloodPressureSystolicMmHg = bloodPressureSystolicMmHg;
        } else {
            throw new IllegalArgumentException("Invalid systolic bloodpressure"
                    + " value.");
        }
        if (bloodPressureDiastolicMmHg > 0) {
            this.bloodPressureDiastolicMmHg = bloodPressureDiastolicMmHg;
        } else {
            throw new IllegalArgumentException("Invalid diastolic bloodpressure"
                    + " value.");
        }
        this.isDiabetic = isDiabetic;
        this.isSmoker = isSmoker;
        this.testDate = testDate;
    }
    
    public CVDRiskData(char sex, int age, String cholesterolType,
            float cholesterolMmolL, int hdlMgDl,
            int bloodPressureSystolicMmHg, int bloodPressureDiastolicMmHg,
            boolean isDiabetic, boolean isSmoker, LocalDate testDate) {
        if (sex == MALE || sex == FEMALE) {
            this.sex = sex;
        } else {
            throw new IllegalArgumentException("Invalid sex.");
        }
        if (age >= 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Invalid age.");
        }
        if (cholesterolType.equalsIgnoreCase(LDL) ||
                cholesterolType.equalsIgnoreCase(CHOL)) {
                this.cholesterolType = cholesterolType;
        } else {
            throw new IllegalArgumentException("Invalid cholesterol type.");
        }
        if (cholesterolMmolL > 0f) {
            this.cholesterolMmolL = cholesterolMmolL;
        } else {
            throw new IllegalArgumentException("Invalid cholesterol value.");
        }
        if (hdlMgDl > 0) {
            this.hdlMmolL = (float)hdlMgDl * 0.0259f;
        } else {
            throw new IllegalArgumentException("Invalid HDL value.");
        }
        if (bloodPressureSystolicMmHg > 0) {
            this.bloodPressureSystolicMmHg = bloodPressureSystolicMmHg;
        } else {
            throw new IllegalArgumentException("Invalid systolic bloodpressure"
                    + " value.");
        }
        if (bloodPressureDiastolicMmHg > 0) {
            this.bloodPressureDiastolicMmHg = bloodPressureDiastolicMmHg;
        } else {
            throw new IllegalArgumentException("Invalid diastolic bloodpressure"
                    + " value.");
        }
        this.isDiabetic = isDiabetic;
        this.isSmoker = isSmoker;
        this.testDate = testDate;
    }
    
    public CVDRiskData(char sex, int age, String cholesterolType, 
            int cholesterolMgDl, int hdlMgDl,
            int bloodPressureSystolicMmHg, int bloodPressureDiastolicMmHg,
            boolean isDiabetic, boolean isSmoker, LocalDate testDate) {
        if (sex == MALE || sex == FEMALE) {
            this.sex = sex;
        } else {
            throw new IllegalArgumentException("Invalid sex.");
        }
        if (age >= 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Invalid age.");
        }
        if (cholesterolType.equalsIgnoreCase(LDL) ||
                cholesterolType.equalsIgnoreCase(CHOL)) {
                this.cholesterolType = cholesterolType;
        } else {
            throw new IllegalArgumentException("Invalid cholesterol type.");
        }
        if (cholesterolMgDl > 0) {
            this.cholesterolMmolL = (float)cholesterolMgDl * 0.0259f;
        } else {
            throw new IllegalArgumentException("Invalid cholesterol value.");
        }
        if (hdlMgDl > 0) {
            this.hdlMmolL = (float)hdlMgDl * 0.0259f;
        } else {
            throw new IllegalArgumentException("Invalid HDL value.");
        }
        if (bloodPressureSystolicMmHg > 0) {
            this.bloodPressureSystolicMmHg = bloodPressureSystolicMmHg;
        } else {
            throw new IllegalArgumentException("Invalid systolic bloodpressure"
                    + " value.");
        }
        if (bloodPressureDiastolicMmHg > 0) {
            this.bloodPressureDiastolicMmHg = bloodPressureDiastolicMmHg;
        } else {
            throw new IllegalArgumentException("Invalid diastolic bloodpressure"
                    + " value.");
        }
        this.isDiabetic = isDiabetic;
        this.isSmoker = isSmoker;
        this.testDate = testDate;
    }

    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        }
    }
    
    public boolean setCholesterolMgDl(String cholesterolType, int mgDl) {
        if (cholesterolType.equalsIgnoreCase(LDL) ||
                cholesterolType.equalsIgnoreCase(CHOL)) {
            this.cholesterolType = cholesterolType;
            this.cholesterolMmolL = (float)mgDl * 0.0259f;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean setCholesterolMmolL(String cholesterolType, float mmolL) {
        if (cholesterolType.equalsIgnoreCase(LDL) ||
                cholesterolType.equalsIgnoreCase(CHOL)) {
            this.cholesterolType = cholesterolType;
            this.cholesterolMmolL = mmolL;
            return true;
        } else {
            return false;
        }
    }
    
    public void setHdlMgDl(int mgDl) {
        this.hdlMmolL = (float)mgDl * 0.0259f;
    }
    
    public void setHdlMmolL(float mmolL) {
        this.hdlMmolL = mmolL;
    }
    
    public void setBloodPressure(int bloodPressureSystolicMmHg,
            int bloodPressureDiastolicMmHg) {
        this.bloodPressureSystolicMmHg = bloodPressureSystolicMmHg;
        this.bloodPressureDiastolicMmHg = bloodPressureDiastolicMmHg;
    }

    public void setIsDiabetic(boolean isDiabetic) {
        this.isDiabetic = isDiabetic;
    }

    public void setIsSmoker(boolean isSmoker) {
        this.isSmoker = isSmoker;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    protected void setTestId(int testId) {
        this.testId = testId;
    }
    
    public char getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public String getCholesterolType() {
        return cholesterolType;
    }

    public float getCholesterolMmolL() {
        return cholesterolMmolL;
    }

    public float getHdlMmolL() {
        return hdlMmolL;
    }

    public int getBloodPressureSystolicMmHg() {
        return bloodPressureSystolicMmHg;
    }

    public int getBloodPressureDiastolicMmHg() {
        return bloodPressureDiastolicMmHg;
    }

    public boolean isDiabetic() {
        return isDiabetic;
    }

    public boolean isSmoker() {
        return isSmoker;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public int getTestId() {
        return testId;
    }
    
    public int calculateRiskScore() {
        int score = 0;
        
        if (this.sex == MALE) {
            if (this.age <= 34) {
                score += -1;
            } else if (this.age >= 40 && this.age <= 44) {
                score += 1;
            } else if (this.age >= 45 && this.age <= 49) {
                score += 2;
            } else if (this.age >= 50 && this.age <= 54) {
                score += 3;
            } else if (this.age >= 55 && this.age <= 59) {
                score += 4;
            } else if (this.age >= 60 && this.age <= 64) {
                score += 5;
            } else if (this.age >= 65 && this.age <= 69) {
                score += 6;
            } else if (this.age >= 70) {
                score += 7;
            }

            switch (this.cholesterolType) {
                case LDL:
                    if (this.cholesterolMmolL < 2.59f) {
                        score += -3;
                    } else if (this.cholesterolMmolL >= 4.15f &&
                            this.cholesterolMmolL <= 4.91f) {
                        score += 1;
                    } else if (this.cholesterolMmolL >= 4.92f) {
                        score += 2;
                    }
                    if (this.hdlMmolL <= 0.90f) {
                        score += 2;
                    } else if (this.hdlMmolL >= 0.91f && this.hdlMmolL <= 1.16f) {
                        score += 1;
                    } else if (this.hdlMmolL >= 1.56f) {
                        score += -1;
                    }
                    break;
                case CHOL:
                    if (this.cholesterolMmolL < 4.14f) {
                        score += -3;
                    } else if (this.cholesterolMmolL >= 5.18f &&
                            this.cholesterolMmolL <= 6.21f) {
                        score += 1;
                    } else if (this.cholesterolMmolL >= 6.22f &&
                            this.cholesterolMmolL <= 7.24f) {
                        score += 2;
                    } else if (this.cholesterolMmolL >= 7.25f) {
                        score += 3;
                    }
                    if (this.hdlMmolL <= 0.90f) {
                        score += 2;
                    } else if (this.hdlMmolL >= 0.91f && this.hdlMmolL <= 1.16f) {
                        score += 1;
                    } else if (this.hdlMmolL >= 1.56f) {
                        score += -2;
                    }
                    break;
            }

            if (this.bloodPressureDiastolicMmHg < 80) {
                if (this.bloodPressureSystolicMmHg >= 130 &&
                        this.bloodPressureSystolicMmHg <= 139) {
                    score += 1;
                } else if (this.bloodPressureSystolicMmHg >=140 &&
                        this.bloodPressureSystolicMmHg <=159) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 160) {
                    score += 3;
                }
            } else if (this.bloodPressureDiastolicMmHg >= 80 &&
                    this.bloodPressureDiastolicMmHg <= 84) {
                if (this.bloodPressureSystolicMmHg >= 130 &&
                        this.bloodPressureSystolicMmHg <= 139) {
                    score += 1;
                } else if (this.bloodPressureSystolicMmHg >=140 &&
                        this.bloodPressureSystolicMmHg <=159) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 160) {
                    score += 3;
                }
            } else if (this.bloodPressureDiastolicMmHg >= 85 &&
                    this.bloodPressureDiastolicMmHg <= 89) {
                if (this.bloodPressureSystolicMmHg < 120) {
                    score += 1;
                } else if (this.bloodPressureSystolicMmHg >= 120 &&
                        this.bloodPressureSystolicMmHg <= 129) {
                    score += 1;
                } else if (this.bloodPressureSystolicMmHg >= 130 &&
                        this.bloodPressureSystolicMmHg <= 139) {
                    score += 1;
                } else if (this.bloodPressureSystolicMmHg >=140 &&
                        this.bloodPressureSystolicMmHg <=159) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 160) {
                    score += 3;
                }
            } else if (this.bloodPressureDiastolicMmHg >= 90 &&
                    this.bloodPressureDiastolicMmHg <= 99) {
                if (this.bloodPressureSystolicMmHg < 120) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 120 &&
                        this.bloodPressureSystolicMmHg <= 129) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 130 &&
                        this.bloodPressureSystolicMmHg <= 139) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >=140 &&
                        this.bloodPressureSystolicMmHg <=159) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 160) {
                    score += 3;
                }
            } else if (this.bloodPressureDiastolicMmHg >= 100) {
                if (this.bloodPressureSystolicMmHg < 120) {
                    score += 3;
                } else if (this.bloodPressureSystolicMmHg >= 120 &&
                        this.bloodPressureSystolicMmHg <= 129) {
                    score += 3;
                } else if (this.bloodPressureSystolicMmHg >= 130 &&
                        this.bloodPressureSystolicMmHg <= 139) {
                    score += 3;
                } else if (this.bloodPressureSystolicMmHg >=140 &&
                        this.bloodPressureSystolicMmHg <=159) {
                    score += 3;
                } else if (this.bloodPressureSystolicMmHg >= 160) {
                    score += 3;
                }
            }

            if (this.isDiabetic) {
                score += 2;
            }

            if (this.isSmoker) {
                score += 2;
            }
        } else if (this.sex == FEMALE) {
            if (this.age <= 34) {
                score += -9;
            } else if (this.age >= 35 && this.age <= 39) {
                score += -4;
            } else if (this.age >= 45 && this.age <= 49) {
                score += 3;
            } else if (this.age >= 50 && this.age <= 54) {
                score += 6;
            } else if (this.age >= 55 && this.age <= 59) {
                score += 7;
            } else if (this.age >= 60 && this.age <= 64) {
                score += 8;
            } else if (this.age >= 65 && this.age <= 69) {
                score += 8;
            } else if (this.age >= 70) {
                score += 8;
            }

            switch (this.cholesterolType) {
                case LDL:
                    if (this.cholesterolMmolL < 2.59f) {
                        score += -2;
                    } else if (this.cholesterolMmolL >= 4.15f &&
                            this.cholesterolMmolL <= 4.91f) {
                        score += 2;
                    } else if (this.cholesterolMmolL >= 4.92f) {
                        score += 2;
                    }
                    if (this.hdlMmolL <= 0.90f) {
                        score += 5;
                    } else if (this.hdlMmolL >= 0.91f && this.hdlMmolL <= 1.16f) {
                        score += 2;
                    } else if (this.hdlMmolL >= 1.17f && this.hdlMmolL <= 1.29f) {
                        score += 1;
                    } else if (this.hdlMmolL >= 1.56f) {
                        score += -2;
                    }
                    break;
                case CHOL:
                    if (this.cholesterolMmolL < 4.14f) {
                        score += -2;
                    } else if (this.cholesterolMmolL >= 5.18f &&
                            this.cholesterolMmolL <= 6.21f) {
                        score += 1;
                    } else if (this.cholesterolMmolL >= 6.22f &&
                            this.cholesterolMmolL <= 7.24f) {
                        score += 1;
                    } else if (this.cholesterolMmolL >= 7.25f) {
                        score += 3;
                    }
                    if (this.hdlMmolL <= 0.90f) {
                        score += 5;
                    } else if (this.hdlMmolL >= 0.91f && this.hdlMmolL <= 1.16f) {
                        score += 2;
                    } else if (this.hdlMmolL >= 1.17f && this.hdlMmolL <= 1.29f) {
                        score += 1;
                    } else if (this.hdlMmolL >= 1.56f) {
                        score += -3;
                    }
                    break;
            }

            if (this.bloodPressureDiastolicMmHg < 80) {
                if (this.bloodPressureSystolicMmHg < 120) {
                    score += -3;
                } else if (this.bloodPressureSystolicMmHg >= 130 &&
                        this.bloodPressureSystolicMmHg <= 139) {
                    score += 1;
                } else if (this.bloodPressureSystolicMmHg >=140 &&
                        this.bloodPressureSystolicMmHg <=159) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 160) {
                    score += 3;
                }
            }  else if (this.bloodPressureDiastolicMmHg >= 90 &&
                    this.bloodPressureDiastolicMmHg <= 99) {
                if (this.bloodPressureSystolicMmHg < 120) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 120 &&
                        this.bloodPressureSystolicMmHg <= 129) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 130 &&
                        this.bloodPressureSystolicMmHg <= 139) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >=140 &&
                        this.bloodPressureSystolicMmHg <=159) {
                    score += 2;
                } else if (this.bloodPressureSystolicMmHg >= 160) {
                    score += 3;
                }
            } else if (this.bloodPressureDiastolicMmHg >= 100) {
                if (this.bloodPressureSystolicMmHg < 120) {
                    score += 3;
                } else if (this.bloodPressureSystolicMmHg >= 120 &&
                        this.bloodPressureSystolicMmHg <= 129) {
                    score += 3;
                } else if (this.bloodPressureSystolicMmHg >= 130 &&
                        this.bloodPressureSystolicMmHg <= 139) {
                    score += 3;
                } else if (this.bloodPressureSystolicMmHg >=140 &&
                        this.bloodPressureSystolicMmHg <=159) {
                    score += 3;
                } else if (this.bloodPressureSystolicMmHg >= 160) {
                    score += 3;
                }
            }

            if (this.isDiabetic) {
                score += 4;
            }

            if (this.isSmoker) {
                score += 2;
            }
        }
        return score;
    }
    
    public int getRiskPercentage(int score) {
        int percentageRisk = 0;
        if (this.sex == MALE) {
            switch (this.cholesterolType) {
                case LDL:
                    if (score <= -3) {
                        percentageRisk = 1;
                    } else if (score >= -2 && score <= -1) {
                        percentageRisk = 2;
                    } else if (score == 0) {
                        percentageRisk = 3;
                    } else if (score >= 1 && score <= 2) {
                        percentageRisk = 4;
                    } else if (score == 3) {
                        percentageRisk = 6;
                    } else if (score == 4) {
                        percentageRisk = 7;
                    } else if (score == 5) {
                        percentageRisk = 9;
                    } else if (score == 6) {
                        percentageRisk = 11;
                    } else if (score == 7) {
                        percentageRisk = 14;
                    } else if (score == 8) {
                        percentageRisk = 18;
                    } else if (score == 9) {
                        percentageRisk = 22;
                    } else if (score == 10) {
                        percentageRisk = 27;
                    } else if (score == 11) {
                        percentageRisk = 33;
                    } else if (score == 12) {
                        percentageRisk = 40;
                    } else if (score == 13) {
                        percentageRisk = 47;
                    } else if (score >= 14) {
                        percentageRisk = 56;
                    }
                    break;
                case CHOL:
                    if (score <= -1) {
                        percentageRisk = 2;
                    } else if (score >= 0 && score <= 1) {
                        percentageRisk = 3;
                    } else if (score == 2) {
                        percentageRisk = 4;
                    } else if (score == 3) {
                        percentageRisk = 5;
                    } else if (score == 4) {
                        percentageRisk = 7;
                    } else if (score == 5) {
                        percentageRisk = 8;
                    } else if (score == 6) {
                        percentageRisk = 10;
                    } else if (score == 7) {
                        percentageRisk = 13;
                    } else if (score == 8) {
                        percentageRisk = 16;
                    } else if (score == 9) {
                        percentageRisk = 20;
                    } else if (score == 10) {
                        percentageRisk = 25;
                    } else if (score == 11) {
                        percentageRisk = 31;
                    } else if (score == 12) {
                        percentageRisk = 37;
                    } else if (score == 13) {
                        percentageRisk = 45;
                    } else if (score >= 14) {
                        percentageRisk = 53;
                    }
                    break;
            }
        } else if (this.sex == FEMALE) {
            switch (this.cholesterolType) {
                case LDL:
                    if (score <= -2) {
                        percentageRisk = 1;
                    } else if (score >= -1 && score <= 1) {
                        percentageRisk = 2;
                    } else if (score >= 2 && score <= 3) {
                        percentageRisk = 3;
                    } else if (score == 4) {
                        percentageRisk = 4;
                    } else if (score == 5) {
                        percentageRisk = 5;
                    } else if (score == 6) {
                        percentageRisk = 6;
                    } else if (score == 7) {
                        percentageRisk = 7;
                    } else if (score == 8) {
                        percentageRisk = 8;
                    } else if (score == 9) {
                        percentageRisk = 9;
                    } else if (score == 10) {
                        percentageRisk = 11;
                    } else if (score == 11) {
                        percentageRisk = 13;
                    } else if (score == 12) {
                        percentageRisk = 15;
                    } else if (score == 13) {
                        percentageRisk = 17;
                    } else if (score == 14) {
                        percentageRisk = 20;
                    } else if (score == 15) {
                        percentageRisk = 24;
                    } else if (score == 16) {
                        percentageRisk = 27;
                    } else if (score >= 17) {
                        percentageRisk = 32;
                    }
                    break;
                case CHOL:
                    if (score <= -2) {
                        percentageRisk = 1;
                    } else if (score >= -1 && score <= 1) {
                        percentageRisk = 2;
                    } else if (score >= 2 && score <= 3) {
                        percentageRisk = 3;
                    } else if (score >= 4 && score <= 5) {
                        percentageRisk = 4;
                    } else if (score == 6) {
                        percentageRisk = 5;
                    } else if (score == 7) {
                        percentageRisk = 6;
                    } else if (score == 8) {
                        percentageRisk = 7;
                    } else if (score == 9) {
                        percentageRisk = 8;
                    } else if (score == 10) {
                        percentageRisk = 10;
                    } else if (score == 11) {
                        percentageRisk = 11;
                    } else if (score == 12) {
                        percentageRisk = 13;
                    } else if (score == 13) {
                        percentageRisk = 15;
                    } else if (score == 14) {
                        percentageRisk = 18;
                    } else if (score == 15) {
                        percentageRisk = 20;
                    } else if (score == 16) {
                        percentageRisk = 24;
                    } else if (score >= 17) {
                        percentageRisk = 27;
                    }
                    break;
            }
        }
        return percentageRisk;
    }
}
