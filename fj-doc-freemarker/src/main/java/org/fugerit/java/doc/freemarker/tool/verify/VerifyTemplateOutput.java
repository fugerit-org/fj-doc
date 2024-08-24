package org.fugerit.java.doc.freemarker.tool.verify;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.util.result.Result;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class VerifyTemplateOutput {

    @Getter @Setter
    private FileFilter fileFilter = f -> Boolean.TRUE;

    @Getter
    private List<VerifyTemplateInfo> infos;

    public VerifyTemplateOutput() {
        this.infos = new ArrayList<>();
    }

    /**
     * General result code for this output
     *
     * @return  0 (OK) if all the template check is OK, ad different value if at least one template check failed
     */
    public int getResultCode() {
        for ( VerifyTemplateInfo info : this.infos ) {
            if ( info.getResultCode() != Result.RESULT_CODE_OK ) {
                return Result.RESULT_CODE_KO;
            }
        }
        return Result.RESULT_CODE_OK;
    }

    public List<VerifyTemplateInfo> getErrors() {
        return this.infos.stream().filter( info -> info.getResultCode() != Result.RESULT_CODE_OK ).collect(Collectors.toList());
    }

    public List<String> getErrorsTemplateIds() {
        return this.getErrors().stream().map( VerifyTemplateInfo::getTemplateId ).collect(Collectors.toList());
    }

}
