package org.fugerit.java.doc.base.kotlin.dsl

class Phrase( text: String = "" ) : HelperDSL.TagWithText( "phrase" ) {

   init { setText(text) }

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }


   fun id( value: String ): Phrase = idType( this, "id", value )
   fun fontName( value: String ): Phrase = fontNameType( this, "font-name", value )
   fun style( value: String ): Phrase = styleType( this, "style", value )
   fun leading( value: Int ): Phrase = leadingType( this, "leading", value )
   fun size( value: Int ): Phrase = fontSizeType( this, "size", value )
   fun link( value: String ): Phrase = setAtt( this, "link", value )
   fun whiteSpaceCollapse( value: Boolean ): Phrase = whiteSpaceCollapsType( this, "white-space-collapse", value )
   fun anchor( value: String ): Phrase = setAtt( this, "anchor", value )

}
