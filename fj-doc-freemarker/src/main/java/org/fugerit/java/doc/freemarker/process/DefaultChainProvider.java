package org.fugerit.java.doc.freemarker.process;

import org.fugerit.java.core.util.filterchain.MiniFilterChain;

public interface DefaultChainProvider {

	public MiniFilterChain newDefaultChain( String id );
	
}
