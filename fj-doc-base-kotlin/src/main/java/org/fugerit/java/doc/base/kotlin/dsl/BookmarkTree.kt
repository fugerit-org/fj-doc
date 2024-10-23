package org.fugerit.java.doc.base.kotlin.dsl

/**
 * BookmarkTree represents the bookmark-tree element.
 *
 * This class will provide function to handle all bookmark-tree attributes and kids 
 */
class BookmarkTree : HelperDSL.TagWithText( "bookmark-tree" ) {
    /**
     * Creates a new default Bookmark instance.
     * @return the new instance.
     */
   fun bookmark( text: String = "", init: Bookmark.() -> Unit = {} ): Bookmark {
       return initTag(Bookmark(text), init);
   }


}
