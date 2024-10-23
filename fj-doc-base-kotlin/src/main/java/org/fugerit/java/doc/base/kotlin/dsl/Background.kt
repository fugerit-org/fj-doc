package org.fugerit.java.doc.base.kotlin.dsl

class Background : HelperDSL.TagWithText( "background" ) {
    /**
     * Creates a new default Image instance.
     * @return the new instance.
     */
   fun image( init: Image.() -> Unit = {} ): Image {
       return initTag(Image(), init);
   }


}
