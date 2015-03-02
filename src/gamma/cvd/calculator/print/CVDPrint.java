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
    
    private final int leftMargin = 50;
    private final int lineSpace = 15;
    private final int initialLineSpace = 70;
    private final int patientLineSpace = 80;
    private final int testDataLineSpace = 95;
    private int line = 0;

    public CVDPrint() {
    }
    
    private void writeLine(String left, String right,
            PDPageContentStream contentStream, float space) throws IOException {
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(leftMargin,
                space - lineSpace * ++line);
        int lead = 54 - (left.length() + right.length());
        contentStream.drawString(left + " ");
        for (int i = 0; i < lead; i++) {
            contentStream.drawString(".");
        }
        contentStream.drawString(" " + right);
        contentStream.endText();
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
        PDPageContentStream contentStream =
                new PDPageContentStream(document, pageList.get(0));
        line = 0;
        contentStream.beginText();
        contentStream.setFont(fontHelveticaBold, 18);
        contentStream.moveTextPositionByAmount(leftMargin,
                rect.getHeight() - initialLineSpace);
        contentStream.drawString("Test record for " + patient.getFirstName() +
                " " + patient.getLastName());
        contentStream.endText();
        
        contentStream.setFont(fontCourier, 12);
        writeLine("Patient no.:", String.valueOf(patient.getPatientId()),
                contentStream, rect.getHeight() - patientLineSpace);
        
        writeLine("First name:", patient.getFirstName(), contentStream,
                rect.getHeight() - patientLineSpace);
        
        writeLine("Last name:", patient.getLastName(), contentStream,
                rect.getHeight() - patientLineSpace);
        
        writeLine("Date of birth:", patient.getBirthdate().toString(),
                contentStream, rect.getHeight() - patientLineSpace);
        
        writeLine("Sex:", String.valueOf(patient.getSex()), contentStream,
                rect.getHeight() - patientLineSpace);
        int n = 0;
        for (CVDRiskData data : patient.getRiskData()) {
            if (n > 0 && n % 3 == 0) {
                contentStream.close();
                PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
                pageList.add(page);
                document.addPage(pageList.get(n/3));
                contentStream = 
                        new PDPageContentStream(document, pageList.get(n/3));
                line = 0;
                contentStream.beginText();
                contentStream.setFont(fontHelveticaBold, 18);
                contentStream.moveTextPositionByAmount(leftMargin,
                        rect.getHeight() - initialLineSpace);
                contentStream.drawString("Test record for " +
                        patient.getFirstName() + " " + patient.getLastName() + 
                        ", page " + ((n/3) + 1));
                contentStream.endText();
            }
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(leftMargin,
                    rect.getHeight() - testDataLineSpace - lineSpace * ++line);
            contentStream.endText();
            
            contentStream.setFont(fontCourierBold, 12);
            writeLine("Test ID:", String.valueOf(data.getTestId()),
                    contentStream, rect.getHeight() - testDataLineSpace);
            
            contentStream.setFont(fontCourier, 12);
            writeLine("Test date:", data.getTestDate().toString(),
                    contentStream, rect.getHeight() - testDataLineSpace);
            
            writeLine("Cholesterol type:", data.getCholesterolType(),
                    contentStream, rect.getHeight() - testDataLineSpace);
            
            writeLine("Cholesterol mmol/L:",
                    String.format("%.2f", data.getCholesterolMmolL()),
                    contentStream, rect.getHeight() - testDataLineSpace);
            
            writeLine("HDL mmol/L:",
                    String.format("%.2f", data.getHdlMmolL()), contentStream,
                    rect.getHeight() - testDataLineSpace);
            
            writeLine("Diastolic blood pressure:",
                    String.valueOf(data.getBloodPressureDiastolicMmHg()),
                    contentStream, rect.getHeight() - testDataLineSpace);
            
            writeLine("Systolic blood pressure:", 
                    String.valueOf(data.getBloodPressureSystolicMmHg()),
                    contentStream, rect.getHeight() - testDataLineSpace);
            
            if (data.isSmoker()) {
                writeLine("Patient is smoker:", "Yes", contentStream,
                    rect.getHeight() - testDataLineSpace);
            } else {
                writeLine("Patient is smoker:", "No", contentStream,
                    rect.getHeight() - testDataLineSpace);
            }
            
            if (data.isDiabetic()) {
                writeLine("Patient is diabetic:", "Yes", contentStream,
                    rect.getHeight() - testDataLineSpace);
            } else {
                writeLine("Patient is diabetic:", "No", contentStream,
                    rect.getHeight() - testDataLineSpace);
            }
            
            int score = data.calculateRiskScore();
            writeLine("Risk score:", String.valueOf(score), contentStream,
                    rect.getHeight() - testDataLineSpace);
            
            int riskPercentage = data.getRiskPercentage(score);
            writeLine("Risk percentage:",
                    new StringBuilder().append(riskPercentage).append(" %")
                            .toString() , contentStream,
                    rect.getHeight() - testDataLineSpace);
            n++;
        }
        contentStream.close();
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
