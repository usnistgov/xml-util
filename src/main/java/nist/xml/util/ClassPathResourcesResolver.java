package nist.xml.util;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

/**
 * Resolve included/imported XSD schemas.
 *
 * Notes:
 *   1) The schemas are expected to be in the specified folder in the class path
 *   2) The schemaLocation should only contain the name of the file to be included
 *
 * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
 */

public class ClassPathResourcesResolver implements LSResourceResolver {

    private String folder;

    public ClassPathResourcesResolver(String folder) {
        this.folder = folder;
        System.out.println("################## " + folder);

    }

    /**
     * Resolve a reference to a resource
     * @param type      The type of resource, for example a schema, source XML document, or query
     * @param namespace The target namespace (in the case of a schema document)
     * @param publicId  The public ID
     * @param systemId  The system identifier (as written, possibly a relative URI)
     * @param baseURI   The base URI against which the system identifier should be resolved
     * @return an LSInput object typically containing the character stream or byte stream identified
     * by the supplied parameters; or null if the reference cannot be resolved or if the resolver chooses
     * not to resolve it.
     */
    public LSInput resolveResource(String type, String namespace, String publicId,
                                   String systemId, String baseURI) {
        System.out.println("################## " + systemId + " # " + publicId);
        String path = folder + File.separator + systemId;
        InputStream is = getClass().getResourceAsStream( path );
        return new Input(publicId, systemId, is);
    }

    class Input implements LSInput {

        String publicId;
        String systemId;
        InputStream input;

        public Input(String publicId, String systemId, InputStream input) {
            this.publicId = publicId;
            this.systemId = systemId;
            this.input = input;
            System.out.println(">>>>>>>>>>>> " + systemId + " # " + publicId);
        }

        @Override
        public Reader getCharacterStream() {  return new InputStreamReader(input); }

        @Override
        public void setCharacterStream(Reader reader) { }

        @Override
        public InputStream getByteStream() { return null; }

        @Override
        public void setByteStream(InputStream inputStream) { }

        @Override
        public String getStringData() {
            Scanner s = new Scanner(input);
            return s.hasNext() ? s.useDelimiter("\\A").next() : "";
        }

        @Override
        public void setStringData(String s) { }

        @Override
        public String getSystemId() { return this.systemId; }

        @Override
        public void setSystemId(String s) { }

        @Override
        public String getPublicId() { return this.publicId; }

        @Override
        public void setPublicId(String s) {}

        @Override
        public String getBaseURI() { return null; }

        @Override
        public void setBaseURI(String s) {}

        @Override
        public String getEncoding() { return null; }

        @Override
        public void setEncoding(String s) {}

        @Override
        public boolean getCertifiedText() { return false; }

        @Override
        public void setCertifiedText(boolean b) {}
    }
}
