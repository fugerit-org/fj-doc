package org.fugerit.java.doc.base.process;

import java.io.InputStream;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.util.filterchain.MiniFilterConfig;
import org.fugerit.java.core.util.filterchain.MiniFilterConfigEntry;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DocProcessConfig extends MiniFilterConfig {

	private static final long serialVersionUID = 6756541624973903875L;

	public static final String ATT_TAG_DATA_LIST = "chain";
	
	public static final String ATT_TAG_DATA = "step";
	
	public DocProcessConfig() {
		super( ATT_TAG_DATA_LIST, ATT_TAG_DATA );
		this.getGeneralProps().setProperty( ATT_TYPE , MiniFilterConfigEntry.class.getName() );
	}

	public static DocProcessConfig loadConfigSafe( String configPath ) {
		DocProcessConfig config = null;
		try ( InputStream is = StreamHelper.resolveStream( configPath ) ) {
			config = loadConfig( is );
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Exception on loadConfigSafe : "+e, e );
		}
		return config;
	}
	
	public static DocProcessConfig loadConfig( InputStream is ) throws Exception {
		DocProcessConfig config = new DocProcessConfig();
		Document doc = DOMIO.loadDOMDoc( is );
		Element root = doc.getDocumentElement();
		config.configure( root );
		return config;
	}
	
}
