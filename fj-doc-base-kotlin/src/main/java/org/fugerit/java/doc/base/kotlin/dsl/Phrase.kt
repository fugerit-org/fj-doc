package org.fugerit.java.doc.base.kotlin.dsl

class Phrase( text: String = "" ) : HelperDSL.TagWithText( "phrase" ) {

   init { setText(text) }

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }


    /**
     * Function handling id attribute of the Phrase with specific check on type.
     * @return the value for the id attribute.
     */
   fun id( value: String ): Phrase = idType( this, "id", value )
    /**
     * Function handling fontName attribute of the Phrase with specific check on type.
     * @return the value for the fontName attribute.
     */
   fun fontName( value: String ): Phrase = fontNameType( this, "font-name", value )
    /**
     * Function handling style attribute of the Phrase with specific check on type.
     * @return the value for the style attribute.
     */
   fun style( value: String ): Phrase = styleType( this, "style", value )
    /**
     * Function handling leading attribute of the Phrase with specific check on type.
     * @return the value for the leading attribute.
     */
   fun leading( value: Int ): Phrase = leadingType( this, "leading", value )
    /**
     * Function handling size attribute of the Phrase with specific check on type.
     * @return the value for the size attribute.
     */
   fun size( value: Int ): Phrase = fontSizeType( this, "size", value )
    /**
     * Function handling link attribute of the Phrase with generic check on type.
     * @return the value for the link attribute.
     */
   fun link( value: String ): Phrase = setAtt( this, "link", value )
    /**
     * Function handling whiteSpaceCollapse attribute of the Phrase with specific check on type.
     * @return the value for the whiteSpaceCollapse attribute.
     */
   fun whiteSpaceCollapse( value: Boolean ): Phrase = whiteSpaceCollapsType( this, "white-space-collapse", value )
    /**
     * Function handling anchor attribute of the Phrase with generic check on type.
     * @return the value for the anchor attribute.
     */
   fun anchor( value: String ): Phrase = setAtt( this, "anchor", value )

}
