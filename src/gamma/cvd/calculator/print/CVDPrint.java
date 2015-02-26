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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public CVDPrint() {
    }
    
    private void writeLine(String left, String right, PDPageContentStream cos)
            throws IOException {
        int lead = 54 - (left.length() + right.length());
        cos.drawString(left + " ");
        for (int i = 0; i < lead; i++) {
            cos.drawString(".");
        }
        cos.drawString(" " + right);
    }
    
    private PDDocument createPdfDocument(CVDPatient patient)
            throws IOException {
        PDDocument document = new PDDocument();
        List<PDPage> pageList = new ArrayList<>();
        PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
        pageList.add(page1);
        PDRectangle rect = pageList.get(0).getMediaBox();
        document.addPage(pageList.get(0));
        PDFont fontHelveticaBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontCourier = PDType1Font.COURIER;
        PDFont fontCourierBold = PDType1Font.COURIER_BOLD;
        PDPageContentStream cos =
                new PDPageContentStream(document, pageList.get(0));
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
        cos.moveTextPositionByAmount(leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        writeLine("Patient no.:", String.valueOf(patient.getPatientId()), cos);
        cos.endText();
        
        cos.beginText();
        cos.moveTextPositionByAmount(leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        writeLine("First name:", patient.getFirstName(), cos);
        cos.endText();
        
        cos.beginText();
        cos.moveTextPositionByAmount(leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        writeLine("Last name:", patient.getLastName(), cos);
        cos.endText();
        
        cos.beginText();
        cos.moveTextPositionByAmount(leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        writeLine("Date of birth:", patient.getBirthdate().toString(), cos);
        cos.endText();
        
        cos.beginText();
        cos.moveTextPositionByAmount(leftMargin,
                rect.getHeight() - patientLineSpace - lineSpace * ++line);
        writeLine("Sex:", String.valueOf(patient.getSex()), cos);
        cos.endText();
        int n = 0;
        for (CVDRiskData data : patient.getRiskData()) {
            if (n > 0 && n % 3 == 0) {
                cos.close();
                PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
                pageList.add(page);
                document.addPage(pageList.get(n/3));
                cos = new PDPageContentStream(document, pageList.get(n/3));
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
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            writeLine("Test ID:", String.valueOf(data.getTestId()), cos);
            cos.endText();
            
            cos.beginText();
            cos.setFont(fontCourier, 12);
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            writeLine("Test date:", data.getTestDate().toString(), cos);
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            writeLine("Cholesterol type:", data.getCholesterolType(), cos);
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            writeLine("Cholesterol mmol/L:",
                    String.format("%.2f", data.getCholesterolMmolL()), cos);
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            writeLine("HDL mmol/L:",
                    String.format("%.2f", data.getHdlMmolL()), cos);
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            writeLine("Diastolic blood pressure:",
                    String.valueOf(data.getBloodPressureDiastolicMmHg()), cos);
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            writeLine("Systolic blood pressure:", 
                    String.valueOf(data.getBloodPressureSystolicMmHg()), cos);
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            if (data.isSmoker()) {
                writeLine("Patient is smoker:", "Yes", cos);
            } else {
                writeLine("Patient is smoker:", "No", cos);
            }
            cos.endText();
            
            cos.beginText();
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            if (data.isDiabetic()) {
                writeLine("Patient is diabetic:", "Yes", cos);
            } else {
                writeLine("Patient is diabetic:", "No", cos);
            }
            cos.endText();
            
            int score = data.calculateRiskScore();
            cos.beginText();
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            writeLine("Risk score:", String.valueOf(score), cos);
            cos.endText();
            
            int riskPercentage = data.getRiskPercentage(score);
            cos.beginText();
            cos.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            writeLine("Risk percentage:",
                    new StringBuilder().append(riskPercentage).append(" %")
                            .toString() , cos);
            cos.endText();
            n++;
        }
        cos.close();
        return document;
    }
    
    public void savePatientDataToPdf(CVDPatient patient,
            String outputFileName) {
        try (PDDocument document = createPdfDocument(patient)) {
            document.save(outputFileName);
        } catch (IOException | COSVisitorException ex) {
            Logger.getLogger(
                    CVDPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printPatientData(CVDPatient patient) {
        try (PDDocument document = createPdfDocument(patient)) {
            document.print();
        } catch (PrinterException | IOException ex) {
            Logger.getLogger(
                    CVDPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
