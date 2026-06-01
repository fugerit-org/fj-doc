package test.org.fugerit.java.doc.freemarker.fun;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModelException;
import org.fugerit.java.doc.freemarker.fun.CleanTextFun;
import org.fugerit.java.doc.freemarker.fun.FMFunHelper;
import org.fugerit.java.doc.freemarker.fun.FormatLocalDateTimeFun;
import org.fugerit.java.doc.freemarker.fun.TextWrapFun;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TestFMFunctions {

    // ---- FMFunHelper tests ----

    @Test
    void testCheckFirstRequiredThrowsWhenEmpty() {
        Assertions.assertThrows( TemplateModelException.class,
                () -> FMFunHelper.checkFirstRequired( new ArrayList<>() ) );
    }

    @Test
    void testCheckFirstRequiredPassesWithOneArg() throws TemplateModelException {
        FMFunHelper.checkFirstRequired( Collections.singletonList( new SimpleScalar( "arg" ) ) );
    }

    @Test
    void testCheckParameterNumberThrowsWhenTooFew() {
        Assertions.assertThrows( TemplateModelException.class,
                () -> FMFunHelper.checkParameterNumber( Collections.singletonList( new SimpleScalar( "x" ) ), 2 ) );
    }

    @Test
    void testCheckParameterNumberPassesWithEnoughArgs() throws TemplateModelException {
        List<Object> args = Arrays.asList( new SimpleScalar( "a" ), new SimpleScalar( "b" ) );
        FMFunHelper.checkParameterNumber( args, 2 );
    }

    @Test
    void testCheckFirstRequiredWithCustomMessage() {
        TemplateModelException ex = Assertions.assertThrows( TemplateModelException.class,
                () -> FMFunHelper.checkFirstRequiredWithMessage( new ArrayList<>(), "custom message" ) );
        Assertions.assertTrue( ex.getMessage().contains( "custom message" ) );
    }

    // ---- CleanTextFun tests ----

    @Test
    void testCleanTextFunReplacesPattern() throws TemplateModelException {
        CleanTextFun fun = new CleanTextFun();
        List<Object> args = Arrays.asList( new SimpleScalar( "Hello World!" ), new SimpleScalar( "[^a-zA-Z ]" ) );
        Object result = fun.exec( args );
        Assertions.assertInstanceOf( SimpleScalar.class, result );
        String value = ((SimpleScalar) result).getAsString();
        Assertions.assertEquals( "Hello World", value );
    }

    @Test
    void testCleanTextFunWithNoReplacement() throws TemplateModelException {
        CleanTextFun fun = new CleanTextFun();
        List<Object> args = Arrays.asList( new SimpleScalar( "Hello" ), new SimpleScalar( "[0-9]" ) );
        Object result = fun.exec( args );
        Assertions.assertEquals( "Hello", ((SimpleScalar) result).getAsString() );
    }

    @Test
    void testCleanTextFunThrowsWithTooFewArgs() {
        CleanTextFun fun = new CleanTextFun();
        Assertions.assertThrows( TemplateModelException.class,
                () -> fun.exec( Collections.singletonList( new SimpleScalar( "text" ) ) ) );
    }

    @Test
    void testCleanTextFunDefaultNameConstant() {
        Assertions.assertEquals( "cleanText", CleanTextFun.DEFAULT_NAME );
    }

    // ---- TextWrapFun tests ----

    @Test
    void testTextWrapFunWithNormalText() throws TemplateModelException {
        TextWrapFun fun = new TextWrapFun();
        List<Object> args = Collections.singletonList( new SimpleScalar( "Hello" ) );
        Object result = fun.exec( args );
        Assertions.assertInstanceOf( SimpleScalar.class, result );
        Assertions.assertNotNull( ((SimpleScalar) result).getAsString() );
    }

    @Test
    void testTextWrapFunThrowsWithNoArgs() {
        TextWrapFun fun = new TextWrapFun();
        Assertions.assertThrows( TemplateModelException.class,
                () -> fun.exec( new ArrayList<>() ) );
    }

    @Test
    void testTextWrapFunDefaultNameConstant() {
        Assertions.assertEquals( "textWrap", TextWrapFun.DEFAULT_NAME );
    }

    // ---- FormatLocalDateTimeFun tests ----

    @Test
    void testFormatLocalDateTimeFunFormatsDate() throws TemplateModelException {
        FormatLocalDateTimeFun fun = new FormatLocalDateTimeFun();
        LocalDateTime dateTime = LocalDateTime.of( 2024, 1, 15, 10, 30, 0 );
        freemarker.ext.beans.BeansWrapper wrapper = new freemarker.ext.beans.BeansWrapper(
                freemarker.template.Configuration.VERSION_2_3_32 );
        freemarker.ext.beans.GenericObjectModel dateModel =
                (freemarker.ext.beans.GenericObjectModel) wrapper.wrap( dateTime );
        List<Object> args = Arrays.asList( dateModel, new SimpleScalar( "yyyy-MM-dd" ) );
        Object result = fun.exec( args );
        Assertions.assertInstanceOf( SimpleScalar.class, result );
        Assertions.assertEquals( "2024-01-15", ((SimpleScalar) result).getAsString() );
    }

    @Test
    void testFormatLocalDateTimeFunThrowsWithTooFewArgs() {
        FormatLocalDateTimeFun fun = new FormatLocalDateTimeFun();
        Assertions.assertThrows( TemplateModelException.class,
                () -> fun.exec( Collections.singletonList( new SimpleScalar( "arg" ) ) ) );
    }

    @Test
    void testFormatLocalDateTimeFunDefaultNameConstant() {
        Assertions.assertEquals( "formatDateTime", FormatLocalDateTimeFun.DEFAULT_NAME );
    }
}
