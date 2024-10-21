package org.fugerit.java.doc.base.kotlin.dsl

class Doc : HelperDSL.TagWithText( "doc" ) {
	init {
		att( "xmlns", "http://javacoredoc.fugerit.org" )
		att( "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" )
		att( "xsi:schemaLocation", "http://javacoredoc.fugerit.org http://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" )
	}
   fun body( init: Body.() -> Unit = {} ): Body {
       return initTag(Body(), init);
   }
   fun metadata( init: Metadata.() -> Unit = {} ): Metadata {
       return initTag(Metadata(), init);
   }
   fun meta( init: Meta.() -> Unit = {} ): Meta {
       return initTag(Meta(), init);
   }


}
