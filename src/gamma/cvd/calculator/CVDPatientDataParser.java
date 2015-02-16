/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamma.cvd.calculator;

import es.vocali.util.AESCrypt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Erling Austvoll
 */
public class CVDPatientDataParser {
    
    private Document patientDb;
    private List<CVDPatient> patientList;
    private final String PATIENTDBPATH = "PatientDB.aes";
    private final String PATIENTDBPASSWORD = "L7>02v9459{0sVHc";

    public CVDPatientDataParser() throws SAXException, IOException,
            GeneralSecurityException, XPathExpressionException {
        loadEncryptedPatientDb();
    }
    
    private void loadEncryptedPatientDb() throws SAXException, IOException,
            GeneralSecurityException, XPathExpressionException {
        FileInputStream fis = new FileInputStream(PATIENTDBPATH);
        AESCrypt aescrypt = new AESCrypt(PATIENTDBPASSWORD);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long inSize = fis.getChannel().size();
        aescrypt.decrypt(inSize, fis, baos);
        DOMParser parser = new DOMParser();
        parser.parse(
                new InputSource(new ByteArrayInputStream(baos.toByteArray())));
        this.patientDb = parser.getDocument();
        // The following code gets rid of extra whitespace from the xml file.
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nl = (NodeList)xPath.evaluate(
                "//text()[normalize-space(.)='']",
                this.patientDb, XPathConstants.NODESET);
        for (int i=0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            node.getParentNode().removeChild(node);
        }
        this.patientList = new ArrayList<>();
        NodeList patientNodeList = (NodeList)xPath.evaluate("//Patient",
                this.patientDb, XPathConstants.NODESET);
        for (int i = 0; i < patientNodeList.getLength(); i++) {
            if (patientNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element patientEl = (Element)patientNodeList.item(i);
                List<CVDRiskData> testData = new ArrayList<>();
                String firstname = (String)xPath.evaluate("FirstName",
                        patientEl, XPathConstants.STRING);
                String lastname = (String)xPath.evaluate("LastName",
                        patientEl, XPathConstants.STRING);
                LocalDate birthdate = LocalDate.parse((String)xPath.evaluate(
                        "Birthdate", patientEl, XPathConstants.STRING));
                char sex = ((String)xPath.evaluate("Sex", patientEl,
                        XPathConstants.STRING)).charAt(0);
                int patientId = Integer.parseInt((String)xPath.evaluate("@id",
                        patientEl, XPathConstants.STRING));
                NodeList testNodeList = (NodeList)xPath.evaluate("TestData",
                        patientEl, XPathConstants.NODESET);
                for (int j = 0; j < testNodeList.getLength(); j++) {
                    Element testEl = (Element)testNodeList.item(j);
                    int testId = Integer.parseInt((String)xPath.evaluate(
                            "@testId", testEl, XPathConstants.STRING));
                    LocalDate testDate = LocalDate.parse((String)xPath.evaluate(
                        "Date", testEl, XPathConstants.STRING));
                    int age = Period.between(birthdate, testDate).getYears();
                    String cholesterolType = null;
                    if (((String)xPath.evaluate("CholesterolType", testEl,
                            XPathConstants.STRING))
                            .equalsIgnoreCase(CVDRiskData.LDL)) {
                        cholesterolType = CVDRiskData.LDL;
                    } else if (((String)xPath.evaluate("CholesterolType",
                            testEl, XPathConstants.STRING)).
                            equalsIgnoreCase(CVDRiskData.CHOL)) {
                        cholesterolType = CVDRiskData.CHOL;
                    }
                    float cholesterolMmolL = (float)(double)xPath.evaluate(
                            "CholesterolValue", testEl,
                            XPathConstants.NUMBER);
                    float hdlMmolL = (float)(double)xPath.evaluate(
                            "HDLValue", testEl, XPathConstants.NUMBER);
                    int bloodPressureSystolicMmHg = (int)(double)xPath.evaluate(
                            "SystolicBloodPressure", testEl,
                            XPathConstants.NUMBER);
                    int bloodPressureDiastolicMmHg =
                            (int)(double)xPath.evaluate(
                                    "DiastolicBloodPressure", testEl,
                                    XPathConstants.NUMBER);
                    boolean isDiabetic = Boolean.parseBoolean(
                            ((String)xPath.evaluate("Diabetes", testEl,
                                    XPathConstants.STRING)));
                    boolean isSmoker = Boolean.parseBoolean(
                            ((String)xPath.evaluate("Smoker", testEl,
                                    XPathConstants.STRING)));
                    if (sex != 0 && age > -1 && cholesterolType != null
                            && cholesterolMmolL > 0 && hdlMmolL > 0
                            && bloodPressureSystolicMmHg > 0
                            && bloodPressureDiastolicMmHg > 0
                            && testDate != null) {
                        CVDRiskData test = new CVDRiskData(sex, age,
                                cholesterolType, cholesterolMmolL,
                                hdlMmolL, bloodPressureSystolicMmHg,
                                bloodPressureDiastolicMmHg,
                                isDiabetic, isSmoker, testDate);
                        test.setTestId(testId);
                        testData.add(test);
                    }
                }
                if (firstname != null && lastname != null && birthdate != null
                        && (sex == CVDPatient.MALE || sex == CVDPatient.FEMALE)
                        && patientId > 0) {
                    CVDPatient patient =
                            new CVDPatient(firstname, lastname, birthdate, sex,
                                    patientId);
                    if (testData.size() > 0) {
                        patient.setRiskData(testData);
                    }
                    patientList.add(patient);
                }
            }
        }
    }
    
