package org.fugerit.java.doc.base.kotlin.dsl

class H( text: String = "" ) : HelperDSL.TagWithText( "h" ) {

   init { setText(text) }

   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }

   fun phrase( text: String = "", init: Phrase.() -> Unit = {} ): Phrase {
       return initTag(Phrase(text), init);
   }
   fun para( text: String = "", init: Para.() -> Unit = {} ): Para {
       return initTag(Para(text), init);
   }
   fun h( text: String = "", init: H.() -> Unit = {} ): H {
       return initTag(H(text), init);
   }

   fun id( value: String ): H = idType( this, "id", value )
   fun style( value: String ): H = styleType( this, "style", value )
   fun align( value: String ): H = alignType( this, "align", value )
   fun fontName( value: String ): H = fontNameType( this, "font-name", value )
   fun leading( value: Int ): H = leadingType( this, "leading", value )
   fun backColor( value: String ): H = colorType( this, "back-color", value )
   fun foreColor( value: String ): H = colorType( this, "fore-color", value )
   fun type( value: String ): H = dataType( this, "type", value )
   fun format( value: String ): H = formatType( this, "format", value )
   fun size( value: Int ): H = fontSizeType( this, "size", value )
   fun textIndent( value: Int ): H = textIndentType( this, "text-indent", value )
   fun spaceBefore( value: Int ): H = spaceType( this, "space-before", value )
   fun spaceAfter( value: Int ): H = spaceType( this, "space-after", value )
   fun spaceLeft( value: Int ): H = spaceType( this, "space-left", value )
   fun spaceRight( value: Int ): H = spaceType( this, "space-right", value )
   fun whiteSpaceCollapse( value: Boolean ): H = whiteSpaceCollapsType( this, "white-space-collapse", value )
   fun headLevel( value: Int ): H = headLevelType( this, "head-level", value )

}
