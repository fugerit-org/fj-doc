package org.fugerit.java.doc.base.kotlin.dsl

class Footer : HelperDSL.TagWithText( "footer" ) {
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
     * Function handling align attribute of the Footer with specific check on type.
     * @return the value for the align attribute.
     */
   fun align( value: String ): Footer = alignType( this, "align", value )
    /**
     * Function handling numbered attribute of the Footer with generic check on type.
     * @return the value for the numbered attribute.
     */
   fun numbered( value: Boolean ): Footer = setAtt( this, "numbered", value )
    /**
     * Function handling borderWidth attribute of the Footer with specific check on type.
     * @return the value for the borderWidth attribute.
     */
   fun borderWidth( value: Int ): Footer = borderWidthType( this, "border-width", value )
    /**
     * Function handling expectedSize attribute of the Footer with generic check on type.
     * @return the value for the expectedSize attribute.
     */
   fun expectedSize( value: Int ): Footer = setAtt( this, "expected-size", value )

}
