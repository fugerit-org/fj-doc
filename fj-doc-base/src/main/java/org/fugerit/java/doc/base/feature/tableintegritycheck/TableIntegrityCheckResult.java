package org.fugerit.java.doc.base.feature.tableintegritycheck;

import lombok.Getter;
import lombok.Setter;
import org.fugerit.java.core.util.result.BasicResult;

import java.util.ArrayList;
import java.util.List;

public class TableIntegrityCheckResult extends BasicResult {

    public TableIntegrityCheckResult(int resultCode) {
        super(resultCode);
        this.messages = new ArrayList<>();
    }

    @Getter @Setter
    private List<String> messages;

}
