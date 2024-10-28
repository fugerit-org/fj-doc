import org.fugerit.java.doc.base.kotlin.dsl.dslDoc
<#assign defaultTitle="My sample title Kotlin + FreeMarker">
dslDoc {
  val docTitle = "${docTitle!defaultTitle}";
  meta {
    info( ( docTitle ) ).name( "doc-title" )
    info( ( "10;10;10;30" ) ).name( "margins" )
    info( ( "fj doc venus sample source Kotlin Template - kts" ) ).name( "doc-subject" )
    info( ( "fugerit79" ) ).name( "dock-author" )
    info( ( "en" ) ).name( "doc-language" )
    info( ( "TitilliumWeb" ) ).name( "default-font-name" )
    info( ( "data-table=print" ) ).name( "excel-table-id" )
    info( ( "data-table" ) ).name( "csv-table-id" )
    footerExt {
      para( '$'+"{currentPage} / "+'$'+"{pageCount}" ).align( "right" )
    }
  }
  body {
    h( docTitle ).headLevel( 1 )
    table {
      row {
        cell { para( "Name" ) }.align( "center" )
        cell { para( "Surname" ) }.align( "center" )
        cell { para( "Title" ) }.align( "center" )
      }.header( true )
    <#if listPeople??>
      <#list listPeople as current>
        row {
          cell { para( "${current.name}" ) }.align( "center" )
          cell { para( "${current.surname}" ) }.align( "center" )
          cell { para( "${current.title}" ) }.align( "center" )
        }
      </#list>
    </#if>
    }.columns( 3 ).colwidths( "30;30;40" ).width( 100 ).id( "data-table" ).padding( 2 )
  }
}