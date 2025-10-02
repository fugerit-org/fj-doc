package org.fugerit.java.doc.yaml.typehandler;

import org.fugerit.java.doc.base.config.*;
import org.fugerit.java.doc.base.facade.DocFacadeSource;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DocTypeHandlerCoreYAML extends DocTypeHandlerDefault {

    /**
     *
     */
    private static final long serialVersionUID = -50249858785381015L;

    public static final DocTypeHandler HANDLER = new DocTypeHandlerCoreYAML();

    public static final DocTypeHandler HANDLER_UTF8 = new DocTypeHandlerCoreYAML( StandardCharsets.UTF_8 );

    public static final String TYPE = DocConfig.TYPE_YAML;

    public static final String MODULE = "doc-core";

    public DocTypeHandlerCoreYAML(Charset charset ) {
        super( TYPE, MODULE, null, charset );
    }

    public DocTypeHandlerCoreYAML() {
        super( TYPE, MODULE );
    }

    @Override
    public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
        DocFacadeSource.getInstance().convert( docInput.getReader(),
                docInput.getSource(), new OutputStreamWriter( docOutput.getOs() ), DocFacadeSource.SOURCE_TYPE_YAML );
    }

}
