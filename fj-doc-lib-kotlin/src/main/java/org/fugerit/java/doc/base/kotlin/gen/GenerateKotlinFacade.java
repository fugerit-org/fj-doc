package org.fugerit.java.doc.base.kotlin.gen;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.lib.autodoc.parser.model.*;
import org.xmlet.xsdparser.xsdelements.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class GenerateKotlinFacade {

    private static final String PACKAGE = "package ";

    private static final String ENDLINE = "\n";

    private static final String FUN = "   fun ";

    private GenerateKotlinFacade() {}

    private static void handleRoot(GenerateKotlinConfig config, AutodocElement element ) throws IOException {
        log.info( "rootElement : {}", element.getName() );
        String fileName = config.getProperty( GenerateKotlinConfig.CONFIG_MAIN_FUN )+".kt";
        String fileNameImport = config.getProperty( GenerateKotlinConfig.CONFIG_MAIN_FUN )+"Kt";
        String rootName = config.toKotlinClass( element.getName() );
        String content = "@file:JvmName(\""+fileNameImport+"\")\n" +
                ENDLINE +
                PACKAGE+config.getProperty( GenerateKotlinConfig.CONFIG_PACKAGE )+ENDLINE +
                ENDLINE +
                "fun "+ config.getProperty( GenerateKotlinConfig.CONFIG_MAIN_FUN )+"(block: "+rootName+".() -> Unit): "+rootName+" =\n" +
                "    "+rootName+"().apply(block)";
        File dslFile = new File( config.getPackageFolder(), fileName );
        FileIO.writeString( content, dslFile );
    }

    private static void checkRestrictions1( XsdSimpleType simpleType, SimpleValue<Integer> min, SimpleValue<Integer> max ) {
        simpleType.getAllRestrictions().forEach( r -> {
            if ( r.getMinLength() != null ) min.setValue( r.getMinLength().getValue() );
            if ( r.getMaxLength() != null ) max.setValue( r.getMaxLength().getValue() );
            if ( r.getMinExclusive() != null ) min.setValue( Integer.valueOf( r.getMinExclusive().getValue() )+1 );
            if ( r.getMinInclusive() != null ) min.setValue( Integer.valueOf( r.getMinInclusive().getValue() ) );
            if ( r.getMaxExclusive() != null ) max.setValue( Integer.valueOf( r.getMaxExclusive().getValue() )-1 );
            if ( r.getMaxInclusive() != null ) max.setValue( Integer.valueOf( r.getMaxInclusive().getValue() ) );
        } );
    }

    private static void checkRestrictions2( XsdSimpleType simpleType, List<String> valueEnum, SimpleValue<String> regex ) {
        simpleType.getAllRestrictions().forEach( r -> {
            if ( r.getEnumeration() != null ) r.getEnumeration().forEach( e -> valueEnum.add( "\""+e.getValue()+"\"" ) );
            if ( r.getPattern() != null && r.getPattern().getValue() != null ) regex.setValue( r.getPattern().getValue() );
        } );
    }

    private static String createCheck( String typeFun, XsdSimpleType simpleType ) {
        String checkFun = "";
        SimpleValue<Integer> min = new SimpleValue<>(-1);
        SimpleValue<Integer> max = new SimpleValue<>(-1);
        List<String> valueEnum = new ArrayList<>();
        SimpleValue<String> regex = new SimpleValue<>( null );
        checkRestrictions1( simpleType, min, max );
        checkRestrictions2( simpleType, valueEnum, regex );
        if ( regex.getValue() != null ) {
            checkFun = " { v -> v.matches(Regex(\""+regex.getValue()+"\")) }";
        } else if ( min.getValue() != -1 && max.getValue() != -1 ) {
            if ( typeFun.equals( "String" ) ) {
                checkFun = " { v -> v.length in "+min.getValue()+".."+max.getValue()+" }";
            } else if ( typeFun.equals( "Int" ) ) {
                checkFun = " { v -> v in "+min.getValue()+".."+max.getValue()+" }";
            }
        } else if ( !valueEnum.isEmpty() ) {
            checkFun = " { v -> setOf( " + StringUtils.concat( ", " , valueEnum ) + " ).contains( v ) }";
        }
        return checkFun;
    }

    private static void recurseElements(GenerateKotlinConfig config, final StringBuilder builder, Collection<XsdElement> elements ) {
        elements.forEach( e -> {
            boolean isTextElementSub = isTextElement( e.getXsdComplexType() );
            String kotlinClass = config.toKotlinClass( e.getRawName() );
            String kotlinFun = config.toKotlinFun( e.getRawName() );
            String textConstructor = isTextElementSub ? " text: String = \"\"," : "";
            builder.append( "    /**\n" +
                    "     * Creates a new default "+kotlinClass+" instance.\n" +
                    "     * @return the new instance.\n" +
                    "     */\n" );
            builder.append( FUN+kotlinFun+"("+textConstructor+" init: "+kotlinClass+".() -> Unit = {} ): "+kotlinClass+" {\n" );
            builder.append( "       return initTag("+kotlinClass+"(" +(isTextElementSub ? "text" : "")+ "), init);\n" );
            builder.append( "   }\n" );
        } );
    }

    private static <T extends XsdMultipleElements> void recurseElements(GenerateKotlinConfig config, final StringBuilder builder, AutodocMulti<T> holder) {
        if ( holder != null ) {
            if ( holder.getXsdElements() != null ) recurseElements( config, builder, holder.getXsdElements() );
            if ( holder.getAutodocSequence() != null ) holder.getAutodocSequence().forEach( c -> recurseElements( config, builder, c.getXsdElements() ) );
            if ( holder.getAutodocChoices() != null ) holder.getAutodocChoices().forEach( c -> recurseElements( config, builder, c.getXsdElements() ) );
        }
    }

    private static String findTypeFun( String baseName ) {
        String typeFun = "String";
        if ( baseName.contains( "int" ) || baseName.contains( "decimal" ) ) {
            typeFun = "Int";
        } else if ( baseName.contains( "boolean" ) ) {
            typeFun = "Boolean";
        }
        return typeFun;
    }

    private static String handleElementFun( GenerateKotlinConfig config, AutodocElement element, boolean isTextElement ) {
        final StringBuilder builder = new StringBuilder();
        // set text for text element
        if ( isTextElement ) {
            builder.append( "\n   fun setText( value: String ) { addKid( HelperDSL.TextElement( value ) ) }\n\n" );
        }
        // functions for elements
        recurseElements( config, builder, element.getAutodocType().getSequence() );
        recurseElements( config, builder, element.getAutodocType().getChoice() );
        builder.append( ENDLINE );
        // functions for attributes
        String kotlinClassElement = config.toKotlinClass( element.getName() );
        element.getXsdAttributes().forEach( a -> {
            String xmlType = a.getType();
            String checkFunName = config.toCheckTypeFun( xmlType );
            String kotlinkFun = config.toKotlinFun( a.getRawName() );
            String baseName = a.getXsdSimpleType() != null ? AutodocUtils.getBaseName( a.getXsdSimpleType().getAllRestrictions(), config.getAutodocModel() ) : a.getType() ;
            String typeFun = findTypeFun( baseName );
            log.info( "type name : {}, basename : {}", a.getRawName(), baseName );
            String checkFun = a.getXsdSimpleType() != null ? createCheck( typeFun, a.getXsdSimpleType() ) : "";
            log.info( "element : {}, attribute : {}, type : {} : xml type : {}, checkFunName : {}", element.getName(), a.getRawName(), typeFun, xmlType, checkFunName );
            if ( xmlType.contains( config.getAutodocModel().getAutodocPrefix() ) )  {
                String checkFunRef = "";
                if ( StringUtils.isNotEmpty( checkFun ) ) {
                    String fullCheckFun = "(v: "+typeFun+") -> Boolean = "+checkFun;
                    if ( !config.getCheckFun().contains( fullCheckFun ) ) {
                        config.getCheckFun().add( fullCheckFun );
                    }
                    int index = config.getCheckFun().indexOf( fullCheckFun );
                    checkFunRef = ", checkFun"+index;
                }
                String extraFun = "protected fun <T : Element> "+checkFunName+"Type( tag : T, name : String, v: "+typeFun+") : T = setAtt( tag, name, v"+checkFunRef+" ) ";
                config.getExtraFun().add( extraFun );
                builder.append( "    /**\n" +
                        "     * Function handling "+kotlinkFun+" attribute of the "+kotlinClassElement+" with specific check on type.\n" +
                        "     * @return the value for the "+kotlinkFun+" attribute.\n" +
                        "     */\n" );
                builder.append( FUN+kotlinkFun+"( value: "+typeFun+" ): "+kotlinClassElement+" = "+checkFunName+"Type( this, \""+a.getRawName()+"\", value )\n" );
            } else {
                builder.append( "    /**\n" +
                        "     * Function handling "+kotlinkFun+" attribute of the "+kotlinClassElement+" with generic check on type.\n" +
                        "     * @return the value for the "+kotlinkFun+" attribute.\n" +
                        "     */\n" );
                builder.append( FUN+kotlinkFun+"( value: "+typeFun+" ): "+kotlinClassElement+" = setAtt( this, \""+a.getRawName()+"\", value )"+checkFun+ENDLINE );
            }
        } );
        return builder.toString();
    }

    private static boolean isTextElement( XsdComplexType complexType ) {
        return complexType != null && complexType.isMixed();
    }

    private static void handleElement( GenerateKotlinConfig config, AutodocElement element ) {
        SafeFunction.apply( () -> {
            // element handling
            boolean isTextElement = isTextElement( element.getComplexType() );
            String kotlinClass = config.toKotlinClass( element.getName() );
            String textConstructor = isTextElement ? "( text: String = \"\" )" : "";
            String textInit = isTextElement ? "\n   init { setText(text) }\n" : "";
            log.info( "handleElement : {}", element.getName() );
            // root handling
            if ( element.getName().equals( config.getProperty( GenerateKotlinConfig.CONFIG_ROOT_ELEMENT ) ) ) {
                handleRoot( config, element );
                textInit = config.getProperty( GenerateKotlinConfig.CONFIG_ROOT_INIT, textInit );
            }
            String content = PACKAGE+config.getProperty( GenerateKotlinConfig.CONFIG_PACKAGE )+ENDLINE +
                    ENDLINE +
                    "class "+kotlinClass+textConstructor+" : HelperDSL.TagWithText( \""+element.getName()+"\" ) {\n" +
                    textInit +
                    handleElementFun( config, element, isTextElement ) + ENDLINE +
                    "}\n";
            String fileName = kotlinClass+".kt";
            File tagFile = new File( config.getPackageFolder(), fileName );
            FileIO.writeString( content, tagFile );
        } );
    }

    public static void generate( GenerateKotlinConfig config ) {
        final StringBuilder extraFun = new StringBuilder();
        if ( !config.getPackageFolder().exists() ) {
            config.getPackageFolder().mkdirs();
        }
        config.getAutodocModel().getElements().forEach( e ->  handleElement( config, e ) );
        SafeFunction.apply( () -> {
            try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "generate-kotlin/helper-dsl.txt" )) {
                for ( int k=0; k<config.getCheckFun().size(); k++ ) {
                    extraFun.append( "\t\tprivate var checkFun"+k+" : "+config.getCheckFun().get( k )+ENDLINE );
                }
                config.getExtraFun().forEach( f -> extraFun.append( "\t\t"+f+ENDLINE ) );
                String helperDslContent = PACKAGE+config.getProperty( GenerateKotlinConfig.CONFIG_PACKAGE )+ENDLINE
                        + StreamIO.readString( is ).replace( "EXTRA-FUN", extraFun.toString() );
                File helperFile = new File( config.getPackageFolder(), "HelperDSL.kt" );
                FileIO.writeString( helperDslContent, helperFile );
            }
        } );
    }

}
