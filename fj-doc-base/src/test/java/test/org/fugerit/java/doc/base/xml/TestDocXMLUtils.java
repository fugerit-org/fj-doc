package test.org.fugerit.java.doc.base.xml;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.xml.DocXMLUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class TestDocXMLUtils {

    @Test
    public void cleanXml() throws IOException, DocException {
        try ( InputStream stream = ClassHelper.loadFromDefaultClassLoader( "sample/default_doc_toclean.xml" ) ) {
            String text = StreamIO.readString( stream );
            try (StringReader reader = new StringReader( text ) ) {
                Assert.assertThrows(  DocException.class, () -> DocFacadeSource.getInstance().parse( reader, DocFacadeSource.SOURCE_TYPE_XML ) );
            }
            try (StringReader reader = new StringReader(DocXMLUtils.cleanXML( text )) ) {
                DocBase docBase = DocFacadeSource.getInstance().parse( reader, DocFacadeSource.SOURCE_TYPE_XML );
                Assert.assertNotNull( docBase );
            }
        }
    }

}
