package org.fugerit.java.doc.base.kotlin.dsl

/**
 * HeaderExt represents the header-ext element.
 *
 * This class will provide function to handle all header-ext attributes and kids 
 */
class HeaderExt : HelperDSL.TagWithText( "header-ext" ) {
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

    /**
     * Function handling align attribute of the HeaderExt with specific check on type.
     * @return the value for the align attribute.
     */
   fun align( value: String ): HeaderExt = alignType( this, "align", value )
    /**
     * Function handling numbered attribute of the HeaderExt with generic check on type.
     * @return the value for the numbered attribute.
     */
   fun numbered( value: Boolean ): HeaderExt = setAtt( this, "numbered", value )
    /**
     * Function handling borderWidth attribute of the HeaderExt with specific check on type.
     * @return the value for the borderWidth attribute.
     */
   fun borderWidth( value: Int ): HeaderExt = borderWidthType( this, "border-width", value )
    /**
     * Function handling expectedSize attribute of the HeaderExt with generic check on type.
     * @return the value for the expectedSize attribute.
     */
   fun expectedSize( value: Int ): HeaderExt = setAtt( this, "expected-size", value )

}