    private void writeEncryptedPatientDb()
            throws FileNotFoundException, TransformerException,
            GeneralSecurityException, UnsupportedEncodingException,
            IOException {
        Transformer transformer =
                TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "4");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Source xmlSource = new DOMSource(this.patientDb);
        Result outputTarget = new StreamResult(outputStream);
        transformer.transform(xmlSource, outputTarget);
        InputStream is =
                new ByteArrayInputStream(outputStream.toByteArray());
        FileOutputStream fos = new FileOutputStream(PATIENTDBPATH);
        AESCrypt aescrypt = new AESCrypt(PATIENTDBPASSWORD);
        aescrypt.encrypt(2, is, fos);
    }
    
    public List<CVDPatient> getPatientList() {
        return Collections.unmodifiableList(this.patientList);
    }
    
    public boolean addNewPatient(String firstName, String lastName,
            LocalDate birthdate, char sex)
            throws FileNotFoundException, TransformerException,
            GeneralSecurityException, IOException {
        int patientId = 1;
        for (CVDPatient patient : this.patientList) {
            if (patient.getPatientId() >= patientId) {
                patientId = patient.getPatientId() + 1;
            }
        }
        CVDPatient newPatient = new CVDPatient(
                firstName, lastName, birthdate, sex, patientId);
        if (this.patientList.add(newPatient)) {
            Element rootElement = this.patientDb.getDocumentElement();
            Element patientElement = this.patientDb.createElement("Patient");
            patientElement.setAttribute("id",
                    String.valueOf(patientId));
            rootElement.appendChild(patientElement);
            
            Element firstNameElement =
                    this.patientDb.createElement("FirstName");
            firstNameElement.appendChild(this.patientDb.createTextNode(
                    firstName));
            patientElement.appendChild(firstNameElement);
            
            Element lastNameElement = this.patientDb.createElement("LastName");
            lastNameElement.appendChild(this.patientDb.createTextNode(
                    lastName));
            patientElement.appendChild(lastNameElement);
            
            Element birthdateElement =
                    this.patientDb.createElement("Birthdate");
            birthdateElement.appendChild(this.patientDb.createTextNode(
                    birthdate.toString()));
            patientElement.appendChild(birthdateElement);
            
            Element sexElement = this.patientDb.createElement("Sex");
            sexElement.appendChild(this.patientDb.createTextNode(
                    String.valueOf(sex)));
            patientElement.appendChild(sexElement);
            writeEncryptedPatientDb();
            return true;
        } else {
            return false;
        }
    }
    
    public boolean removePatient(CVDPatient patient)
            throws XPathExpressionException, FileNotFoundException,
            TransformerException, GeneralSecurityException, IOException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node node = (Node)xPath.evaluate(
                "//Patient[@id='" + String.valueOf(patient.getPatientId()) +
                        "']", this.patientDb, XPathConstants.NODE);
        if (node != null && this.patientList.remove(patient)) {
            node.getParentNode().removeChild(node);
            writeEncryptedPatientDb();
            return true;
        }
        return false;
    }
    
    public boolean addRiskDataToPatient(CVDPatient patient,
            CVDRiskData data) throws XPathExpressionException,
            FileNotFoundException, TransformerException,
            GeneralSecurityException, IOException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node patientNode = (Node)xPath.evaluate(
                "//Patient[@id='" + String.valueOf(patient.getPatientId()) +
                        "']", this.patientDb, XPathConstants.NODE);
        if (patientNode != null) {
            if (data.getSex() != patient.getSex()) {
                data.setSex(patient.getSex());
            }
            data.setAge(Period.between(
                    patient.getBirthdate(), data.getTestDate()).getYears());
            int testId = 1;
            for (CVDRiskData dataEntry : patient.getRiskData()) {
                if (dataEntry.getTestId() >= testId) {
                    testId = dataEntry.getTestId() + 1;
                }
            }
            data.setTestId(testId);
            if (patient.addToRiskData(data)) {
                Element testDataElement =
                        this.patientDb.createElement("TestData");
                testDataElement.setAttribute("testId",
                        String.valueOf(data.getTestId()));
                patientNode.appendChild(testDataElement);

                Element dateElement = this.patientDb.createElement("Date");
                dateElement.appendChild(this.patientDb.createTextNode(
                        data.getTestDate().toString()));
                testDataElement.appendChild(dateElement);

                Element cholesterolTypeElement = this.patientDb
                        .createElement("CholesterolType");
                cholesterolTypeElement.appendChild(this.patientDb
                        .createTextNode(data.getCholesterolType()));
                testDataElement.appendChild(cholesterolTypeElement);

                Element cholesterolValueElement = this.patientDb
                        .createElement("CholesterolValue");
                cholesterolValueElement.appendChild(this.patientDb
                        .createTextNode(String.valueOf(
                                data.getCholesterolMmolL())));
                testDataElement.appendChild(cholesterolValueElement);

                Element hdlValueElement = this.patientDb
                        .createElement("HDLValue");
                hdlValueElement.appendChild(this.patientDb.createTextNode(
                        String.valueOf(data.getHdlMmolL())));
                testDataElement.appendChild(hdlValueElement);

                Element diastolicBloodPressureElement = this.patientDb
                        .createElement("DiastolicBloodPressure");
                diastolicBloodPressureElement.appendChild(this.patientDb
                        .createTextNode(String.valueOf(
                                data.getBloodPressureDiastolicMmHg())));
                testDataElement.appendChild(diastolicBloodPressureElement);

                Element systolicBloodPressureElement = this.patientDb
                        .createElement("SystolicBloodPressure");
                systolicBloodPressureElement.appendChild(this.patientDb
                        .createTextNode(String.valueOf(
                                data.getBloodPressureSystolicMmHg())));
                testDataElement.appendChild(systolicBloodPressureElement);

                Element diabetesElement = this.patientDb
                        .createElement("Diabetes");
                diabetesElement.appendChild(this.patientDb.createTextNode(
                        String.valueOf(data.isDiabetic())));
                testDataElement.appendChild(diabetesElement);

                Element smokerElement = this.patientDb.createElement("Smoker");
                smokerElement.appendChild(this.patientDb.createTextNode(
                        String.valueOf(data.isSmoker())));
                testDataElement.appendChild(smokerElement);

                writeEncryptedPatientDb();
                return true;
            }
        }
        return false;
    }
    
    public boolean removeRiskDataFromPatient(CVDPatient patient,
            CVDRiskData data) throws XPathExpressionException,
            FileNotFoundException, TransformerException,
            GeneralSecurityException, IOException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node patientNode = (Node)xPath.evaluate(
                "//Patient[@id='" + String.valueOf(patient.getPatientId()) +
                        "']", this.patientDb, XPathConstants.NODE);
        Node testDataNode = (Node)xPath.evaluate(
                "TestData[@testId='" + String.valueOf(data.getTestId()) +
                        "']", patientNode, XPathConstants.NODE);
        if (testDataNode != null && patient.removeFromRiskData(data)) {
            testDataNode.getParentNode().removeChild(testDataNode);
            writeEncryptedPatientDb();
            return true;
        }
        return false;
    }
    
    public boolean changePatientName(CVDPatient patient, String firstName,
            String lastName) throws XPathExpressionException,
            FileNotFoundException, TransformerException,
            GeneralSecurityException, IOException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node patientNode = (Node)xPath.evaluate(
                "//Patient[@id='" + String.valueOf(patient.getPatientId()) +
                        "']", this.patientDb, XPathConstants.NODE);
        if (patientNode != null &&
                patient.setPatientName(firstName, lastName)) {
            Node firstNameNode = (Node)xPath.evaluate(
                "FirstName", patientNode, XPathConstants.NODE);
            firstNameNode.setTextContent(firstName);
            Node lastNameNode = (Node)xPath.evaluate(
                "LastName", patientNode, XPathConstants.NODE);
            lastNameNode.setTextContent(lastName);
            writeEncryptedPatientDb();
            return true;
        }
        return false;
    }
}
