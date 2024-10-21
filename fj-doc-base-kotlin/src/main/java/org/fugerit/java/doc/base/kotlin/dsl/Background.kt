package org.fugerit.java.doc.base.kotlin.dsl

class Background : HelperDSL.TagWithText( "background" ) {
   fun image( init: Image.() -> Unit = {} ): Image {
       return initTag(Image(), init);
   }


}
