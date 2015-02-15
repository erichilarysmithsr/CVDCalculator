/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamma.cvd.dbdecrypter;

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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Erling Austvoll
 */
public class DBDecrypter {
    
    public static void decryptDatabase(String encryptedFilename, String password,
            String decryptedFilename) throws FileNotFoundException,
            GeneralSecurityException, UnsupportedEncodingException,
            IOException {
        FileInputStream fis = new FileInputStream(encryptedFilename);
        AESCrypt aescrypt = new AESCrypt(password);
        FileOutputStream fos = new FileOutputStream(decryptedFilename);
        long inSize = fis.getChannel().size();
        aescrypt.decrypt(inSize, fis, fos);
    }
    
    public static void encryptDatabase(String decryptedFilename,
            String encryptedFilename, String password) throws IOException,
            SAXException, TransformerException, UnsupportedEncodingException,
            GeneralSecurityException {
        DOMParser parser = new DOMParser();
        parser.parse(decryptedFilename);
        Document doc = parser.getDocument();
        Transformer transformer =
                TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "4");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Source xmlSource = new DOMSource(doc);
        Result outputTarget = new StreamResult(outputStream);
        transformer.transform(xmlSource, outputTarget);
        InputStream is =
                new ByteArrayInputStream(outputStream.toByteArray());
        FileOutputStream fos = new FileOutputStream(encryptedFilename);
        AESCrypt aescrypt = new AESCrypt(password);
        aescrypt.encrypt(2, is, fos);
    }
}
