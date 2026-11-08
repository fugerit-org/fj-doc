package org.fugerit.java.doc.mod.pdfbox;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDNumberTreeNode;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDMarkInfo;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDParentTreeValue;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.StandardStructureTypes;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.schema.XMPSchema;
import org.apache.xmpbox.type.IntegerType;
import org.apache.xmpbox.xml.XmpSerializer;
import org.fugerit.java.core.io.StreamIO;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PdfUAConfigurator {

    private PdfUAConfigurator() {
        // private constructor to hide the implicit public one.
    }

    // PDF/UA namespace
    private static final String PDFUA_NAMESPACE = "http://www.aiim.org/pdfua/ns/id/";
    private static final String PDFUA_PREFIX = "pdfuaid";

    public static void configurePdfUA1(PDDocument document, PdfBoxConfig config)
            throws IOException {

        // Configure document catalog for accessibility
        PDDocumentCatalog catalog = document.getDocumentCatalog();

        // Set document language (required for PDF/UA)
        catalog.setLanguage(config.getDocumentLanguage());

        // Configure viewer preferences - FIX for Error 1
        PDViewerPreferences viewerPrefs = catalog.getViewerPreferences();
        if (viewerPrefs == null) {
            viewerPrefs = new PDViewerPreferences();
        }
        viewerPrefs.setDisplayDocTitle(true);
        catalog.setViewerPreferences(viewerPrefs);

        // Enable tagging (required for PDF/UA)
        PDMarkInfo markInfo = new PDMarkInfo();
        markInfo.setMarked(true);
        markInfo.setUserProperties(true);
        catalog.setMarkInfo(markInfo);

        // Initialize structure tree root
        PDStructureTreeRoot structTreeRoot = new PDStructureTreeRoot();
        catalog.setStructureTreeRoot(structTreeRoot);

        // Add PDF/UA metadata
        addPdfUAMetadata(document, config);
    }

    /**
     * Tag rendered page content streams with Marked Content sequences and populate
     * the PDF/UA-1 logical structure tree and parent tree.
     *
     * @param document rendered PDDocument
     * @throws IOException if page stream processing fails
     */
    public static void tagDocumentContent(PDDocument document) throws IOException {
        PDDocumentCatalog catalog = document.getDocumentCatalog();
        PDStructureTreeRoot structTreeRoot = catalog.getStructureTreeRoot();
        if (structTreeRoot == null) {
            structTreeRoot = new PDStructureTreeRoot();
            catalog.setStructureTreeRoot(structTreeRoot);
        }

        PDStructureElement docElem = new PDStructureElement(StandardStructureTypes.DOCUMENT, structTreeRoot);
        structTreeRoot.appendKid(docElem);

        Map<Integer, COSObjectable> parentTreeMap = new HashMap<>();
        int pageIdx = 0;
        for (PDPage page : document.getPages()) {
            ByteArrayOutputStream combined = new ByteArrayOutputStream();
            combined.write("/P << /MCID 0 >> BDC\n".getBytes(StandardCharsets.ISO_8859_1));
            try (InputStream is = page.getContents()) {
                if (is != null) {
                    StreamIO.pipeStream(is, combined, StreamIO.MODE_CLOSE_NONE);
                }
            }
            combined.write("\nEMC\n".getBytes(StandardCharsets.ISO_8859_1));

            PDStream newStream = new PDStream(document, new ByteArrayInputStream(combined.toByteArray()), COSName.FLATE_DECODE);
            page.setContents(newStream);
            page.setStructParents(pageIdx);

            PDStructureElement pElem = new PDStructureElement(StandardStructureTypes.P, docElem);
            pElem.setPage(page);
            pElem.appendKid(0);
            docElem.appendKid(pElem);

            COSArray pageArray = new COSArray();
            pageArray.add(pElem.getCOSObject());
            parentTreeMap.put(pageIdx, new PDParentTreeValue(pageArray));
            pageIdx++;
        }

        PDNumberTreeNode parentTree = new PDNumberTreeNode(PDParentTreeValue.class);
        parentTree.setNumbers(parentTreeMap);
        structTreeRoot.setParentTree(parentTree);
        structTreeRoot.setParentTreeNextKey(pageIdx);
    }

    private static void addPdfUAMetadata(PDDocument document, PdfBoxConfig config)
            throws IOException {
        try {
            XMPMetadata xmp = XMPMetadata.createXMPMetadata();

            // Add PDF/UA identification manually - FIX for Error 2
            addPdfUAIdentification(xmp);

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

            // Serialize and attach
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XmpSerializer serializer = new XmpSerializer();
            serializer.serialize(xmp, baos, true);

            PDMetadata metadata = new PDMetadata(document);
            metadata.importXMPMetadata(baos.toByteArray());
            document.getDocumentCatalog().setMetadata(metadata);

        } catch (TransformerException e) {
            throw new IOException("Failed to create PDF/UA metadata", e);
        }
    }

    /**
     * Manually add PDF/UA identification properties to XMP metadata
     * Creates a custom schema for PDF/UA since XMPBox doesn't provide one
     */
    private static void addPdfUAIdentification(XMPMetadata xmp) {
        // Create a custom XMP schema for PDF/UA
        XMPSchema pdfuaSchema = new XMPSchema(xmp, PDFUA_NAMESPACE, PDFUA_PREFIX);

        // Add part property (1 for PDF/UA-1)
        IntegerType part = new IntegerType(xmp, PDFUA_NAMESPACE, PDFUA_PREFIX, "part", 1);
        pdfuaSchema.addProperty(part);

        // Add the schema to metadata
        xmp.addSchema(pdfuaSchema);
    }
}
