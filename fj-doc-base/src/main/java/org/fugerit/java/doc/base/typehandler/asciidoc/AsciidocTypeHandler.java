package org.fugerit.java.doc.base.typehandler.asciidoc;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class AsciidocTypeHandler extends DocTypeHandlerDefault {

    private static final long serialVersionUID = -397451608L;

    public static final String TYPE = DocConfig.TYPE_ADOC;

    public static final String MODULE = "asciidoc";

    public static final String MIME = "text/asciidoc";

    public AsciidocTypeHandler() {
        this( StandardCharsets.UTF_8 );
    }

    public AsciidocTypeHandler(Charset charset) {
        super( TYPE, MODULE, MIME, charset );
    }

    @Override
    public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
        PrintWriter writer = new PrintWriter( new OutputStreamWriter( docOutput.getOs(), this.getCharset() ) );
        DocBase docBase = docInput.getDoc();
        AsciidocFacade facade = new AsciidocFacade( writer );
        facade.handleDoc( docBase );
    }

}
