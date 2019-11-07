package org.fugerit.java.doc.ent.servlet.facade;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.log.BasicLogObject;
import org.fugerit.java.core.log.LogFacade;
import org.fugerit.java.core.web.servlet.config.ConfigContext;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.fugerit.java.core.xml.dom.SearchDOM;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.ent.servlet.DefaultDocHandler;
import org.fugerit.java.doc.ent.servlet.DocHandler;
import org.fugerit.java.doc.ent.servlet.DocTypeHandler;
import org.fugerit.java.doc.mod.itext.ITextDocHandler;
import org.w3c.dom.Element;

public class DocRequestConfig extends BasicLogObject {

	private ConfigContext context;

	public HashMap<String, DocTypeHandler> getTypeHandlerMap() {
		return typeHandlerMap;
	}

	public boolean isSkipFilter() {
		return skipFilter;
	}

	public String getProcessingPage() {
		return processingPage;
	}
	
	public String getErrorManager() {
		return errorManager;
	}

	private void addTypeHandlers( SearchDOM searchDOM, Element rootTag ) {
		List<Element> typeHandlerTagList = searchDOM.findAllTags( rootTag , "type-handler" );
		Iterator<Element> typeHandlerTagIt = typeHandlerTagList.iterator();
		while ( typeHandlerTagIt.hasNext() ) {
			Element docHandlerTag = (Element) typeHandlerTagIt.next();
			Properties atts = DOMUtils.attributesToProperties( docHandlerTag );
			String name = atts.getProperty( "name" );
			String type = atts.getProperty( "type" );
			this.getLogger().info( "TypeHandler : "+name+" -> "+type );
			try {
				DocTypeHandler typeHandler = (DocTypeHandler)ClassHelper.newInstance( type );
				typeHandler.init( docHandlerTag );
				this.typeHandlerMap.put( name , typeHandler );
			} catch (Throwable t1) {
				this.getLogger().warn( "Error on type handler init : "+name+" : "+type, t1 );
			}
		}
	}

