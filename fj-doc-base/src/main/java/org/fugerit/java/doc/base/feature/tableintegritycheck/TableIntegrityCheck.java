package org.fugerit.java.doc.base.feature.tableintegritycheck;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.result.BasicResult;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class TableIntegrityCheck {

    public static final String TABLE_INTEGRITY_CHECK_DISABLED = "disabled";
    public static final String TABLE_INTEGRITY_CHECK_FAIL = "fail";
    public static final String TABLE_INTEGRITY_CHECK_WARN = "warn";
    public static final String TABLE_INTEGRITY_CHECK_FAIL_CONDITIONAL = "fail-conditional";
    public static final String TABLE_INTEGRITY_CHECK_DEFAULT = TABLE_INTEGRITY_CHECK_WARN;

    private TableIntegrityCheck() {}

    private static final Map<String, Consumer<DocBase>> DOC_CONSUMER_MAP = new HashMap<>();
    static {
        DOC_CONSUMER_MAP.put(TABLE_INTEGRITY_CHECK_DISABLED, doc -> log.debug("Table Integrity Check disabled") );
        DOC_CONSUMER_MAP.put(TABLE_INTEGRITY_CHECK_WARN, doc -> checkWorker( doc, result -> {} ) );
        DOC_CONSUMER_MAP.put(TABLE_INTEGRITY_CHECK_FAIL, doc -> checkWorker( doc, result -> {
            if ( result.getResultCode() != BasicResult.RESULT_CODE_KO ) {
                throw new ConfigRuntimeException( "Table check integrity failed, see logs for details." );
            }
        } ) );
        DOC_CONSUMER_MAP.put(TABLE_INTEGRITY_CHECK_FAIL_CONDITIONAL, doc -> checkWorker( doc, result -> {
            if ( result.getResultCode() != BasicResult.RESULT_CODE_KO ) {
                throw new ConfigRuntimeException( "Table check integrity failed, see logs for details." );
            }
        } ) );
    }

    private static TableIntegrityCheckResult checkWorker(DocBase docBase, Consumer<TableIntegrityCheckResult> resultConsumer) {
        TableIntegrityCheckResult result = new TableIntegrityCheckResult(BasicResult.RESULT_CODE_OK);
        if ( result.getResultCode() == BasicResult.RESULT_CODE_OK ) {
            log.debug( "Table Integrity Check OK" );
        } else {
            log.warn( "Table Integrity Check FAILED : {}", result.getResultCode() );
            result.getMessages().forEach(log::warn);
            resultConsumer.accept( result );
        }
        return result;
    }

    public static void apply(DocBase docBase, String tableCheckIntegrityGlobal) {
        String tableCheckIntegrityInfo = StringUtils.valueWithDefault(
                docBase.getStableInfo().getProperty( GenericConsts.DOC_TABLE_CHECK_INTEGRITY ),
                tableCheckIntegrityGlobal );
        Consumer<DocBase> consumer =  DOC_CONSUMER_MAP.get( tableCheckIntegrityInfo );
        consumer.accept( docBase );
    }

}
