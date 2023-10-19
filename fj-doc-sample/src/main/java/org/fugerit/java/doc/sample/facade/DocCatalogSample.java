package org.fugerit.java.doc.sample.facade;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.xml.ListMapCatalogConfig;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.collection.ListMapStringKey;

public class DocCatalogSample extends ListMapCatalogConfig<DocCatalogEntry> {

	public static final String ATT_TAG_DATA_LIST = "catalog";
	
	public static final String ATT_TAG_DATA = "doc";
	
	public static final String ATT_BASE_PATH = "path-base";

	public static final String CATALOG_ID_PLAYGROUND_CORE = "playground-core";
	
	private static final long serialVersionUID = -79003856880584L;

	public DocCatalogSample() {
		super( ATT_TAG_DATA_LIST, ATT_TAG_DATA );
		this.getGeneralProps().setProperty( ATT_TYPE,DocCatalogEntry.class.getCanonicalName() );
	}
	
	public static DocCatalogSample loadConfig( InputStream is ) {
		return (DocCatalogSample)load( is, new DocCatalogSample() );
	}
	
	private static final DocCatalogSample INSTANCE = SafeFunction.get( ()  -> {
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample_docs/doc_catalog.xml" ) ) {
			return loadConfig( is );
		}
	} );
	
	public static DocCatalogSample getInstance() {
		return INSTANCE;
	}
	
	public Reader entryReaderJsonData( DocCatalogEntry entry ) throws ConfigException {
		return new InputStreamReader( this.entryStreamJsonData( entry ) );
	}
	
	public Reader entryReader( DocCatalogEntry entry ) throws ConfigException {
		return new InputStreamReader( this.entryStream( entry ) );
	}
	
	private InputStream loadStreamWorker( String path, DocCatalogEntry entry ) throws ConfigException {
		String fullPath = this.getGeneralProps().getProperty( ATT_BASE_PATH, "" )+path;
		logger.info( "load entry full path : {}, entry : {}", fullPath , entry );
		return ConfigException.get( () -> ClassHelper.loadFromDefaultClassLoader( fullPath ) );
	}
	
	public InputStream entryStreamJsonData( DocCatalogEntry entry ) throws ConfigException {
		return this.loadStreamWorker( entry.getJsonDataPath() , entry );
	}
	
	public InputStream entryStream( DocCatalogEntry entry ) throws ConfigException {
		return this.loadStreamWorker( entry.getPath() , entry );
	}
	
	public ListMapStringKey<DocCatalogEntry> getPlaygroundCoreCatalog() {
		return this.getListMap( CATALOG_ID_PLAYGROUND_CORE );
	}
	
}
