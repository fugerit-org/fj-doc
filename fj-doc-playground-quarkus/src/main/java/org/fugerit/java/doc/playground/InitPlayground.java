package org.fugerit.java.doc.playground;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.InitHandler;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class InitPlayground {

    public static final String OUTPUT_FORMAT_PDF_A = "pdfa";

    public static final String OUTPUT_FORMAT_PDF_UA = "pdfua";

    private static final FreemarkerDocProcessConfig PROCESS_CONFIG = FreemarkerDocProcessConfigFacade
            .loadConfigSafe("cl://playground-config/fm-playground-doc-process.xml");

    public static final DocTypeHandler PDF_FOP_TYPE_HANDLER = PROCESS_CONFIG.getFacade().findHandler("pdf-fop");

    public static final DocTypeHandler PDFA_FOP_TYPE_HANDLER = PROCESS_CONFIG.getFacade().findHandler("PDF/A-1a");

    public static final DocTypeHandler PDFUA_FOP_TYPE_HANDLER = PROCESS_CONFIG.getFacade().findHandler("PDF/UA-1");

    void onStart(@Observes StartupEvent ev) {
        log.info("InitPlayground start {}", ev);
        InitHandler.initDocAsync(PDF_FOP_TYPE_HANDLER);
        log.info("InitPlayground PDF_FOP_TYPE_HANDLER  -> {}", PDF_FOP_TYPE_HANDLER);
        InitHandler.initDocAsync(PDFA_FOP_TYPE_HANDLER);
        log.info("InitPlayground PDFA_FOP_TYPE_HANDLER -> {}", PDFA_FOP_TYPE_HANDLER);
        InitHandler.initDocAsync(PDFUA_FOP_TYPE_HANDLER);
        log.info("InitPlayground PDFUA_FOP_TYPE_HANDLER -> {}", PDFUA_FOP_TYPE_HANDLER);
        log.info("InitPlayground end");
    }

    public static DocTypeHandler findHandler(String id) {
        return PROCESS_CONFIG.getFacade().findHandler(id);
    }

}
