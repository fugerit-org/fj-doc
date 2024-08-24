package org.fugerit.java.doc.freemarker.tool.verify;

import lombok.*;

@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class VerifyTemplateInfo {

    @Getter
    @NonNull
    private Integer resultCode;

    @Getter
    @NonNull
    private String templateId;

    @Getter
    private Exception exception;

}
