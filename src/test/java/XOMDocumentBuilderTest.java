import nist.xml.util.XOMDocumentBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mcl1 on 12/1/15.
 */
public class XOMDocumentBuilderTest {

    @Test
    public void loadFileTest(){
        File xml = new File("file.xml");
        File xsd = new File("file.xsd");
        try {
            InputStream isXml = FileUtils.openInputStream(xml);
            InputStream isXsd = FileUtils.openInputStream(xsd);
            XOMDocumentBuilder.build(isXml,isXsd,null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
