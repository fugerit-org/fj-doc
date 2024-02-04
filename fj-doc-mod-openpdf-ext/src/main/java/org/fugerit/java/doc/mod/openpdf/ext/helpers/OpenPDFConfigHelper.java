package org.fugerit.java.doc.mod.openpdf.ext.helpers;

import org.fugerit.java.core.cfg.ConfigException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenPDFConfigHelper {

    private OpenPDFConfigHelper() {}

    public static final String TAG_NAME_FONT = "font";

    public static final String ATT_NAME = "name";
    public static final String ATT_PATH = "path";


    public static void handleConfig( Element config, String type ) throws ConfigException {
        log.info( "configure for type: {}", type );
        NodeList fontList = config.getElementsByTagName( TAG_NAME_FONT );
        for ( int k=0; k<fontList.getLength(); k++ ) {
            Element currentFontTag = (Element) fontList.item( k );
            String name = currentFontTag.getAttribute( ATT_NAME );
            String path = currentFontTag.getAttribute( ATT_PATH );
            log.info( "current font {} - {}", name, path );
            ConfigException.apply( () -> OpenPpfDocHandler.registerFont( name , path ) );
        }
    }

}
