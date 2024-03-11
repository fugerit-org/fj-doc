package org.fugerit.java.doc.base.typehandler.asciidoc;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.helper.DocTypeFacadeDefault;
import org.fugerit.java.doc.base.helper.DocTypeFacadeHelper;
import org.fugerit.java.doc.base.model.*;

import java.io.PrintWriter;
import java.util.Date;

public class AsciidocFacade extends DocTypeFacadeDefault {

    private static final long serialVersionUID = -397084516L;

    private transient PrintWriter writer;

    protected PrintWriter getWriter() { return this.writer; }

    public AsciidocFacade(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void handleDoc(DocBase docBase) throws DocException {
        // actual document handling :we will treat only the body
        DocTypeFacadeHelper helper = new DocTypeFacadeHelper( docBase );
        this.handleElements( helper.getDocBase().getDocBody(), helper );
        // flush content
        this.getWriter().flush();
    }

    @Override
    public void handlePara(DocPara docPara, DocContainer parent, DocTypeFacadeHelper helper) throws DocException {
        super.handlePara(docPara, parent, helper);
    }

    @Override
    public void handlePhrase(DocPhrase docPhrase, DocContainer parent, DocTypeFacadeHelper helper) throws DocException {
        super.handlePhrase(docPhrase, parent, helper);
    }

    @Override
    public void handleList(DocList docList, DocContainer parent, DocTypeFacadeHelper helper) throws DocException {
        super.handleList(docList, parent, helper);
    }

    @Override
    public void handleImage(DocImage docImage, DocContainer parent, DocTypeFacadeHelper helper) throws DocException {
        super.handleImage(docImage, parent, helper);
    }

    @Override
    public void handleTable(DocTable docTable, DocContainer parent, DocTypeFacadeHelper helper) throws DocException {
        super.handleTable(docTable, parent, helper);
    }
}
