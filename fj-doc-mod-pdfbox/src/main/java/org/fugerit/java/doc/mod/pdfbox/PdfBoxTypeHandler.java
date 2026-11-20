package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.mvn.MavenProps;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.config.VenusVersion;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.fugerit.java.doc.base.typehelper.generic.SecurityHardeningConsts;
import org.fugerit.java.doc.base.typehelper.generic.SecurityHardeningUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class PdfBoxTypeHandler extends DocTypeHandlerDefault {

    private static final String MODULE = "pdfbox";

    public static final String TAG_NAME_FONT = "font";
    public static final String ATT_FONT_NAME = "name";
    public static final String ATT_FONT_PATH = "path";
    public static final String ATT_FONT_STYLE = "style";

    private final transient AtomicReference<PdfBoxConfig> configRef = new AtomicReference<>(new PdfBoxConfig());

    public PdfBoxTypeHandler() {
        super(DocConfig.TYPE_PDF, MODULE);
    }

    @Override
    public void configure(Properties config) throws ConfigException {
        if (config != null) {
            this.applyFormatProperty(config);
            this.configRef.set(PdfBoxConfig.fromProperties(config));
        }
    }

    @Override
    protected void handleConfigTag(Element config) throws ConfigException {
        super.handleConfigTag(config);
        if (config != null) {
            Properties props = DOMUtils.attributesToProperties(config);
            PdfBoxConfig parsedConfig = PdfBoxConfig.fromProperties(props);
            NodeList fontList = config.getElementsByTagName(TAG_NAME_FONT);
            for (int k = 0; k < fontList.getLength(); k++) {
                Element fontTag = (Element) fontList.item(k);
                String name = fontTag.getAttribute(ATT_FONT_NAME);
                String path = fontTag.getAttribute(ATT_FONT_PATH);
                String style = fontTag.getAttribute(ATT_FONT_STYLE);
                if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(path)) {
                    log.info("Registering custom font '{}' (style='{}') from '{}'", name, style, path);
                    parsedConfig.registerCustomFont(name, style, path);
                }
            }
            this.applyFormatProperty(props);
            this.configRef.set(parsedConfig);
        }
    }

    private void applyFormatProperty(Properties props) {
        String format = props.getProperty(PdfBoxConfig.FORMAT);
        if (StringUtils.isNotEmpty(format)) {
            this.setFormat(format);
        }
    }

    protected PdfBoxConfig getConfigCopy() {
        PdfBoxConfig current = this.configRef.get();
        return current != null ? current.copy() : new PdfBoxConfig();
    }

    private static String getModuleVersion() {
        return VenusVersion.getFjDocCoreVersionS();
    }

    private static String getPdfBoxVersion() {
        return StringUtils.valueWithDefault(MavenProps.getProperty("org.apache.pdfbox", MODULE, MavenProps.VERSION), "3.x");
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

    private void applyDocMetadata(PDDocument document, DocBase docBase, PdfBoxConfig localConfig) {
        PDDocumentInformation info = document.getDocumentInformation();
        Properties props = docBase.getStableInfo();

        String title = props.getProperty(GenericConsts.INFO_KEY_DOC_TITLE, localConfig.getDocumentTitle());
        if (StringUtils.isNotEmpty(title)) {
            info.setTitle(title);
            localConfig.setDocumentTitle(title);
        }

        String author = props.getProperty(GenericConsts.INFO_KEY_DOC_AUTHOR, localConfig.getDocumentAuthor());
        if (StringUtils.isNotEmpty(author)) {
            info.setAuthor(author);
            localConfig.setDocumentAuthor(author);
        }

        String subject = props.getProperty(GenericConsts.INFO_KEY_DOC_SUBJECT, localConfig.getDocumentSubject());
        if (StringUtils.isNotEmpty(subject)) {
            info.setSubject(subject);
            localConfig.setDocumentSubject(subject);
        }

        String language = props.getProperty(GenericConsts.INFO_KEY_DOC_LANGUAGE, localConfig.getDocumentLanguage());
        if (StringUtils.isNotEmpty(language)) {
            localConfig.setDocumentLanguage(language);
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

        PdfBoxConfig localConfig = this.getConfigCopy();

        try (PDDocument document = new PDDocument()) {
            applyDocMetadata(document, docBase, localConfig);

            // Configure PDF/A or PDF/UA if requested
            if (localConfig.isPdfAEnabled()) {
                log.info("Configuring PDF/A");
                configurePdfA(document, localConfig);
            }
            if (localConfig.isPdfUAEnabled()) {
                log.info("Configuring PDF/UA");
                configurePdfUA(document, localConfig);
            }

            // Process document structure
            log.info("Creating renderer");
            PdfBoxDocumentRenderer renderer = new PdfBoxDocumentRenderer(document, localConfig);

            log.info("Calling renderDocument");
            renderer.renderDocument(docBase);

            if (localConfig.isPdfUAEnabled()) {
                log.info("Tagging document content for PDF/UA");
                PdfUAConfigurator.tagDocumentContent(document);
            }

            // Save document
            log.info("Saving PDF");
            document.save(docOutput.getOs());

            log.info("PDF saved successfully");
        } catch (Exception e) {
            log.error("Error rendering PDF", e);
            throw e;
        }
    }

    private void configurePdfA(PDDocument document, PdfBoxConfig localConfig) throws IOException {
        PdfAConfigurator.configurePdfA1b(document, localConfig);
    }

    private void configurePdfUA(PDDocument document, PdfBoxConfig localConfig) throws IOException {
        PdfUAConfigurator.configurePdfUA1(document, localConfig);
    }
}
