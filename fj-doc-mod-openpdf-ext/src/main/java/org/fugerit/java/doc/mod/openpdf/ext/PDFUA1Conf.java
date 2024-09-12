package org.fugerit.java.doc.mod.openpdf.ext;

public interface PDFUA1Conf {

    void setActivatePDFUA1( boolean value );

    default boolean isActivatePDFUA1() {
        return false;
    }

}
