package org.fugerit.java.doc.base.kotlin.dsl

class Nbsp : HelperDSL.TagWithText( "nbsp" ) {

    /**
     * Function handling length attribute of the Nbsp with specific check on type.
     * @return the value for the length attribute.
     */
   fun length( value: Int ): Nbsp = lengthType( this, "length", value )

}
