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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.transaction.Transactional;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apromore.bpmn_item.BPMNItem;
import org.apromore.bpmn_item.BPMNItemService;
import org.apromore.bpmn_item.jpa.BPMNItemDAO;
import org.apromore.bpmn_item.jpa.BPMNItemRepository;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apromore.service.bpmndiagramimporter.BPMNDiagramImporter;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;

/**
 * Both an {@link BPMNItemService} and an {@link ItemPlugin} providing
 * BPMN 2.0 support.
 */
@Component(service = {BPMNItemService.class, ItemPlugin.class})
public final class BPMNItemPluginImpl implements BPMNItemService,
    ItemPlugin<BPMNItem> {

    // OSGi services

    /** Factory service for BPMN-specific data access objects. */
    private BPMNItemRepository  bpmnItemRepository;

    /** Utility service for {@link ItemPlugin}s. */
    private ItemPluginContext   itemPluginContext;

    /**
     * Service used by created {@link BPMNItem}s to evaluate their
     * {@link BPMNItem#getBPMNDiagram bpmnDiagram} property.
     */
    private BPMNDiagramImporter importerService;

    /**
     * OSGi service bind handler.
     *
     * @param repository  factory service for BPMN-specific data access objects
     */
    @Reference
    public void setBpmnItemRepository(final BPMNItemRepository repository) {
        this.bpmnItemRepository = repository;
    }

    /**
     * OSGi service bind handler.
     *
     * @param context  newly-bound service
     */
    @Reference
    public void setItemPluginContext(final ItemPluginContext context) {
        this.itemPluginContext = context;
    }

    /**
     * OSGi service bind handler.
     *
     * @param importer  newly-bound service
     */
    @Reference
    public void setBPMNDiagramImporter(final BPMNDiagramImporter importer) {
        this.importerService = importer;
    }

    // ItemPlugin implementation

    @Override
    public BPMNItem create(final InputStream inputStream)
        throws ItemFormatException, NotAuthorizedException {

        return createBPMNItem(new StreamSource(inputStream));
    }

    @Override
    public String getType() {
        return BPMNItem.TYPE;
    }

    @Override
    public BPMNItem toConcreteItem(final Item item)
        throws ItemTypeException {

        BPMNItemDAO dao = bpmnItemRepository.get(item.getId());
        if (dao == null) {
             throw new ItemTypeException(getType(), item.getType());
        }
        return new BPMNItemImpl(item, dao, this.importerService);
    }

    // BPMNItemService implementation

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BPMNItem createBPMNItem(final Source source)
        throws ItemFormatException, NotAuthorizedException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TransformerFactory.newInstance()
                              .newTransformer()
                              .transform(source, new StreamResult(baos));

            // Validate
            try {
                BPMNDiagram validated =
                    importerService.importBPMNDiagram(baos.toString("UTF-8"));

            } catch (Exception e) {
                throw new ItemFormatException(getType(), e);
            }

            Item item = this.itemPluginContext.create(getType());

            BPMNItemDAO dao = new BPMNItemDAO();
            dao.setId(item.getId());
            dao.setXmlSerialization(baos.toByteArray());
            bpmnItemRepository.add(dao);

            return new BPMNItemImpl(item, dao, this.importerService);

        } catch (TransformerConfigurationException e) {
            throw new Error("Server configuration error", e);

        } catch (TransformerException e) {
            throw new ItemFormatException(getType(), e);
        }
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public BPMNItem getById(final Long id) throws NotAuthorizedException {
        Item        item = this.itemPluginContext.getById(id);
        BPMNItemDAO dao  = bpmnItemRepository.get(id);
        return new BPMNItemImpl(item, dao, this.importerService);
    }
}
