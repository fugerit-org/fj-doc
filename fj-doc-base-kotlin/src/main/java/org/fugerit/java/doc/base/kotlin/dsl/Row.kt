package org.fugerit.java.doc.base.kotlin.dsl

class Row : HelperDSL.TagWithText( "row" ) {
    /**
     * Creates a new default Cell instance.
     * @return the new instance.
     */
   fun cell( init: Cell.() -> Unit = {} ): Cell {
       return initTag(Cell(), init);
   }

    /**
     * Function handling id attribute of the Row with specific check on type.
     * @return the value for the id attribute.
     */
   fun id( value: String ): Row = idType( this, "id", value )
    /**
     * Function handling header attribute of the Row with generic check on type.
     * @return the value for the header attribute.
     */
   fun header( value: Boolean ): Row = setAtt( this, "header", value )

}