	public void configure( Element root, ConfigContext context ) {
		this.docHandlerMap = new HashMap<>();
		Properties defMime = new Properties();
		try {
			this.context = context;
			this.typeHandlerMap = new HashMap<>();
			SearchDOM searchDOM = SearchDOM.newInstance( true , true );
			Element generalConfigTag = searchDOM.findTag( root , "general-config" );
			Properties generalConfigAtts = DOMUtils.attributesToProperties( generalConfigTag );
			this.jspPath = generalConfigAtts.getProperty( "jsp-path" );
			this.outMode = generalConfigAtts.getProperty( "out-mode" );
			this.debug = Boolean.valueOf( generalConfigAtts.getProperty( "debug" ) ).booleanValue();
			this.debug = Boolean.valueOf( generalConfigAtts.getProperty( "skip-filter" ) ).booleanValue();
			this.processingPage = generalConfigAtts.getProperty( "processing-page" );
			this.errorManager = generalConfigAtts.getProperty( "error-manager" );
			this.getLogger().info( "jsp-path : "+this.jspPath );
			this.getLogger().info( "debug    : "+this.debug );
			this.getLogger().info( "processing-page    : "+this.processingPage );
			// doc helper
			String docHelperType = generalConfigAtts.getProperty( "helper-type" );
			this.getLogger().info( "docHelperType    : "+docHelperType );
			if ( docHelperType != null ) {
				try {
					this.docHelper = (DocHelper)ClassHelper.newInstance( docHelperType );
				} catch (Exception e2) {
					this.getLogger().info( "failed to create : "+e2 );
					this.docHelper = DocHelper.DEFAULT;
				}
			} else {
				this.docHelper = DocHelper.DEFAULT;
			}
			this.getLogger().info( "docHelper    : "+this.docHelper );
						
			// itext font
			List<Element> itextFontTagList = searchDOM.findAllTags( root , "itext-font" );
			Iterator<Element> itextFontTagIt = itextFontTagList.iterator();
			while ( itextFontTagIt.hasNext() ) {
				Element itextFontTag = (Element) itextFontTagIt.next();
				Properties itextFontAtts = DOMUtils.attributesToProperties( itextFontTag );
				String name = itextFontAtts.getProperty( "name" );
				String path = itextFontAtts.getProperty( "path" );
				this.getLogger().info( "ITextFont : "+name+" -> "+path );
				ITextDocHandler.registerFont(name, path);
			}
			// type handlers
			try {
				this.addTypeHandlers(searchDOM, DOMIO.loadDOMDoc( ClassHelper.loadFromDefaultClassLoader( "config/type-handler-default.xml") ).getDocumentElement() );
			} catch (Exception e1) {
				LogFacade.handleWarn( e1 );
			}
			this.addTypeHandlers(searchDOM, root);
			// doc handlers
			List<Element> docHandlerTagList = searchDOM.findAllTags( root , "doc-handler" );
			Iterator<Element> docHandlerTagIt = docHandlerTagList.iterator();
			while ( docHandlerTagIt.hasNext() ) {
				Element docHandlerTag = (Element) docHandlerTagIt.next();
				Properties atts = DOMUtils.attributesToProperties( docHandlerTag );
				String name = atts.getProperty( "name" );
				String type = atts.getProperty( "type" );
				String mode = atts.getProperty( "mode" );
				if ( mode == null || mode.equalsIgnoreCase( "" ) ) {
					mode = DocHandler.MODE_JSP;
				}
				String useJsp = atts.getProperty( "use-jsp", "true" );
				String jsp = atts.getProperty( "jsp", name );
				this.getLogger().info( "DocHandler : "+name+" -> "+type );
				DocHandler docHandler = null;
				if ( type != null ) {
					docHandler = (DocHandler)ClassHelper.newInstance( type );
				} else {
					docHandler = new DefaultDocHandler();
				}
				docHandler.init( docHandlerTag );
				docHandler.setJsp( jsp );
				docHandler.setUseJsp( "true".equals( useJsp ) );
				docHandler.setMode( mode );
				this.docHandlerMap.put( name , docHandler );
			}
			try {
				Element mimeTag = searchDOM.findTag( root , "mime-map" );
				if ( mimeTag != null ) {
					this.mime = new Properties( defMime );
					this.mime.putAll( DOMUtils.attributesToProperties( mimeTag ) );
					this.getLogger().info( "override mime : "+this.mime );
				} else {
					this.mime = defMime;
					this.getLogger().info( "default mime : "+this.mime );
				}
			} catch (Exception e1) {
				this.getLogger().warn( "error setting mime types", e1 );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ConfigContext getContext() {
		return context;
	}

	public void setContext(ConfigContext context) {
		this.context = context;
	}

	private HashMap<String, DocHandler> docHandlerMap;
	
	public HashMap<String, DocHandler> getDocHandlerMap() {
		return docHandlerMap;
	}

	public void setDocHandlerMap(HashMap<String, DocHandler> docHandlerMap) {
		this.docHandlerMap = docHandlerMap;
	}
	
	private String jspPath;
	
	private boolean skipFilter;
	
	private boolean debug;
	
	private Properties mime;
	
	private DocHelper docHelper;
	
	private String outMode;
	
	private String processingPage;

	private HashMap<String, DocTypeHandler> typeHandlerMap;
	
	private String errorManager;
	
	public String getJspPath() {
		return jspPath;
	}

	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Properties getMime() {
		return mime;
	}

	public void setMime(Properties mime) {
		this.mime = mime;
	}

	public DocHelper getDocHelper() {
		return docHelper;
	}

	public void setDocHelper(DocHelper docHelper) {
		this.docHelper = docHelper;
	}


	public String getOutMode() {
		return outMode;
	}

	public void setOutMode(String outMode) {
		this.outMode = outMode;
	}
	
}
