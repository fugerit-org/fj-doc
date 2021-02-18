package test.org.fugerit.java.doc.mod.itext.poc;

import java.awt.Color;
import java.io.FileOutputStream;

import org.junit.Test;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfWriter;

public class PocIndex {

	private static final String VERSION = "002";
	
	@Test
	public void testIndex001() throws Exception {
		String dest = "target/test_index001.pdf";
	    Document document = new Document(PageSize.A4.rotate());
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
	    writer.setViewerPreferences( PdfWriter.DisplayDocTitle );
	    document.addTitle( "Test title "+VERSION );
	    writer.setPdfVersion(PdfWriter.VERSION_1_7);
	    
	    //TAGGED PDF
	    //Make document tagged
	    writer.setTagged();
	    //===============
	    //PDF/UA
	    //Set document metadata
	    writer.setViewerPreferences(PdfWriter.DisplayDocTitle);
	    //document.addLanguage("en-US");
	    writer.createXmpMetadata();
	    //=====================
	    document.open();
	    
	    document.add(new Paragraph("Outline action example "+VERSION));
	    
		PdfContentByte cb = writer.getDirectContent();
		PdfOutline root = cb.getRootOutline();
		PdfOutline links = new PdfOutline(root, new PdfAction("http://www.lowagie.com/iText/links.html"),
				"Useful links");
		links.setColor(new Color(0x00, 0x80, 0x80));
		links.setStyle(Font.BOLD);
		new PdfOutline(links, new PdfAction("http://www.lowagie.com/iText"), "Bruno's iText site");
		new PdfOutline(links, new PdfAction("http://itextpdf.sourceforge.net/"), "Paulo's iText site");
		new PdfOutline(links, new PdfAction("http://sourceforge.net/projects/itext/"), "iText @ SourceForge");
		PdfOutline other = new PdfOutline(root, new PdfDestination(PdfDestination.FIT), "other actions", false);
		other.setStyle(Font.ITALIC);
		new PdfOutline(other, new PdfAction("remote.pdf", 1), "Go to yhe first page of a remote file");
		new PdfOutline(other, new PdfAction("remote.pdf", "test"), "Go to a local destination in a remote file");
		new PdfOutline(other, PdfAction.javaScript("app.alert('Hello');\r", writer), "Say Hello");	    
	    
	    Font font = new Font( Font.HELVETICA );
	    Paragraph p = new Paragraph();
	    //PdfStructureElement ele = new PdfStructureElement( writer.getStructureTreeRoot(), PdfName.H1 );
	   
	    
	    //PDF/UA
	    //Embed font
	    //==================
	    Chunk c = new Chunk("Test tag");
	    
	    p.add(c);
	    document.add(p);
	    p = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n", font);
	    document.add(p);
	    List list = new List(true);
	    list.add(new ListItem("quick", font));
	    list.add(new ListItem("brown", font));
	    list.add(new ListItem("fox", font));
	    list.add(new ListItem("jumps", font));
	    list.add(new ListItem("over", font));
	    list.add(new ListItem("the", font));
	    list.add(new ListItem("lazy", font));
	    list.add(new ListItem("dog", font));
	    document.add(list);
	    
	    //PdfStructureTreeRoot tree = writer.getStructureTreeRoot();;
	    
	    
//		PdfOutline root = writer.getRootOutline();
//		PdfDestination mybookmark = new PdfDestination( PdfDestination.FITH, writer.getVerticalPosition(true) );
//		PdfOutline bookmark = new PdfOutline( root , mybookmark, "test bookmark", true );
//		ArrayList outlines = new ArrayList();
//		HashMap map = new HashMap();
//		outlines.add(map);
		

		
	    
	    
	    document.close();

	}
		
}
