/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamma.cvd.login;

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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
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
public class CVDLogin {
    
    private Document userDb;
    private final String USERDBPATH = "UserDB.aes";
    private final String USERDBPASSWORD = "|nTfQP1X.%b<QgyE";

    public CVDLogin() throws SAXException, IOException,
            XPathExpressionException, GeneralSecurityException {
        loadEncryptedUserDb();
    }
    
    private void loadEncryptedUserDb() throws GeneralSecurityException,
            IOException, SAXException, XPathExpressionException {
        FileInputStream fis = new FileInputStream(USERDBPATH);
        AESCrypt aescrypt = new AESCrypt(USERDBPASSWORD);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long inSize = fis.getChannel().size();
        aescrypt.decrypt(inSize, fis, baos);
        DOMParser parser = new DOMParser();
        parser.parse(
                new InputSource(new ByteArrayInputStream(baos.toByteArray())));
        this.userDb = parser.getDocument();
        // The following code gets rid of extra whitespace from the xml file.
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nl = (NodeList)xPath.evaluate(
                "//text()[normalize-space(.)='']",
                this.userDb, XPathConstants.NODESET);
        for (int i=0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            node.getParentNode().removeChild(node);
        }
    }
    
    public boolean validateUsernameAndPassword(String username,
            String password) throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Element passwordElement = (Element)xPath.evaluate(
                "//User[@username='" + username + "']/PasswordHash",
                this.userDb, XPathConstants.NODE);
        if (passwordElement != null) {
            return PasswordHash.validatePassword(password,
                    passwordElement.getTextContent());
        } else {
            return false;
        }
    }
    
    public boolean createNewUser(String username, String password, String title,
            String firstname, String lastname, String emailAddress)
            throws NoSuchAlgorithmException, InvalidKeySpecException,
            TransformerConfigurationException, TransformerException,
            XPathExpressionException, FileNotFoundException,
            GeneralSecurityException, UnsupportedEncodingException,
            IOException {
        
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node node = (Node)xPath.evaluate(
                "//User[@username='" + username + "']",
                this.userDb, XPathConstants.NODE);
        if (node != null) {
            return false;
        } else {
           Element rootElement = this.userDb.getDocumentElement();
            Element userElement = this.userDb.createElement("User");
            userElement.setAttribute("username", username);
            rootElement.appendChild(userElement);

            Element passwordElement = this.userDb.createElement("PasswordHash");
            passwordElement.appendChild(
                    this.userDb.createTextNode(
                            PasswordHash.createHash(password)));
            userElement.appendChild(passwordElement);

            Element titleElement = this.userDb.createElement("Title");
            titleElement.appendChild(this.userDb.createTextNode(title));
            userElement.appendChild(titleElement);

            Element firstnameElement = this.userDb.createElement("FirstName");
            firstnameElement.appendChild(this.userDb.createTextNode(firstname));
            userElement.appendChild(firstnameElement);

            Element lastnameElement = this.userDb.createElement("FirstName");
            lastnameElement.appendChild(this.userDb.createTextNode(lastname));
            userElement.appendChild(lastnameElement);

            Element emailElement = this.userDb.createElement("EmailAddress");
            emailElement.appendChild(this.userDb.createTextNode(emailAddress));
            userElement.appendChild(emailElement);

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(this.userDb);
            Result outputTarget = new StreamResult(outputStream);
            transformer.transform(xmlSource, outputTarget);
            InputStream is =
                    new ByteArrayInputStream(outputStream.toByteArray());
            FileOutputStream fos = new FileOutputStream(USERDBPATH);
            AESCrypt aescrypt = new AESCrypt(USERDBPASSWORD);
            aescrypt.encrypt(2, is, fos);
            return true; 
        }
    }
    
    public boolean deleteUser(String username)
            throws TransformerConfigurationException, TransformerException,
            XPathExpressionException, FileNotFoundException,
            GeneralSecurityException, UnsupportedEncodingException,
            IOException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node node = (Node)xPath.evaluate(
                "//User[@username='" + username + "']",
                this.userDb, XPathConstants.NODE);
        if (node != null) {
            node.getParentNode().removeChild(node);
            
            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(this.userDb);
            Result outputTarget = new StreamResult(outputStream);
            transformer.transform(xmlSource, outputTarget);
            InputStream is =
                    new ByteArrayInputStream(outputStream.toByteArray());
            FileOutputStream fos = new FileOutputStream(USERDBPATH);
            AESCrypt aescrypt = new AESCrypt(USERDBPASSWORD);
            aescrypt.encrypt(2, is, fos);
            return true;
        } else {
            return false;
        }
    }
}
