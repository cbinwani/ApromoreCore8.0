package org.apromore.bpmn_item.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apromore.bpmn_item.BPMNItem;
import org.apromore.bpmn_item.BPMNItemService;
import org.apromore.bpmn_item.jpa.BpmnItemDao;
import org.apromore.bpmn_item.jpa.BpmnItemRepository;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
import org.apromore.service.bpmndiagramimporter.BPMNDiagramImporter;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;

class BPMNItemPluginImpl implements BPMNItemService, ItemPlugin<BPMNItem> {

    private BpmnItemRepository  bpmnItemRepository;
    private ItemPluginContext   itemPluginContext;
    private BPMNDiagramImporter importerService;

    BPMNItemPluginImpl(ItemPluginContext itemPluginContext, BPMNDiagramImporter importerService) {
        if (itemPluginContext == null) {
            throw new IllegalArgumentException("Item plugin context missing");
        }
        if (importerService == null) {
            throw new IllegalArgumentException("BPMN diagram importer service missing");
        }

        this.itemPluginContext = itemPluginContext;
        this.importerService   = importerService;
    }

    public void setBpmnItemRepository(BpmnItemRepository newRepository) {
        this.bpmnItemRepository = newRepository;
    }

    // ItemPlugin implementation

    public BPMNItem create(InputStream inputStream) throws ItemFormatException, NotAuthorizedException {
        return createBPMNItem(new StreamSource(inputStream));
    }

    public String getType() {
        return "BPMN 2.0";  // TODO: make this a constant
    }

    public BPMNItem toConcreteItem(Item item) throws ItemTypeException {
        BpmnItemDao dao = bpmnItemRepository.get(item.getId());
        if (dao == null) {
             throw new ItemTypeException(getType(), item.getType());
        }
        BPMNItemImpl bpmnItemImpl = new BPMNItemImpl(dao);
        bpmnItemImpl.item = item;
        bpmnItemImpl.importerService = this.importerService;
        return bpmnItemImpl;
    }

    // BPMNItemService implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public BPMNItem createBPMNItem(Source source) throws ItemFormatException, NotAuthorizedException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(baos));

            // Validate
            try {
                BPMNDiagram validated = importerService.importBPMNDiagram(baos.toString("UTF-8"));
            } catch (Exception e) {
                throw new ItemFormatException(getType(), e);
            }

            Item item = this.itemPluginContext.create(getType());

            BpmnItemDao dao = new BpmnItemDao();
            dao.setId(item.getId());
            dao.setXmlSerialization(baos.toByteArray());
            bpmnItemRepository.add(dao);

            BPMNItemImpl bpmnItem = new BPMNItemImpl(dao);
            bpmnItem.item            = item;
            bpmnItem.importerService = this.importerService;

            return bpmnItem;

        } catch (TransformerConfigurationException e) {
            throw new Error("Server configuration error", e);

        } catch (TransformerException e) {
            throw new ItemFormatException(getType(), e);
        }
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public BPMNItem getById(Long id) throws NotAuthorizedException {
        BpmnItemDao dao = bpmnItemRepository.get(id);
        BPMNItemImpl bpmnItemImpl = new BPMNItemImpl(dao);
        bpmnItemImpl.item            = this.itemPluginContext.getById(id);
        bpmnItemImpl.importerService = this.importerService;
        return bpmnItemImpl;
    }
}
