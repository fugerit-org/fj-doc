package org.fugerit.java.doc.base.kotlin.dsl

class FooterExt : HelperDSL.TagWithText( "footer-ext" ) {
    /**
     * Creates a new default Para instance.
     * @return the new instance.
     */
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
    /**
     * Creates a new default Table instance.
     * @return the new instance.
     */
   fun table( init: Table.() -> Unit = {} ): Table {
       return initTag(Table(), init);
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
     * Creates a new default Barcode instance.
     * @return the new instance.
     */
   fun barcode( init: Barcode.() -> Unit = {} ): Barcode {
       return initTag(Barcode(), init);
   }
    /**
     * Creates a new default H instance.
     * @return the new instance.
     */
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }

   fun align( value: String ): FooterExt = alignType( this, "align", value )
   fun numbered( value: Boolean ): FooterExt = setAtt( this, "numbered", value )
   fun borderWidth( value: Int ): FooterExt = borderWidthType( this, "border-width", value )
   fun expectedSize( value: Int ): FooterExt = setAtt( this, "expected-size", value )

}
