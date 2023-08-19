package org.fugerit.java.doc.mod.fop.config;

/**
 * @deprecated planned for removal in version 1.6 (see https://github.com/fugerit-org/fj-doc/issues/7)
 */
@Deprecated
public class FopConfigClassLoader extends FopConfigClassLoaderWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 188843074194800812L;
	
	public static final String MIN_VERSION_NEW_CLASSLOADER_MODE = "[0.5.2](https://github.com/fugerit-org/fj-doc/issues/7)";
	
	/**
	 * Provided only for compatibility
	 *
	 * @param fopConfigPath
	 * @param defaultFontPath
	 * 
	 * @deprecated use instaed the other constructors or the xml configuration, see [0.5.2](https://github.com/fugerit-org/fj-doc/issues/7)
	 */
	@Deprecated
	public FopConfigClassLoader(String fopConfigPath, String defaultFontPath) {
		super( fopConfigPath, new ClassLoaderResourceResolver( defaultFontPath ) );
	}

}
