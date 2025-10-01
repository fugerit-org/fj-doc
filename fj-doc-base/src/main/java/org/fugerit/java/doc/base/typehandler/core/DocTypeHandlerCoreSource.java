package org.fugerit.java.doc.base.typehandler.core;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.base.config.*;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DocTypeHandlerCoreSource extends DocTypeHandlerDefault {

    /**
     *
     */
    private static final long serialVersionUID = -50249858785381015L;

    public static final DocTypeHandler HANDLER = new DocTypeHandlerCoreSource();

    public static final DocTypeHandler HANDLER_UTF8 = new DocTypeHandlerCoreSource( StandardCharsets.UTF_8 );

    public static final String TYPE = DocConfig.TYPE_TXT;

    public static final String MODULE = "doc-core";

    public DocTypeHandlerCoreSource(Charset charset ) {
        super( TYPE, MODULE, null, charset );
    }

    public DocTypeHandlerCoreSource() {
        super( TYPE, MODULE );
    }

    @Override
    public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
        StreamIO.pipeCharCloseBoth( docInput.getReader() , new OutputStreamWriter( docOutput.getOs(), this.getCharset() ) );
    }

}
