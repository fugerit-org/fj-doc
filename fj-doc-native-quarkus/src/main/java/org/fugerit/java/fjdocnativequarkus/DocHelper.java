package org.fugerit.java.fjdocnativequarkus;

import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

/**
 * DocHelper, version : auto generated on 2024-11-01 20:36:51.313
 */
public class DocHelper {

    private FreemarkerDocProcessConfig docProcessConfig = FreemarkerDocProcessConfigFacade
            .loadConfigSafe("cl://fj-doc-native-quarkus/fm-doc-process-config.xml");

    public FreemarkerDocProcessConfig getDocProcessConfig() {
        return this.docProcessConfig;
    }

}
