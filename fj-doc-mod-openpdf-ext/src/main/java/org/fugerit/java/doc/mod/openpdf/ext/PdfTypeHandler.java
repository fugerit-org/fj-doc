package org.fugerit.java.doc.mod.openpdf.ext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocHeaderFooter;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.DocumentMetaHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPDFConfigHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;
import org.w3c.dom.Element;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.xmp.DublinCoreSchema;
import com.lowagie.text.xml.xmp.PdfSchema;
import com.lowagie.text.xml.xmp.XmpBasicSchema;
import com.lowagie.text.xml.xmp.XmpSchema;
import com.lowagie.text.xml.xmp.XmpWriter;

public class PdfTypeHandler extends DocTypeHandlerDefault implements PDFUA1Conf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5459938865782356227L;
	
	public static final DocTypeHandler HANDLER = new PdfTypeHandler();

	private boolean activatePDFUA1;

	public PdfTypeHandler() {
		super( DocConfig.TYPE_PDF, OpenPpfDocHandler.MODULE );
		this.setActivatePDFUA1( false );
	}

	private static boolean checkTotalPageNeeded( DocBase docBase ) {
		return checkContainerHasTotalPage( docBase.getDocFooter() )
				|| checkContainerHasTotalPage( docBase.getDocHeader() );
	}

	private static boolean checkContainerHasTotalPage( DocHeaderFooter container ) {
		if ( container != null && container.getElementList() != null ) {
			for ( DocElement el : container.getElementList() ) {
				if ( el instanceof DocPara ) {
					String text = ((DocPara) el).getText();
					if ( text != null && (text.contains( "${pageCount}" ) || text.contains( "${totalPage}" )) ) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void setupPdfUA1(PdfWriter pdfWriter, DocBase docBase) throws Exception {
		if (this.isActivatePDFUA1()) {
			pdfWriter.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
			pdfWriter.setViewerPreferences(PdfWriter.DisplayDocTitle);
			pdfWriter.setTagged();
			pdfWriter.setUserProperties(true);

			String lang = docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_LANGUAGE);
			if (StringUtils.isNotEmpty(lang)) {
				pdfWriter.getExtraCatalog().put(PdfName.LANG, new PdfString(lang));
			}

			ByteArrayOutputStream xmpBaos = new ByteArrayOutputStream();
			XmpWriter xmpWriter = new XmpWriter(xmpBaos);
			XmpSchema pdfUaSchema = new XmpSchema("xmlns:pdfuaid=\"http://www.aiim.org/pdfua/ns/id/\"") {};
			pdfUaSchema.setProperty("pdfuaid:part", "1");
			xmpWriter.addRdfDescription(pdfUaSchema);

			DublinCoreSchema dc = new DublinCoreSchema();
			String title = docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_TITLE);
			if (title != null) {
				dc.addTitle(title);
			}
			String author = docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_AUTHOR);
			if (author != null) {
				dc.addAuthor(author);
			}
			String subject = docBase.getStableInfo().getProperty(GenericConsts.INFO_KEY_DOC_SUBJECT);
			if (subject != null) {
				dc.addDescription(subject);
			}
			if (StringUtils.isNotEmpty(lang)) {
				dc.setProperty(DublinCoreSchema.LANGUAGE, lang);
			}
			xmpWriter.addRdfDescription(dc);

			PdfSchema pdfSchema = new PdfSchema();
			pdfSchema.addProducer("OpenPDF");
			xmpWriter.addRdfDescription(pdfSchema);

			XmpBasicSchema basic = new XmpBasicSchema();
			basic.addCreatorTool("Fugerit Venus Doc");
			xmpWriter.addRdfDescription(basic);

			xmpWriter.close();
			pdfWriter.setXmpMetadata(xmpBaos.toByteArray());
		}
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		DocBase docBase = docInput.getDoc();
		OutputStream outputStream = docOutput.getOs();
		String[] margins = docBase.getInfo().getProperty( "margins", "20;20;20;20" ).split( ";" );
		Document document = new Document( PageSize.A4, Integer.parseInt( margins[0] ),
				Integer.parseInt( margins[1] ),
				Integer.parseInt( margins[2] ), 
				Integer.parseInt( margins[3] ) );
		// allocate buffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// create pdf writer
		PdfWriter pdfWriter = PdfWriter.getInstance( document, baos );
		// create doc handler
		OpenPpfDocHandler handler = new OpenPpfDocHandler( document, pdfWriter );
		if ( "true".equalsIgnoreCase( docBase.getInfo().getProperty( "set-total-page" ) ) || checkTotalPageNeeded( docBase ) ) {
			handler.handleDoc( docBase );
			int totalPageCount = pdfWriter.getCurrentPageNumber()-1;
			document = new Document( PageSize.A4, Integer.parseInt( margins[0] ),
					Integer.parseInt( margins[1] ),
					Integer.parseInt( margins[2] ), 
					Integer.parseInt( margins[3] ) );
			baos = new ByteArrayOutputStream();
			pdfWriter = PdfWriter.getInstance( document, baos );
			handler = new OpenPpfDocHandler(document, pdfWriter, totalPageCount );
		}
		this.setupPdfUA1( pdfWriter, docBase );
		DocumentMetaHelper.handleDocMeta( document, docBase );
		handler.handleDoc( docBase );
		baos.writeTo( outputStream );
		baos.close();
		outputStream.close();	
	}

	@Override
	public boolean isActivatePDFUA1() {
		return this.activatePDFUA1;
	}

	@Override
	public void setActivatePDFUA1(boolean value) {
		this.activatePDFUA1 = value;
	}

	@Override
	protected void handleConfigTag(Element config) throws ConfigException {
		super.handleConfigTag(config);
		OpenPDFConfigHelper.handleConfig( config, this.getType(), this );
	}

}
