package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Header represents the header element.
 *
 * This class will provide function to handle all header attributes and kids 
 */
class Header : HelperDSL.TagWithText( "header" ) {
    /**
     * Creates a new default Para instance.
     * @return the new instance.
     */
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
    /**
     * Creates a new default Image instance.
     * @return the new instance.
     */
   fun image( init: Image.() -> Unit = {} ): Image {
       return initTag(Image(), init);
   }
    /**
     * Creates a new default Phrase instance.
     * @return the new instance.
     */
   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }
    /**
     * Creates a new default H instance.
     * @return the new instance.
     */
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }

    /**
     * Function handling align attribute of the Header with specific check on type.
     * @return the value for the align attribute.
     */
   fun align( value: String ): Header = alignType( this, "align", value )
    /**
     * Function handling numbered attribute of the Header with generic check on type.
     * @return the value for the numbered attribute.
     */
   fun numbered( value: Boolean ): Header = setAtt( this, "numbered", value )
    /**
     * Function handling borderWidth attribute of the Header with specific check on type.
     * @return the value for the borderWidth attribute.
     */
   fun borderWidth( value: Int ): Header = borderWidthType( this, "border-width", value )
    /**
     * Function handling expectedSize attribute of the Header with generic check on type.
     * @return the value for the expectedSize attribute.
     */
   fun expectedSize( value: Int ): Header = setAtt( this, "expected-size", value )

}
