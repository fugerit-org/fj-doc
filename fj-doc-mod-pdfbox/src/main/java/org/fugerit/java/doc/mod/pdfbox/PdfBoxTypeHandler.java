package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.mvn.MavenProps;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.config.VenusVersion;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.fugerit.java.doc.base.typehelper.generic.SecurityHardeningConsts;
import org.fugerit.java.doc.base.typehelper.generic.SecurityHardeningUtil;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PdfBoxTypeHandler extends DocTypeHandlerDefault {

    private static final String MODULE = "pdfbox";

    private transient PdfBoxConfig config;

    public PdfBoxTypeHandler() {
        super(DocConfig.TYPE_PDF, MODULE);
        this.config = new PdfBoxConfig();
    }

    @Override
    public void configure(Properties config) throws ConfigException {
        if (config != null) {
            this.config = PdfBoxConfig.fromProperties(config);
        }
    }

    private static String getModuleVersion() {
        return VenusVersion.getFjDocCoreVersionS();
    }

    private static String getPdfBoxVersion() {
        return StringUtils.valueWithDefault(MavenProps.getProperty("org.apache.pdfbox", "pdfbox", MavenProps.VERSION), "3.x");
    }

    private static final String PRODUCER_OVER = "PDFBox";

    private static final String PRODUCER_DEFAULT = String.format(
            VenusVersion.VENUS_PRODUCER_FORMAT,
            DocConfig.FUGERIT_VENUS_DOC,
            getModuleVersion(),
            "Apache PDFBox",
            getPdfBoxVersion());

    private static final String PRODUCER_DEFAULT_SH1 = String.format(
            VenusVersion.VENUS_PRODUCER_FORMAT_SH1,
            DocConfig.FUGERIT_VENUS_DOC,
            PRODUCER_OVER);

    private static String findDefaultProducer(DocBase docBase) {
        return SecurityHardeningUtil.applyHardening(
                docBase,
                SecurityHardeningConsts.SECURITY_HARDENING_1,
                () -> PRODUCER_DEFAULT_SH1,
                () -> PRODUCER_DEFAULT);
    }

    private void applyDocMetadata(PDDocument document, DocBase docBase) {
        PDDocumentInformation info = document.getDocumentInformation();
        Properties props = docBase.getStableInfo();

        String title = props.getProperty(GenericConsts.INFO_KEY_DOC_TITLE, config.getDocumentTitle());
        if (StringUtils.isNotEmpty(title)) {
            info.setTitle(title);
            config.setDocumentTitle(title);
        }

        String author = props.getProperty(GenericConsts.INFO_KEY_DOC_AUTHOR, config.getDocumentAuthor());
        if (StringUtils.isNotEmpty(author)) {
            info.setAuthor(author);
            config.setDocumentAuthor(author);
        }

        String subject = props.getProperty(GenericConsts.INFO_KEY_DOC_SUBJECT, config.getDocumentSubject());
        if (StringUtils.isNotEmpty(subject)) {
            info.setSubject(subject);
            config.setDocumentSubject(subject);
        }

        String language = props.getProperty(GenericConsts.INFO_KEY_DOC_LANGUAGE, config.getDocumentLanguage());
        if (StringUtils.isNotEmpty(language)) {
            config.setDocumentLanguage(language);
            document.getDocumentCatalog().setLanguage(language);
        }

        String creator = props.getProperty(GenericConsts.INFO_KEY_DOC_CREATOR, VenusVersion.VENUS_CREATOR);
        info.setCreator(creator);

        String producer = props.getProperty(GenericConsts.INFO_KEY_DOC_PRODUCER, findDefaultProducer(docBase));
        info.setProducer(producer);
    }

    @Override
    public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
        log.info("=== PdfBoxTypeHandler.handle() called ===");

        // Get the parsed document model
        DocBase docBase = docInput.getDoc();
        log.info("DocBase: {}", docBase != null ? docBase.getClass().getName() : "NULL");

        if (docBase == null) {
            throw new IllegalStateException("DocBase is null - document was not parsed");
        }

        try (PDDocument document = new PDDocument()) {
            applyDocMetadata(document, docBase);

            // Configure PDF/A or PDF/UA if requested
            if (config.isPdfAEnabled()) {
                log.info("Configuring PDF/A");
                configurePdfA(document);
            }
            if (config.isPdfUAEnabled()) {
                log.info("Configuring PDF/UA");
                configurePdfUA(document);
            }

            // Process document structure
            log.info("Creating renderer");
            PdfBoxDocumentRenderer renderer = new PdfBoxDocumentRenderer(document, config);

            log.info("Calling renderDocument");
            renderer.renderDocument(docBase);

            // Save document
            log.info("Saving PDF");
            document.save(docOutput.getOs());

            log.info("PDF saved successfully");
        } catch (Exception e) {
            log.error("Error rendering PDF", e);
            throw e;
        }
    }

    private void configurePdfA(PDDocument document) throws IOException {
        PdfAConfigurator.configurePdfA1b(document, config);
    }

    private void configurePdfUA(PDDocument document) throws IOException {
        PdfUAConfigurator.configurePdfUA1(document, config);
    }
}
