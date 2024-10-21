package org.fugerit.java.doc.base.kotlin.dsl

class Table : HelperDSL.TagWithText( "table" ) {
   fun row( init: Row.() -> Unit = {} ): Row {
       return initTag(Row(), init);
   }

   fun id( value: String ): Table = idType( this, "id", value )
   fun columns( value: Int ): Table = columnsType( this, "columns", value )
   fun width( value: Int ): Table = percentageType( this, "width", value )
   fun backColor( value: String ): Table = colorType( this, "back-color", value )
   fun foreColor( value: String ): Table = colorType( this, "fore-color", value )
   fun spacing( value: Int ): Table = spaceType( this, "spacing", value )
   fun padding( value: Int ): Table = spaceType( this, "padding", value )
   fun colwidths( value: String ): Table = setAtt( this, "colwidths", value )
   fun spaceBefore( value: Int ): Table = spaceType( this, "space-before", value )
   fun spaceAfter( value: Int ): Table = spaceType( this, "space-after", value )
   fun renderMode( value: String ): Table = tableRenderModeType( this, "render-mode", value )
   fun caption( value: String ): Table = setAtt( this, "caption", value )

}
