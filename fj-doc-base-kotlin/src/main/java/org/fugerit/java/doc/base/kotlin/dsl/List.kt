package org.fugerit.java.doc.base.kotlin.dsl

class List : HelperDSL.TagWithText( "list" ) {
    /**
     * Creates a new default Li instance.
     * @return the new instance.
     */
   fun li( init: Li.() -> Unit = {} ): Li {
       return initTag(Li(), init);
   }

   fun id( value: String ): List = idType( this, "id", value )
   fun listType( value: String ): List = listType( this, "list-type", value )

}
