<#import 'macro/DocHelperMacro.ftl' as dhm>
// generated from template '${templatePath}' on ${generationTime?string.iso}
package test.${context.docConfigPackage};

import ${context.docConfigPackage}.DocHelper;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
<#if context.preVersion862 >
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
</#if>

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
<#if context.addLombok >
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import lombok.AllArgsConstructor;
</#if>

<@dhm.createExampleJavadoc context=context junit=true/>
<#if context.addLombok >@Slf4j</#if>
class DocHelperTest {

    @Test
    void testDocProcess() {
        <@dhm.createExampleBody context=context junit=true className='DocHelperTest'/>
    }

    <@dhm.createExampleModel context=context/>

}
