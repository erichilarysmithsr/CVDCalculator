/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamma.cvd.calculator.print;

import gamma.cvd.calculator.CVDPatient;
import gamma.cvd.calculator.CVDRiskData;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author Erling Austvoll, K0927253
 */
public class CVDPrint {
    
    private PDDocument document;

    public CVDPrint() {
    }
    
    public void createPdfDocument(CVDPatient patient) throws IOException {
        this.document = new PDDocument();
        List<PDPage> pageList = new ArrayList<>();
        PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
        pageList.add(page1);
        PDRectangle rect = pageList.get(0).getMediaBox();
        this.document.addPage(pageList.get(0));
        PDFont fontHelveticaBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontCourier = PDType1Font.COURIER;
        PDFont fontCourierBold = PDType1Font.COURIER_BOLD;
        PDPageContentStream cos =
                new PDPageContentStream(this.document, pageList.get(0));
        int leftMargin = 50;
        int initialLineSpace = 70;
        int lineSpace = 15;
        int patientLineSpace = 80;
        int testDataLineSpace = 95;
        int line = 0;
        cos.beginText();
        cos.setFont(fontHelveticaBold, 18);
        cos.moveTextPositionByAmount(leftMargin,
                rect.getHeight() - initialLineSpace);
        cos.drawString("Test record for " + patient.getFirstName() + " " +
                patient.getLastName());
        cos.endText();
        
        cos.beginText();
        cos.setFont(fontCourier, 12);
        cos.moveTextPositionByAmount(
                leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        cos.drawString("Patient no.: ");
        int lead = 42;
        if (patient.getPatientId() > 9 && patient.getPatientId() < 100) {
            lead--;
        } else if (patient.getPatientId() > 99 &&
                patient.getPatientId() < 1000) {
            lead = lead - 2;
        } else if (patient.getPatientId() > 999 &&
                patient.getPatientId() < 10000) {
            lead = lead - 3;
        } else if (patient.getPatientId() > 9999) {
            lead = lead - 3;
        }
        for (int i = 0; i < lead; i++) {
            cos.drawString(".");
        }
        cos.drawString(" " + patient.getPatientId());
        cos.endText();
        
        cos.beginText();
        cos.moveTextPositionByAmount(
                leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        cos.drawString("First name: ");
        lead = 44 - patient.getFirstName().length();
        for (int i = 0; i < lead; i++) {
            cos.drawString(".");
        }
        cos.drawString(" " + patient.getFirstName());
        cos.endText();
        
        cos.beginText();
        cos.moveTextPositionByAmount(
                leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        cos.drawString("Last name: ");
        lead = 45 - patient.getLastName().length();
        for (int i = 0; i < lead; i++) {
            cos.drawString(".");
        }
        cos.drawString(" " + patient.getLastName());
        cos.endText();
        
        cos.beginText();
        cos.moveTextPositionByAmount(
                leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        cos.drawString("Date of birth: ");
        lead = 31;
        for (int i = 0; i < lead; i++) {
            cos.drawString(".");
        }
        cos.drawString(" " + patient.getBirthdate().toString());
        cos.endText();
        
        cos.beginText();
        cos.moveTextPositionByAmount(
                leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        cos.drawString( "Sex: ");
        lead = 50;
        for (int i = 0; i < lead; i++) {
            cos.drawString(".");
        }
        cos.drawString(" " + patient.getSex());
        cos.endText();
        int n = 0;
        for (CVDRiskData data : patient.getRiskData()) {
            if (n > 0 && n % 3 == 0) {
                cos.close();
                PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
                pageList.add(page);
                this.document.addPage(pageList.get(n/3));
                cos = new PDPageContentStream(this.document, pageList.get(n/3));
                line = 0;
                cos.beginText();
                cos.setFont(fontHelveticaBold, 18);
                cos.moveTextPositionByAmount(leftMargin,
                        rect.getHeight() - initialLineSpace);
                cos.drawString("Test record for " + patient.getFirstName() +
                        " " + patient.getLastName() + ", page " + ((n/3) + 1));
                cos.endText();
            }
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.endText();
            
            cos.beginText();
            cos.setFont(fontCourierBold, 12);
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Test ID: ");
            lead = 46;
            if (data.getTestId() > 9) {
                lead--;
            }
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " + data.getTestId());
            cos.endText();
            
            cos.beginText();
            cos.setFont(fontCourier, 12);
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Test date: ");
            lead = 35;
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " + data.getTestDate().toString());
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Cholesterol type: ");
            if (data.getCholesterolType().equalsIgnoreCase(CVDRiskData.LDL)) {
                lead = 15;
            } else {
                lead = 27;
            }
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " + data.getCholesterolType());
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Cholesterol mmol/L: ");
            if (data.getCholesterolMmolL() < 10) {
                lead = 32;
            } else {
                lead = 31;
            }
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " +
                    String.format("%.2f", data.getCholesterolMmolL()));
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("HDL mmol/L: ");
            if (data.getHdlMmolL() < 10) {
                lead = 40;
            } else {
                lead = 39;
            }
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " +
                    String.format("%.2f", data.getHdlMmolL()));
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Diastolic blood pressure: ");
            if (data.getBloodPressureDiastolicMmHg() < 100) {
                lead = 28;
            } else {
                lead = 27;
            }
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " + data.getBloodPressureDiastolicMmHg());
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Systolic blood pressure: ");
            if (data.getBloodPressureSystolicMmHg()< 100) {
                lead = 29;
            } else {
                lead = 28;
            }
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " + data.getBloodPressureSystolicMmHg());
            cos.endText();
            
            String smoker;
            if (data.isSmoker()) {
                smoker = "Yes";
                lead = 34;
            } else {
                smoker = "No";
                lead = 35;
            }
            
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Patient is smoker: ");
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " + smoker);
            cos.endText();
            
            String diabetic;
            if (data.isDiabetic()) {
                diabetic = "Yes";
                lead = 32;
            } else {
                diabetic = "No";
                lead = 33;
            }
            
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Patient is diabetic: ");
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " + diabetic);
            cos.endText();
            
            int score = data.calculateRiskScore();
            if (score < 0 || score > 9) {
                lead = 42;
            } else {
                lead = 43;
            }
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Risk score: ");
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " + score);
            cos.endText();
            
            int riskPercentage = data.getRiskPercentage(score);
            if (riskPercentage < 10) {
                lead = 36;
            } else {
                lead = 35;
            }
            cos.beginText();
            cos.moveTextPositionByAmount(
                    leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            cos.drawString("Risk percentage: ");
            for (int i = 0; i < lead; i++) {
                cos.drawString(".");
            }
            cos.drawString(" " + riskPercentage + " %");
            cos.endText();
            n++;
        }
        
        cos.close();
}
    
    public void savePatientDataToPdf(CVDPatient patient, String outputFileName)
            throws IOException, COSVisitorException {
        createPdfDocument(patient);
        this.document.save(outputFileName);
        this.document.close();
    }
    
        public void printPatientData(CVDPatient patient)
            throws IOException, PrinterException {
        createPdfDocument(patient);
        this.document.print();
        this.document.close();
    }
}
