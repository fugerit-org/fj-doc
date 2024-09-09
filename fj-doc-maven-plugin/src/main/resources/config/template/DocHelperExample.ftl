<#import 'macro/DocHelperMacro.ftl' as dhm>
// generated from template '${templatePath}' on ${generationTime?string.iso}
package ${context.docConfigPackage};

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
<#if context.preVersion862 >
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
</#if>

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
<#if context.addLombok >
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import lombok.AllArgsConstructor;
</#if>

<@dhm.createExampleJavadoc context=context junit=false/>
public class DocHelperExample {

    public static void main(String[] args) {
        <@dhm.createExampleBody context=context junit=false className='DocHelperExample'/>
    }

}
