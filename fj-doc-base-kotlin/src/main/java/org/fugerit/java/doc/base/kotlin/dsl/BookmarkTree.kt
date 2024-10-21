package org.fugerit.java.doc.base.kotlin.dsl

class BookmarkTree : HelperDSL.TagWithText( "bookmark-tree" ) {
   fun bookmark( text: String = "", init: Bookmark.() -> Unit = {} ): Bookmark {
       return initTag(Bookmark(text), init);
   }


}
