import org.fugerit.java.doc.base.kotlin.dsl.dslDoc

dslDoc {
    meta {
        info( ( "DSL Kotlin Document From JUnit 2" ) ).name( "doc-title" )
        info {}
        headerExt().h { setText( "header 2" ) }
        footerExt().h { setText( "footer 2" ) }
        bookmarkTree()
    }
    body {
        image()
        table()
        list()
        phrase()
        para( "Concise paragraph" )
            .align( "center" )
            .style( "bold" )
            .whiteSpaceCollapse( true )
            .spaceLeft( 10 )
            .leading( 1 )
            .format( "{}" )
        para {
            setText("Verbose paragraph")
            align( "left" )
            style( "italic" )
            whiteSpaceCollapse( true )
            spaceAfter( 10 )
            foreColor( "#000000" )
            backColor( "#ffffff" )
        }
        phrase( "Inline phrase" ).style( "normal" ).size( 8 )
        table {
            row {
                cell { table {}  }
                cell { table()  }
            }.header( true )
            row {
                cell { nbsp()  }
                cell { br()  }
            }
            row {
                cell { phrase()  }
                cell { h()  }
            }
        }.width( 100 ).columns( 2 ).colwidths( "50;50" )
    }
}