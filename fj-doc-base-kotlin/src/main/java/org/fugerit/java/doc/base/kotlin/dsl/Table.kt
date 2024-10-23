package org.fugerit.java.doc.base.kotlin.dsl

/**
 * Table represents the table element.
 *
 * This class will provide function to handle all table attributes and kids 
 */
class Table : HelperDSL.TagWithText( "table" ) {
    /**
     * Creates a new default Row instance.
     * @return the new instance.
     */
   fun row( init: Row.() -> Unit = {} ): Row {
       return initTag(Row(), init);
   }

    /**
     * Function handling id attribute of the Table with specific check on type.
     * @return the value for the id attribute.
     */
   fun id( value: String ): Table = idType( this, "id", value )
    /**
     * Function handling columns attribute of the Table with specific check on type.
     * @return the value for the columns attribute.
     */
   fun columns( value: Int ): Table = columnsType( this, "columns", value )
    /**
     * Function handling width attribute of the Table with specific check on type.
     * @return the value for the width attribute.
     */
   fun width( value: Int ): Table = percentageType( this, "width", value )
    /**
     * Function handling backColor attribute of the Table with specific check on type.
     * @return the value for the backColor attribute.
     */
   fun backColor( value: String ): Table = colorType( this, "back-color", value )
    /**
     * Function handling foreColor attribute of the Table with specific check on type.
     * @return the value for the foreColor attribute.
     */
   fun foreColor( value: String ): Table = colorType( this, "fore-color", value )
    /**
     * Function handling spacing attribute of the Table with specific check on type.
     * @return the value for the spacing attribute.
     */
   fun spacing( value: Int ): Table = spaceType( this, "spacing", value )
    /**
     * Function handling padding attribute of the Table with specific check on type.
     * @return the value for the padding attribute.
     */
   fun padding( value: Int ): Table = spaceType( this, "padding", value )
    /**
     * Function handling colwidths attribute of the Table with generic check on type.
     * @return the value for the colwidths attribute.
     */
   fun colwidths( value: String ): Table = setAtt( this, "colwidths", value )
    /**
     * Function handling spaceBefore attribute of the Table with specific check on type.
     * @return the value for the spaceBefore attribute.
     */
   fun spaceBefore( value: Int ): Table = spaceType( this, "space-before", value )
    /**
     * Function handling spaceAfter attribute of the Table with specific check on type.
     * @return the value for the spaceAfter attribute.
     */
   fun spaceAfter( value: Int ): Table = spaceType( this, "space-after", value )
    /**
     * Function handling renderMode attribute of the Table with specific check on type.
     * @return the value for the renderMode attribute.
     */
   fun renderMode( value: String ): Table = tableRenderModeType( this, "render-mode", value )
    /**
     * Function handling caption attribute of the Table with generic check on type.
     * @return the value for the caption attribute.
     */
   fun caption( value: String ): Table = setAtt( this, "caption", value )

}
