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

    private byte[] xmlSerialization;

    private BPMNDiagramImporter importerService;

    BPMNItemImpl(final Item                item,
                 final BPMNItemDAO         dao,
                 final BPMNDiagramImporter newImporterService) {
        super(item);

        assert item.getId() == dao.getId();
        this.xmlSerialization = dao.getXmlSerialization();
        this.importerService = newImporterService;
    }


    // Implementation of BPMNItem

    public BPMNDiagram getBPMNDiagram() {
        try {
            return importerService.importBPMNDiagram(
                new String(xmlSerialization, Charset.forName("UTF-8")));

        } catch (Exception e) {
            throw new Error("Internal error: validated BPMN 2.0 failed to "
                + "re-parse", e);
        }
    }


    // Accessors

    public InputStream getXMLSerialization() {
        return new ByteArrayInputStream(xmlSerialization);
    }
}
