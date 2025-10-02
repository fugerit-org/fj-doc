package org.fugerit.java.doc.base.typehandler.core;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class DocTypeHandlerCoreXMLUTF8 extends DocTypeHandlerDecorator {

    private static final long serialVersionUID = -8512962951L;

    public static final DocTypeHandler HANDLER = new DocTypeHandlerCoreXMLUTF8();

    public DocTypeHandlerCoreXMLUTF8() {
        super( DocTypeHandlerCoreXML.HANDLER_UTF8 );
    }

}
