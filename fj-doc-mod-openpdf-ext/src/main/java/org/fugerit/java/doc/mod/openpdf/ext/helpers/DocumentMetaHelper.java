package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import com.lowagie.text.Document;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.UnsafeConsumer;
import org.fugerit.java.core.util.mvn.MavenProps;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.VenusVersion;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;

public class DocumentMetaHelper {

    private DocumentMetaHelper() {}

    private static String getModuleVersion() {
        return VenusVersion.getFjDocModuleVersionS( "fj-doc-mod-openpdf-ext");
    }

    private static String getOpenPDFVersion() {
        return MavenProps.getProperty( "com.github.librepdf", "openpdf", MavenProps.VERSION );
    }

    private static final String PRODUCER_DEFAULT = String.format( VenusVersion.VENUS_PRODUCER_FORMAT, DocConfig.FUGERIT_VENUS_DOC , getModuleVersion() , Document.getProduct(), getOpenPDFVersion() );

    private static void metaWorker(String property, UnsafeConsumer<String, Exception> fun ) {
        SafeFunction.applyIfNotNull( property, () -> fun.accept( property ) );
    }

    public static void handleDocMeta(Document document, DocBase docBase) {
        // setup meta properties
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_TITLE),
                document::addTitle );
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_AUTHOR),
                document::addAuthor );
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_SUBJECT),
                document::addSubject );
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_LANGUAGE),
                document::setDocumentLanguage );
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_CREATOR, VenusVersion.VENUS_CREATOR),
                document::addCreator );
        metaWorker(
                docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_PRODUCER, PRODUCER_DEFAULT),
                document::addProducer );
    }

}
