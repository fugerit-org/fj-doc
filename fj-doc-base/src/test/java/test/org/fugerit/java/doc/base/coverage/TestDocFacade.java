package test.org.fugerit.java.doc.base.coverage;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.helper.SourceResolverHelper;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.model.DocImage;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.typehelper.excel.TableMatrix;
import org.junit.Assert;
import org.junit.Test;

public class TestDocFacade {

	private static final String DEF_PATH = "coverage/xml/default_doc.xml";
	
	private void additionalTest( DocBase doc ) {
		DocTable table = (DocTable)doc.getElementById( "excel-table" );
		Assert.assertNotNull( table );
		TableMatrix matrix = new TableMatrix( table.containerSize() , table.getColumns() );
		Iterator<DocElement> itRows = table.docElements();
		while ( itRows.hasNext() ) {
			DocRow row = (DocRow) itRows.next();
			Iterator<DocElement> itCells = row.docElements();
			while ( itCells.hasNext() ) {
				DocCell cell = (DocCell) itCells.next();
				matrix.setNext( cell , cell.getRowSpan(), cell.getColumnSpan() );
			}
		}
		Assert.assertNotNull( matrix );
		Assert.assertNotNull( matrix.getParent( 1, 1 ) );
		Assert.assertNotNull( matrix.getBorders( 1, 1 ) );
		Assert.assertFalse( matrix.isCellEmpty( 1, 1 ) );
		for ( int r=0; r<matrix.getRowCount(); r++ ) {
			for ( int c=0; c<matrix.getColumnCount(); c++ ) {
				matrix.getCell( r , c );	
			}
		}
		DocFacade.print( System.out , doc );
		// test image
		doc.getDocBody().getElementList().forEach( e -> {
			if ( e instanceof DocImage ) {
				DocImage img = (DocImage)e;
				SafeFunction.apply( () -> {
					byte[] data = SourceResolverHelper.resolveImage( img );
					Assert.assertNotNull( data );
				});
			}
		} );
	}
	
	@Test
	public void testParse001() {
		Assert.assertNotNull(
			SafeFunction.get( () -> {
				try ( InputStream reader = ClassHelper.loadFromDefaultClassLoader( DEF_PATH ) ) {
					DocBase doc =  DocFacade.parse( reader );
					this.additionalTest(doc);
					return doc;
				}
			})
		);
	}
	
	@Test
	public void testParseRE001() {
		Assert.assertNotNull( SafeFunction.get( () -> {
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( DEF_PATH ) ) ) {
				 return DocFacade.parseRE( reader );
			}
		}) );
	}
	
	@Test
	public void testParseRE002() {
		Assert.assertNotNull( SafeFunction.get( () -> {
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( DEF_PATH ) ) ) {
				 return DocFacade.parseRE( reader, DocFacadeSource.SOURCE_TYPE_XML );
			}
		}) );
	}
	
	@Test
	public void testValidate001() {
		Assert.assertTrue( SafeFunction.get( () -> {
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( DEF_PATH ) ) ) {
				 return DocFacade.validate( reader );
			}
		}) );
	}
	
	@Test
	public void testParse002() {
		Assert.assertNotNull( SafeFunction.get( () -> {
			try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( DEF_PATH ) ) ) {
				 return DocFacade.parse( reader, new DocHelper() );
			}
		}) );
	}
	
}
