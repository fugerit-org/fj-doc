package test.org.fugerit.java.doc.freemarker.coverage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocBookmarkTree;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.xml.DocValidator;
import org.fugerit.java.doc.freemarker.asciidoc.FreeMarkerAsciidocTypeHandler;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandlerUTF8;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerEscapeUTF8;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerUTF8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestFullCoverage {

	private static final String FULL_COVERAGE_XML = "coverage/xml/full-coverage.xml";

	@Test
	void testValidateAndParseModel() {
		SafeFunction.apply( () -> {
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( FULL_COVERAGE_XML ), StandardCharsets.UTF_8 ) ) {
				SAXParseResult validationResult = DocValidator.validateVersion( reader );
				DocValidator.logResult( validationResult, log );
				Assertions.assertTrue( validationResult.isTotalSuccess(), "full-coverage.xml must be valid against doc-2-1.xsd" );
			}
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( FULL_COVERAGE_XML ), StandardCharsets.UTF_8 ) ) {
				DocBase docBase = DocFacade.parse( reader );
				Assertions.assertNotNull( docBase );
				Assertions.assertEquals( "Full Coverage Venus Doc", docBase.getInfoDocTitle() );
				Assertions.assertEquals( "Comprehensive test case for Venus Doc Source Document features", docBase.getInfoDocSubject() );
				Assertions.assertEquals( "fugerit79", docBase.getInfoDocAuthor() );
				Assertions.assertEquals( "Venus Doc Test Suite", docBase.getInfoDocCreator() );
				Assertions.assertEquals( "Venus Doc Full Coverage Producer", docBase.getInfoDocProducer() );
				Assertions.assertEquals( "en", docBase.getInfoDocLanguage() );
				Assertions.assertEquals( "2.1.0", docBase.getInfoDocVersion() );
				Assertions.assertEquals( "21cm", docBase.getInfoPageWidth() );
				Assertions.assertEquals( "29.7cm", docBase.getInfoPageHeight() );
				Assertions.assertEquals( 15, docBase.getMarginLeft() );
				Assertions.assertEquals( 15, docBase.getMarginRight() );
				Assertions.assertEquals( 20, docBase.getMarginTop() );
				Assertions.assertEquals( 25, docBase.getMarginBottom() );

				// Header & Footer
				Assertions.assertTrue( docBase.isUseHeader() );
				DocHeader header = docBase.getDocHeader();
				Assertions.assertFalse( header.isBasic() );
				Assertions.assertTrue( header.isNumbered() );
				Assertions.assertEquals( 1, header.getBorderWidth() );
				Assertions.assertEquals( 25, header.getExpectedSize() );

				Assertions.assertTrue( docBase.isUseFooter() );
				DocFooter footer = docBase.getDocFooter();
				Assertions.assertFalse( footer.isBasic() );
				Assertions.assertTrue( footer.isNumbered() );
				Assertions.assertEquals( 1, footer.getBorderWidth() );
				Assertions.assertEquals( 25, footer.getExpectedSize() );

				// Background & Bookmarks
				Assertions.assertNotNull( docBase.getDocBackground() );
				DocBookmarkTree bookmarkTree = docBase.getDocBookmarkTree();
				Assertions.assertNotNull( bookmarkTree );
				Assertions.assertEquals( 4, bookmarkTree.containerSize() );

				// ID lookup
				DocTable mainTable = (DocTable) docBase.getElementById( "main-data-table" );
				Assertions.assertNotNull( mainTable );
				Assertions.assertEquals( 4, mainTable.getColumns() );
				Assertions.assertTrue( mainTable.getUtil().isStrictHeader() );
			}
		} );
	}

	@Test
	void testRenderFullCoverageHtml() {
		DocTypeHandler handler = FreeMarkerHtmlTypeHandlerUTF8.HANDLER;
		File outputFile = new File( "target/full-coverage." + DocConfig.TYPE_HTML );
		SafeFunction.apply( () -> {
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( FULL_COVERAGE_XML ), StandardCharsets.UTF_8 );
				  FileOutputStream fos = new FileOutputStream( outputFile ) ) {
				handler.handle( DocInput.newInput( handler.getType(), reader ), DocOutput.newOutput( fos ) );
			}
		} );
		Assertions.assertTrue( outputFile.exists() );
		String html = SafeFunction.get( () -> FileIO.readString( outputFile ) );
		log.info( "Generated HTML size: {}", html.length() );

		// 1. Metadata & Head assertions
		Assertions.assertTrue( html.contains( "<html lang=\"en\">" ) );
		Assertions.assertTrue( html.contains( "<title>Full Coverage Venus Doc</title>" ) );
		Assertions.assertTrue( html.contains( "<meta charset=\"UTF-8\">" ) );
		Assertions.assertTrue( html.contains( "<meta name=\"author\" content=\"fugerit79\"/>" ) );
		Assertions.assertTrue( html.contains( "<meta name=\"description\" content=\"Comprehensive test case for Venus Doc Source Document features\"/>" ) );
		Assertions.assertTrue( html.contains( "<meta http-equiv=\"content-language\" content=\"en\"/>" ) );
		Assertions.assertTrue( html.contains( "<meta http-equiv=\"generator\" content=\"Venus Doc Full Coverage Producer\"/>" ) );
		Assertions.assertTrue( html.contains( "<meta http-equiv=\"created-by\" content=\"Venus Doc Test Suite\"/>" ) );
		Assertions.assertTrue( html.contains( "<meta name=\"keywords\" content=\"venus,fj-doc,coverage\"/>" ) );
		Assertions.assertTrue( html.contains( ".custom-para { border-left: 2px solid #336699; }" ) );
		Assertions.assertTrue( html.contains( "<link rel=\"stylesheet\" href=\"/css/full-coverage.css\"/>" ) );

		// 2. Headings h1..h7
		Assertions.assertTrue( html.contains( "<h1  id=\"h1-title\"" ) );
		Assertions.assertTrue( html.contains( "Main Title H1</h1>" ) );
		Assertions.assertTrue( html.contains( "<h2  id=\"section-typography\"" ) );
		Assertions.assertTrue( html.contains( "<h3  id=\"h3-title\"" ) );
		Assertions.assertTrue( html.contains( "<h4  id=\"h4-title\"" ) );
		Assertions.assertTrue( html.contains( "<h5  id=\"h5-title\"" ) );
		Assertions.assertTrue( html.contains( "<h6  id=\"h6-title\"" ) );
		Assertions.assertTrue( html.contains( "<h7  id=\"h7-title\"" ) );

		// 3. Paragraphs & Phrases styling, links, anchors, whitespace
		Assertions.assertTrue( html.contains( "id=\"para-times-bold\"" ) );
		Assertions.assertTrue( html.contains( "font-family: 'times-roman';" ) );
		Assertions.assertTrue( html.contains( "white-space-collapse: preserve; white-space: pre-wrap;" ) );
		Assertions.assertTrue( html.contains( "href=\"https://venusdocs.fugerit.org\"" ) );
		Assertions.assertTrue( html.contains( "href=\"#h1-title\"" ) );
		Assertions.assertTrue( html.contains( "id=\"my-anchor\"" ) );
		Assertions.assertTrue( html.contains( "&nbsp;&nbsp;&nbsp;" ) );

		// 4. Images & SVG
		Assertions.assertTrue( html.contains( "alt=\"Teal JPG from ClassLoader\"" ) );
		Assertions.assertTrue( html.contains( "alt=\"Inline Base64 PNG\"" ) );
		Assertions.assertTrue( html.contains( "<svg xmlns=\"http://www.w3.org/2000/svg\"" ) );

		// 5. Tables: strict header (thead/tbody), colors, borders, valign, spans, inline table
		Assertions.assertTrue( html.contains( "<table  id=\"main-data-table\"" ) );
		Assertions.assertTrue( html.contains( "background-color: #fafafa;" ) );
		Assertions.assertTrue( html.contains( "<thead>" ) );
		Assertions.assertTrue( html.contains( "<tbody>" ) );
		Assertions.assertTrue( html.contains( "id=\"cell-h1\"" ) );
		Assertions.assertTrue( html.contains( "background: #ddeeff;" ) );
		Assertions.assertTrue( html.contains( "vertical-align: middle;" ) );
		Assertions.assertTrue( html.contains( "vertical-align: top;" ) );
		Assertions.assertTrue( html.contains( "vertical-align: bottom;" ) );
		Assertions.assertTrue( html.contains( "border-top: 1px solid #ff0000; border-bottom: 2px solid #00ff00; border-left: 3px solid #0000ff; border-right: 4px solid #ffff00;" ) );
		Assertions.assertTrue( html.contains( "colspan=\"2\"" ) );
		Assertions.assertTrue( html.contains( "rowspan=\"2\"" ) );
		Assertions.assertTrue( html.contains( "<table  id=\"nested-table\"" ) );
		Assertions.assertTrue( html.contains( "<th id=\"cell_0_0\"" ) ); // header="true" cell in mixed-table
		Assertions.assertTrue( html.contains( "Inline Cell Left" ) );

		// 6. Lists: ul, ol, list-style-types, pl, nested list
		Assertions.assertTrue( html.contains( "<ol  id=\"list-default\">" ) );
		Assertions.assertTrue( html.contains( "<ul  id=\"list-ul\">" ) );
		Assertions.assertTrue( html.contains( "<ul  id=\"list-uld-nested\">" ) );
		Assertions.assertTrue( html.contains( "list-style-type: circle;" ) );
		Assertions.assertTrue( html.contains( "list-style-type: square;" ) );
		Assertions.assertTrue( html.contains( "list-style-type: lower-alpha;" ) );
		Assertions.assertTrue( html.contains( "Phrase list item: " ) );
	}

	@Test
	void testRenderOtherFreemarkerHandlers() {
		DocTypeHandler[] handlers = {
				FreeMarkerHtmlTypeHandlerEscapeUTF8.HANDLER,
				FreeMarkerHtmlFragmentTypeHandlerUTF8.HANDLER,
				FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8.HANDLER,
				FreeMarkerAsciidocTypeHandler.HANDLER_UTF8
		};
		for ( DocTypeHandler handler : handlers ) {
			SafeFunction.apply( () -> {
				File outputFile = new File( "target/full-coverage-" + handler.getClass().getSimpleName() + "." + handler.getType() );
				try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( FULL_COVERAGE_XML ), StandardCharsets.UTF_8 );
					  FileOutputStream fos = new FileOutputStream( outputFile ) ) {
					handler.handle( DocInput.newInput( handler.getType(), reader ), DocOutput.newOutput( fos ) );
				}
				Assertions.assertTrue( outputFile.exists() );
				Assertions.assertTrue( outputFile.length() > 0 );
			} );
		}
	}

}
