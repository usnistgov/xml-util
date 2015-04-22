package nist.xml.util;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;

class XSDValidator {

    final static String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    /**
     * Validates the XML file against the XSD schema
     * and Returns the list of problems detected.
     * @param xml - The XML instance
     * @param xsd - The XSD schema
     * @return The list of errors detected
     * @throws SAXException
     * @throws IOException
     */
    public static List<String> validate(InputStream xml, InputStream xsd)
            throws SAXException, IOException {
        return validate(xml, xsd, null);
    }

    /**
     * Validates the XML file against the XSD schema
     * and Returns the list of problems detected.
     * @param xml - The XML instance
     * @param xsd - The XSD schema
     * @param lsr - The resource resolver
     * @return The list of errors detected
     * @throws SAXException
     * @throws IOException
     */
    public static List<String> validate(InputStream xml, InputStream xsd,
            LSResourceResolver lsr) throws SAXException, IOException {
        // Create the validator
        Validator validator = createValidator(xsd);
        // Set the resources resolver
        if( lsr != null )
            validator.setResourceResolver(lsr);
        System.out.println("################## LSR Null" + (lsr != null) );

        // Set the error handler
        MyErrorHandler errorHandler = new MyErrorHandler();
        validator.setErrorHandler(errorHandler);
        // Validate the instance
        StreamSource xmlFile = new StreamSource(xml);
        validator.validate(xmlFile);
        return errorHandler.getErrors();
    }

    /**
     * Creates and returns the schema validator
     * @param xsd - The XSD schema
     * @return The schema validator
     * @throws SAXException
     */
    private static Validator createValidator(InputStream xsd) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(schemaLanguage);
        Schema schema = factory.newSchema(new StreamSource(xsd));
        return schema.newValidator();
    }
}
