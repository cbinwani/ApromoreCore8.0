package org.apromore.bpmn_item.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.bpmn_item.jpa.BPMNItemDAO;
import org.apromore.item.Item;
import org.apromore.item.spi.ItemWrapper;
import org.apromore.service.bpmndiagramimporter.BPMNDiagramImporter;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;

/**
 * A BPMN 2.0 process model as a concrete {@link Item}.
 *
 */
class BPMNItemImpl extends ItemWrapper implements BPMNItem {

    private Long id;

    private byte[] xmlSerialization;

    transient BPMNDiagramImporter importerService;

    BPMNItemImpl(BPMNItemDAO dao) {
        this.id = dao.getId();
        this.xmlSerialization = dao.getXmlSerialization();
    }


    // Implementation of BPMNItem

    public BPMNDiagram getBPMNDiagram() {
        try {
            return importerService.importBPMNDiagram(new String(xmlSerialization, Charset.forName("UTF-8")));

        } catch (Exception e) {
            throw new Error("Internal error: validated BPMN 2.0 failed to re-parse", e);
        }
    }


    // Accessors

    /** @return primary key */
    public Long getId() {
        return id;
    }

    public void setId(Long newId) {
        this.id = newId;
    }

    public InputStream getXMLSerialization() {
        return new ByteArrayInputStream(xmlSerialization);
    }

    public void setXMLSerialization(byte[] newXMLSerialization) {
        this.xmlSerialization = newXMLSerialization;
    }
}
