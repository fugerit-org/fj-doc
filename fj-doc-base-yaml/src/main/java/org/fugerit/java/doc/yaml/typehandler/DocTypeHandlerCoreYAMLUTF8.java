package org.fugerit.java.doc.yaml.typehandler;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class DocTypeHandlerCoreYAMLUTF8 extends DocTypeHandlerDecorator {

    private static final long serialVersionUID = -8512962958109L;

    public static final DocTypeHandler HANDLER = new DocTypeHandlerCoreYAMLUTF8();

    public DocTypeHandlerCoreYAMLUTF8() {
        super( DocTypeHandlerCoreYAML.HANDLER_UTF8 );
    }

}
