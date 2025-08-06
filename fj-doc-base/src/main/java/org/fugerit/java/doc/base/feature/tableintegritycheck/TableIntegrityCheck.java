package org.fugerit.java.doc.base.feature.tableintegritycheck;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.doc.base.feature.DocFeatureRuntimeException;
import org.fugerit.java.doc.base.feature.FeatureConfig;
import org.fugerit.java.doc.base.model.*;
import org.fugerit.java.doc.base.typehelper.generic.GenericConsts;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class TableIntegrityCheck {

    private TableIntegrityCheck() {}

    private static final Map<String, Consumer<DocTable>> DOC_CONSUMER_MAP = new HashMap<>();
    static {
        DOC_CONSUMER_MAP.put(TableIntegrityCheckConstants.TABLE_INTEGRITY_CHECK_DISABLED, doc -> log.debug("Table Integrity Check disabled") );
        DOC_CONSUMER_MAP.put(TableIntegrityCheckConstants.TABLE_INTEGRITY_CHECK_WARN, doc -> checkWorker( doc, result -> {} ) );
        DOC_CONSUMER_MAP.put(TableIntegrityCheckConstants.TABLE_INTEGRITY_CHECK_FAIL, doc -> checkWorker( doc, result -> {
            if ( result.getResultCode() != Result.RESULT_CODE_KO ) {
                throw new DocFeatureRuntimeException( "Table check integrity failed, see logs for details.", result.getResultCode(), result.getMessages() );
            }
        } ) );
    }

    private static int processCells( DocRow docRow, final int currentColParam, Map<Integer,Integer> rowSpanTracker  ) {
        int currentCol = currentColParam;
        for (DocElement cell : docRow.getElementList()) {
            DocCell docCell = (DocCell) cell;
            int colspan = Math.max(1, docCell.getColumnSpan());
            int rowspan = Math.max(1, docCell.getRowSpan());
            int startCol = currentCol;
            currentCol += colspan;
            // mark future row occupation
            if (rowspan > 1) {
                for (int c = startCol; c < startCol+colspan; c++) {
                    rowSpanTracker.put(c, rowSpanTracker.getOrDefault(c,0) + (rowspan-1));
                }
            }
        }
        return currentCol;
    }

    private static void validateRows(TableIntegrityCheckResult result, DocTable docTable, int columns, Map<Integer,Integer> rowSpanTracker ) {
        int rowIndex = 0;
        for (DocElement row : docTable.getElementList()) {
            DocRow docRow = (DocRow) row;
            int currentCol = 0;
            // consume row spans first
            while (currentCol < columns && rowSpanTracker.getOrDefault(currentCol, 0) > 0) {
                rowSpanTracker.put(currentCol, rowSpanTracker.get(currentCol) - 1);
                currentCol++;
            }
            // process actual row cells exactly once
            currentCol = processCells(docRow, currentCol, rowSpanTracker);
            if (currentCol != columns) {
                result.getMessages().add(
                        String.format("Row %s has %s columns instead of %s", rowIndex, currentCol, columns)
                );
            }
            rowIndex++;
        }
    }


    private static TableIntegrityCheckResult checkWorker(DocTable docTable, Consumer<TableIntegrityCheckResult> resultConsumer) {
        TableIntegrityCheckResult result = new TableIntegrityCheckResult(Result.RESULT_CODE_OK);
        int columns = docTable.getColumns();
        Map<Integer,Integer> rowSpanTracker = new HashMap<>();
        validateRows( result, docTable, columns, rowSpanTracker );
        for (Map.Entry<Integer,Integer> e : rowSpanTracker.entrySet()) {
            if (e.getValue() > 0) {
                result.getMessages().add( String.format( "Unfinished rowspan at column %s", e.getKey() ) );
            }
        }
        result.setResultCode( result.getMessages().size() );
        if ( result.getResultCode() == Result.RESULT_CODE_OK ) {
            log.debug( "Table Integrity Check OK" );
        } else {
            log.warn( "Table Integrity Check FAILED : {}", result.getResultCode() );
            result.getMessages().forEach(log::warn);
            resultConsumer.accept( result );
        }
        return result;
    }

    public static void apply(DocBase docBase, DocTable docTable, FeatureConfig featureConfig) {
        String tableCheckIntegrityInfo = StringUtils.valueWithDefault(
                docBase.getStableInfoSafe().getProperty( GenericConsts.DOC_TABLE_CHECK_INTEGRITY ),
                featureConfig.getTableCheckIntegrity() );
        Consumer<DocTable> consumer =  DOC_CONSUMER_MAP.get( tableCheckIntegrityInfo );
        consumer.accept( docTable );
    }

}
