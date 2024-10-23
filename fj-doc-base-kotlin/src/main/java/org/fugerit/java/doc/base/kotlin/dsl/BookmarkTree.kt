package org.fugerit.java.doc.base.kotlin.dsl

class BookmarkTree : HelperDSL.TagWithText( "bookmark-tree" ) {
    /**
     * Creates a new default Bookmark instance.
     * @return the new instance.
     */
   fun bookmark( text: String = "", init: Bookmark.() -> Unit = {} ): Bookmark {
       return initTag(Bookmark(text), init);
   }


}
