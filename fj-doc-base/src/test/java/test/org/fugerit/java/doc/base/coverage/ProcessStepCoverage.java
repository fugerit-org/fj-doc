package test.org.fugerit.java.doc.base.coverage;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.base.process.DocProcessorBasic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessStepCoverage extends DocProcessorBasic {

	private static final long serialVersionUID = 6389046120807963369L;

	@Override
	public int process(DocProcessContext context, DocProcessData data) throws Exception {
		String xmlPath = this.getCustomConfig().getProperty( "xmlPath" ); 
		log.info( "xmlPath {}", xmlPath );
		data.setCurrentXmlData( StreamIO.readString( ClassHelper.loadFromDefaultClassLoader( xmlPath ) ) );
		return super.process(context, data);
	}

}
