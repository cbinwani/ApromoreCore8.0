package org.apromore.bpmn_item.impl;

/*-
 * #%L
 * Apromore :: bpmn-item
 * %%
 * Copyright (C) 2018 - 2019 The Apromore Initiative
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

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

    /** The serialized BPMN 2.0 document. */
    private byte[] xmlSerialization;

    /** Used by {@link #getBPMNDiagram()}. */
    private BPMNDiagramImporter importerService;

    /**
     * @param item  a partially-constructed item from
     *     {@link org.apromore.item.jpa.ItemRepository}
     * @param dao  data access object for BPMN-specific fields
     * @param newImporterService  used by {@link #getBPMNDiagram()}
     */
    BPMNItemImpl(final Item                item,
                 final BPMNItemDAO         dao,
                 final BPMNDiagramImporter newImporterService) {
        super(item);

        assert item.getId() == dao.getId();
        this.xmlSerialization = dao.getXmlSerialization();
        this.importerService = newImporterService;
    }


    // Implementation of BPMNItem

    @Override
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

    @Override
    public InputStream getXMLSerialization() {
        return new ByteArrayInputStream(xmlSerialization);
    }
}
