package test.org.fugerit.java.doc.freemarker.issue;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class TestIssue583POC {

    @Test
    void testIssue583() throws IOException {
        String xmlPath = "issue/583-some-characters-not-rendered-properly/issue-583-poc.xml";
        DocTypeHandler handler = FreeMarkerHtmlTypeHandler.HANDLER_UTF8;
        String type = DocConfig.TYPE_HTML;
        File outputFile = new File( "target/issue-583-some-characters-not-rendered-properly."+type );
        SafeFunction.apply( () -> {
            try (InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( xmlPath ) );
                 FileOutputStream fos = new FileOutputStream( outputFile ) ) {
                handler.handle( DocInput.newInput( handler.getType() , reader ) ,  DocOutput.newOutput( fos ) );
                fos.flush();
            }
        } );
        Assertions.assertTrue( outputFile.exists() );
        String content = FileIO.readString( outputFile );
        Assertions.assertTrue( content.contains( "✅" ) );
    }

}
