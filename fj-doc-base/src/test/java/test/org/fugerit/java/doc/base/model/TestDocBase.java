package test.org.fugerit.java.doc.base.model;

import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocFooter;
import org.fugerit.java.doc.base.model.DocHeader;
import org.fugerit.java.doc.base.model.DocInfo;
import org.fugerit.java.doc.base.model.DocPara;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocBase {

	@Test
	public void test1() {
		DocBase docBase = new DocBase();
		docBase.setDocMeta( new DocContainer() );
		DocInfo info = new DocInfo();
		info.setName( "test" );
		info.setContent( new StringBuilder() );
		docBase.getDocMeta().addElement(info);
		docBase.setDocBody( new DocContainer() );
		DocPara text = new DocPara();
		text.setText( "a" );
		docBase.getDocBody().addElement( text );
		log.info( "info : {}", docBase.getInfo() );
		docBase.setStableInfo( docBase.getInfo() );
		log.info( "info 1 : {}", docBase.getInfoPageHeight() );
		log.info( "info 2 : {}", docBase.getInfoPageWidth() );
		log.info( "info 3 : {}", docBase.getInfoDocVersion() );
		log.info( "info 4 : {}", docBase.getMarginTop() );
		log.info( "info 5 : {}", docBase.getMarginBottom() );
		log.info( "info 6 : {}", docBase.getMarginLeft() );
		log.info( "info 7 : {}", docBase.getMarginRight() );
		log.info( "info 8 : {}", docBase.isUseFooter() );
		log.info( "info 9 : {}", docBase.isUseHeader() );
		log.info( "info 10 : {}", docBase.getInfoDocCreator() );
		docBase.setDocHeader( new DocHeader() );
		docBase.setDocFooter( new DocFooter() );
		log.info( "info 8 : {}", docBase.isUseFooter() );
		log.info( "info 9 : {}", docBase.isUseHeader() );
		docBase.getDocHeader().setUseHeader(true);
		docBase.getDocFooter().setUseFooter(true);
		log.info( "info 8 : {}", docBase.isUseFooter() );
		log.info( "info 9 : {}", docBase.isUseHeader() );
		DocBase.print(docBase, System.out);
		Assert.assertNotNull( docBase );
		Assert.assertNull( docBase.getDocBackground() );
		Assert.assertNull( docBase.getDocBookmarkTree() );
	}
	
}
