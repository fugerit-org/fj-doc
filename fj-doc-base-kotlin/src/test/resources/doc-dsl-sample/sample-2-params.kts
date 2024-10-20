import org.fugerit.java.doc.base.kotlin.dsl.dslDoc

dslDoc {
    meta {
        info( ( "DSL Kotlin Document From JUnit 2 with parameters" ) ).name( "doc-title" )
    }
    body {
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
                cell { para( "col 1" )  }
                cell { para( "col 2" )  }
            }.header( true )
            row {
                cell { para( "data 1" )  }
                cell { para { + "data 2 unary" } }
            }
        }.width( 100 ).columns( 2 ).colwidths( "50;50" )
        para( attStr( attMap( data, "testMap" ), "nestedKey" ) )
        table {
            row {
                cell { para( "Name" ) }.align( "center" )
                cell { para( "Surname" ) }.align( "center" )
                cell { para( "Title" ) }.align( "center" )
            }.header( true )
            attListMap( data, "listPeople" ).forEach( { e -> row {
                cell { para( attStr( e, "name" ) ) }
                cell { para( attStr( e, "surname" ) ) }
                cell { para( attStr( e, "title" ) ) }
            } } )
            attList( data, "listPeople" ).forEach( { e -> row {
                cell { para( "$e.name" ) }
                cell { para( "$e.surname" ) }
                cell { para( "$e.title" ) }
            } } )
        }.columns( 3 ).colwidths( "30;30;40" ).width( 100 ).id( "data-table" ).padding( 2 )
        list {
            attList( data, "testList" ).forEach { e -> li { para( "$e" )} }
        }
    }
}