import org.fugerit.java.doc.base.kotlin.dsl.dslDoc

dslDoc {
    metadata {
        info( ( "DSL Kotlin Document From JUnit 2 with coverage" ) ).name( "doc-title" )
        headerExt {
            h( "title 2" )
            para("test header")
            align( "center" )
            borderWidth( 1 );
            table { row { cell { para("cell 1") }  } }.columns( 1 ).colwidths( "100" ).width( 100 )
            image().type( "png" ).base64( "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" )
            expectedSize( 100 )
            phrase("a")
            barcode { text( "a" ) }
        }
        footerExt {
            h( "title 2" )
            para("test footer")
            align( "center" )
            borderWidth( 1 );
            table { row { cell { para("cell 1") }  } }.columns( 1 ).colwidths( "100" ).width( 100 )
            image().align( "center" ).alt( "avatar" ).url( "https://avatars.githubusercontent.com/u/37816284?s=96&v=4" ).scaling( 80 )
            expectedSize( 100 )
            phrase("a")
            barcode { text( "a" ) }
        }
        bookmarkTree {
            bookmark( "Bookmark 1" ).ref( "bookmark1" )
        }
        background().image().align( "center" ).alt( "avatar" ).url( "https://avatars.githubusercontent.com/u/37816284?s=96&v=4" )
    }
    body {
        h( "head level 1" ).headLevel( 1 ).align( "center" ).id( "bookmark1" )
        h( "head level 2" ).headLevel( 2 ).id( "second title" ).style( "italic" ).fontName( "Arial" ).size( 10 ).format( "{}" )
            .backColor( "#000000" ).foreColor( "#ffffff" ).type( "number" ).textIndent( 1 ).whiteSpaceCollapse( true )
            .spaceLeft( 1 ).spaceAfter( 1 ).spaceBefore( 1 ).spaceRight( 1 )
        para( "Concise paragraph" )
            .align( "center" )
            .style( "bold" )
            .whiteSpaceCollapse( true )
            .spaceLeft( 10 )
            .leading( 1 )
            .format( "{}" )
        para {
            para( "test 1 para" )
            phrase( "test 1 phrase" )
            h( "head level 4 a" ).headLevel( 4 )
            setText("Verbose paragraph")
            align( "left" )
            style( "italic" )
            whiteSpaceCollapse( true )
            spaceAfter( 10 )
            foreColor( "#000000" )
            backColor( "#ffffff" )
        }
        h {
            setText("head level 3")
            headLevel( 3 )
            para( "test 2 para" )
            phrase( "test 2 phrase" )
            h( "head level 4 b" ).headLevel( 4 )
        }
        phrase( "Inline phrase" ).id( "ele 3" ).style( "normal" ).size( 8 ).fontName( "Arial" )
            .leading( 3 ).link( "#a" ).anchor( "a" ).whiteSpaceCollapse( true )
        table {
            row {
                cell { para( "col 1" )  }
                cell { para( "col 2" )  }
                cell { para( "col 3" )  }
            }.header( true ).id( "headerRow" )
            row {
                cell { para( "data 1" )  }.borderColorBottom( "#000000" ).borderColorLeft( "#000000" ).borderColorRight( "#000000" ).borderColorTop( "#ffffff" )
                cell { para( "data 2 and 3" ).style( "italic" )  }.colspan( 2 ).rowspan( 1 ).align( "center" )
            }.id( "firstRow" )
            row {
                cell { nbsp { length( 1 ) } }.valign( "middle" ).backColor( "#000000" ).foreColor( "#ffffff" )
                cell { phrase( "test" ) }.borderColorTop( "#000000" ).borderColorLeft( "#000000" ).borderColorRight( "#000000" ).borderColorBottom( "#000000" )
                cell { br {} }.borderWidth( 1 )
            }
            row {
                cell { barcode().text( "test"  ) }.header( true ).id( "cellid1" )
                cell {
                    h( "test").headLevel(5)
                    image().align( "center" ).alt( "avatar" ).url( "https://avatars.githubusercontent.com/u/37816284?s=96&v=4" ).scaling( 80 )
                }
                cell { table { row { cell { para("cell 1") }  } }.columns( 1 ).colwidths( "100" ).width( 100 ) }
            }
        }.id( "table-1" ).width( 100 ).columns( 3 ).colwidths( "50;50;50" ).backColor( "#ffddee" ).foreColor( "#556677" )
            .spacing( 1 ).padding( 2 ).spaceAfter( 5 ).spaceAfter( 5 ).renderMode( "normal" ).caption( "test caption" )
        barcode().size( 100 ).type( "EAN" )
        br()
        pageBreak()
        list {
            li { para("test 1") }
            li { pl { phrase("test 2") } }
            li { list { li { para("test 3") } } }
            li { h("h 4").headLevel( 3 ) }
        }.id("list1").listType("ul")
        nbsp().length( 2 )
    }
}