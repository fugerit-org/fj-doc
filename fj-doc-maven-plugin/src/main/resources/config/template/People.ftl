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

/*
* Class used to wrap data to be rendered in the document template
*/
<#if context.addLombok >@Getter
@AllArgsConstructor
</#if>
public class People {

    private String name;

    private String surname;

    private String title;
<#if !context.addLombok >
    public People(String name, String surname, String title) {
        this.name = name;
        this.surname = surname;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTitle() {
        return title;
    }
</#if>

}