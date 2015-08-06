package ssmm.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DocumentBuilderTest {

    @Test
    public void buildNoValidation() {
        try {
            InputStream xsd = getClass().getResourceAsStream("/xsd/Person.xsd");
            InputStream xml = getClass().getResourceAsStream("/xsd/People.xml");
            LSResourceResolver lsr = new LocalLSResourceResolver("/xsd");
            Document d = DocumentBuilder.build(xml, xsd, lsr);
            assertNotNull(d);
        } catch (Exception e) {
            fail( e.getMessage() );
        }
    }
}
