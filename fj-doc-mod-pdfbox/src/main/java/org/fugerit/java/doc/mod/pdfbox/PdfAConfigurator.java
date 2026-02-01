package org.fugerit.java.doc.mod.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;
import org.fugerit.java.core.lang.helpers.ClassHelper;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PdfAConfigurator {

    private PdfAConfigurator() {}

    public static void configurePdfA1b(PDDocument document, PdfBoxConfig config)
            throws IOException {

        // Set PDF version to 1.4 for PDF/A-1b
        document.setVersion(1.4f);

        // Create document info
        PDDocumentInformation info = document.getDocumentInformation();
        info.setTitle(config.getDocumentTitle());
        info.setAuthor(config.getDocumentAuthor());
        info.setSubject(config.getDocumentSubject());
        info.setCreator("Venus Doc PDFBox Handler");
        info.setProducer("Apache PDFBox 3.x");
        Calendar creationDate = new GregorianCalendar();
        info.setCreationDate(creationDate);
        info.setModificationDate(creationDate);

        // Add XMP metadata for PDF/A
        addPdfAMetadata(document, config);

        // Add sRGB color profile (required for PDF/A-1b)
        addOutputIntent(document);
    }

    private static void addPdfAMetadata(PDDocument document, PdfBoxConfig config)
            throws IOException {
        try {
            // Create XMP metadata
            XMPMetadata xmp = XMPMetadata.createXMPMetadata();

            // PDF/A Identification Schema
            PDFAIdentificationSchema pdfaid = xmp.createAndAddPDFAIdentificationSchema();
            pdfaid.setPart(1); // PDF/A-1
            pdfaid.setConformance("B"); // Level B (basic)

            // Dublin Core Schema
            DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
            dc.setTitle(config.getDocumentTitle());
            dc.setDescription(config.getDocumentSubject());
            dc.addCreator(config.getDocumentAuthor());
            dc.setFormat("application/pdf");

            // XMP Basic Schema
            XMPBasicSchema xmpBasic = xmp.createAndAddXMPBasicSchema();
            xmpBasic.setCreateDate(Calendar.getInstance());
            xmpBasic.setModifyDate(Calendar.getInstance());
            xmpBasic.setCreatorTool("Venus Doc PDFBox Handler");

            // Serialize and attach to document
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XmpSerializer serializer = new XmpSerializer();
            serializer.serialize(xmp, baos, true);

            PDMetadata metadata = new PDMetadata(document);
            metadata.importXMPMetadata(baos.toByteArray());

            PDDocumentCatalog catalog = document.getDocumentCatalog();
            catalog.setMetadata(metadata);

        } catch (BadFieldValueException | TransformerException e) {
            throw new IOException("Failed to create PDF/A metadata", e);
        }
    }

    private static final String SRGB = "sRGB IEC61966-2.1";

    private static void addOutputIntent(PDDocument document) throws IOException {
        // Load sRGB color profile from resources
        try (InputStream colorProfile = ClassHelper.loadFromDefaultClassLoader("fugerit-doc-pdfbox/sRGB_v4_ICC_preference_displayclass.icc")) {

            if (colorProfile == null) {
                throw new IOException("sRGB color profile not found in resources");
            }

            PDOutputIntent intent = new PDOutputIntent(document, colorProfile);
            intent.setInfo(SRGB);
            intent.setOutputCondition(SRGB);
            intent.setOutputConditionIdentifier(SRGB);
            intent.setRegistryName("http://www.color.org");

            PDDocumentCatalog catalog = document.getDocumentCatalog();
            catalog.addOutputIntent(intent);
        }
    }
}