package org.fugerit.java.doc.base.kotlin.dsl

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

   fun align( value: String ): Header = alignType( this, "align", value )
   fun numbered( value: Boolean ): Header = setAtt( this, "numbered", value )
   fun borderWidth( value: Int ): Header = borderWidthType( this, "border-width", value )
   fun expectedSize( value: Int ): Header = setAtt( this, "expected-size", value )

}
