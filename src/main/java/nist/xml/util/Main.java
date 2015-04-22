package nist.xml.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;

public class Main {

    public static void main(String[] a) {
        try {
//            InputStream xsd = Main.class.getResourceAsStream("/Profile.xsd");
//            InputStream xml = Main.class.getResourceAsStream("/Profile.xml");
            /*InputStream xsd = Main.class.getResourceAsStream("/rules/ConformanceContext.xsd");
            InputStream xml = Main.class.getResourceAsStream("/rules/ConfContextSample.xml");
            LSResourceResolver lsr = new ClassPathResourcesResolver("/rules");
            for(String error: XSDValidator.validate(xml, xsd, lsr))
                System.out.println(">>>> " +error);*/

            vvv();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void vvv() {
        try {
            InputStream xsd = Main.class.getResourceAsStream("/xsd/Person.xsd");
            InputStream xml = Main.class.getResourceAsStream("/xsd/People.xml");
            Document d = nist.xml.util.DocumentBuilder.build(xml, xsd, new LocalLSResourceResolver("/xsd"));
            printNode(d, "");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void vv() {
        try {

            InputStream xsd = Main.class.getResourceAsStream("/xsd/Person.xsd");
            InputStream xml = Main.class.getResourceAsStream("/xsd/People.xml");

            // create a SchemaFactory capable of understanding WXS schemas
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setResourceResolver( new LocalLSResourceResolver("/rules") );

            // load a WXS schema, represented by a Schema instance
            Source schemaFile = new StreamSource( xsd );
            Schema schema = factory.newSchema(schemaFile);

            // create a Validator instance, which can be used to validate an instance document
            Validator validator = schema.newValidator();

            // parse an XML document into a DOM tree
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document document = builder.parse( xml );

            // validate the DOM tree
            DOMSource source = new DOMSource(document);
            DOMResult result = new DOMResult(document);
            validator.validate(new DOMSource(document), result);
            printNode(result.getNode(), "");
        } catch (Exception e) {
            System.out.println("Document is invalid !!!");
            e.printStackTrace();
        }
    }

    private static void printNode(Node n, String tab) {

        if( "Person".equals(n.getNodeName()) ) {
            String name = n.getLocalName();
            String fn = n.getAttributes().getNamedItem("FullName").getNodeValue();
            String role = n.getAttributes().getNamedItem("Role").getNodeValue(); /*!= null
                    ? n.getAttributes().getNamedItem("Role").getNodeValue()
                    : "NULL";*/

            System.out.println(tab + "Name: " + name + " @FN:" + fn + " @Role:" + role);
            for (int i = 0; i < n.getChildNodes().getLength(); i++) {
                Node nn = n.getChildNodes().item(i);
                printNode(nn, tab + "\t");
            }
        } else {
            System.out.println(tab + "Name: " + n.getNodeName() + " Type:" + n.getNodeType());
            for (int i = 0; i < n.getChildNodes().getLength(); i++) {
                Node nn = n.getChildNodes().item(i);
                printNode(nn, tab + "\t");
            }
        }
    }
}
