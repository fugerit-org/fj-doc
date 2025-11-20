package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Background represents the background element.
 *
 * This class will provide function to handle all background attributes and kids 
 */
class Background : HelperDSL.TagWithText( "background" ) {
    /**
     * Creates a new default Image instance.
     * @return the new instance.
     */
   fun image( text: String = "", init: Image.() -> Unit = {} ): Image {
       return initTag(Image(text), init);
   }


}
