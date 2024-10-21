package org.fugerit.java.doc.base.kotlin.dsl

class List : HelperDSL.TagWithText( "list" ) {
   fun li( init: Li.() -> Unit = {} ): Li {
       return initTag(Li(), init);
   }

   fun id( value: String ): List = idType( this, "id", value )
   fun listType( value: String ): List = listType( this, "list-type", value )

}
