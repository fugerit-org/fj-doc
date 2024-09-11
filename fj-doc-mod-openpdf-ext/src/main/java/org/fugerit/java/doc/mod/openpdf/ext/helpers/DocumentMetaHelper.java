package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import com.lowagie.text.Document;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.UnsafeConsumer;
import org.fugerit.java.core.function.UnsafeVoid;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;

public class DocumentMetaHelper {

    private DocumentMetaHelper() {}

    public static final String CREATOR_DEFAULT = "OpenPDF over Fugerit Venus DOC";

    private static void metaWorker(String property, UnsafeConsumer<String, Exception> fun ) {
        SafeFunction.applyIfNotNull( property, () -> fun.accept( property ) );
    }

    public static void handleDocMeta(Document document, DocBase docBase) {
        // setup meta properties
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_TITLE),
                v -> document.addTitle( v ) );
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_AUTHOR),
                v -> document.addAuthor( v ) );
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_SUBJECT),
                v -> document.addSubject( v ) );
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_LANGUAGE),
                v -> document.setDocumentLanguage( v ) );
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_CREATOR, CREATOR_DEFAULT),
                v -> document.addCreator( v ) );

    }

}
