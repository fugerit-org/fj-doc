package org.fugerit.java.doc.json.typehandler;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class DocTypeHandlerCoreJSONUTF8 extends DocTypeHandlerDecorator {

    private static final long serialVersionUID = -8512951518109L;

    public static final DocTypeHandler HANDLER = new DocTypeHandlerCoreJSONUTF8();

    public DocTypeHandlerCoreJSONUTF8() {
        super( DocTypeHandlerCoreJSON.HANDLER_UTF8 );
    }

}
