package org.fugerit.java.doc.playground.catalog;

import org.fugerit.java.doc.playground.facade.BasicOutput;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
public class CatalogOutput extends BasicOutput {

    @Getter
    @Setter
    private String docOutput;

    @Getter
    @Setter
    private String jsonData;

}
