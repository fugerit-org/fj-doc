package org.fugerit.java.doc.mod.opencsv;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocPara;
import org.fugerit.java.doc.base.model.DocPhrase;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.typehelper.csv.CsvHelperConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

public class OpenCSVTypeHandler extends DocTypeHandlerDefault {

	private static final Logger log = LoggerFactory.getLogger( OpenCSVTypeHandler.class );
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1832379566311585295L;
	
	public static final String TYPE_CSV = "csv";
	public static final String MODULE = "csv";
	public static final String MIME = "text/csv";

	public static final OpenCSVTypeHandler HANDLER = new OpenCSVTypeHandler();
	
	public static final DocTypeHandler HANDLER_UTF8 = new OpenCSVTypeHandler( StandardCharsets.UTF_8 );
	
	public OpenCSVTypeHandler( Charset charset ) {
		super(TYPE_CSV, MODULE, MIME, charset);
	}
	
	public OpenCSVTypeHandler() {
		this(null);
	}
	
	private String handleCell( DocCell cell ) {
		StringBuilder currentContent = new StringBuilder();
		for ( DocElement contentElement : cell.getElementList() ) {
			if ( contentElement instanceof DocPara ) {
				DocPara para = (DocPara) contentElement;
				currentContent.append( para.getText() );
			} else if ( contentElement instanceof DocPhrase ) {
				DocPhrase para = (DocPhrase) contentElement;
				currentContent.append( para.getText() );
			}
		}
		return currentContent.toString();
	}
	
	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
 		DocBase docBase = docInput.getDoc();
		CSVWriter writer = new CSVWriter( new OutputStreamWriter( docOutput.getOs(), this.getCharset()  ) );
		String csvTableId = docBase.getInfo().getProperty( CsvHelperConsts.PROP_CSV_TABLE_ID );
		DocTable table = (DocTable)docBase.getElementById( csvTableId );
		if ( table == null ) {
			log.warn( "table id {} not found!", csvTableId );
		} else {
			log.info( "handling table id {}", csvTableId );
			String[] currentRow = new String[ table.getColumns() ];
			for ( DocElement currentElement : table.getElementList() ) {
				DocRow row = (DocRow) currentElement;
				int count = 0;
				for ( DocElement currentCell : row.getElementList() ) {
					DocCell cell = (DocCell) currentCell;
					currentRow[count] = this.handleCell(cell);
					count++;
				}
				writer.writeNext( currentRow );
			}
			writer.flush();
		}
	}
	
}
