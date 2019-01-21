package org.apromore.bpmn_item.impl;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.bpmn_item.BPMNItemService;
import org.apromore.bpmn_item.jpa.BPMNItemRepository;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.service.bpmndiagramimporter.BPMNDiagramImporter;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** Test suite for {@link BPMNItemPluginImpl}. */
public class BPMNItemPluginImplTest {

    /** Instance to be tested. */
    private BPMNItemPluginImpl instance;

    /** Initialize the instance to be tested. */
    @Before
    public void setup() {
        BPMNItemRepository bpmnItemRepository = createMock(BPMNItemRepository.class);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);

        BPMNDiagramImporter bpmnDiagramImporter = createMock(BPMNDiagramImporter.class);
        //bpmnDiagramImporter.setItemPluginContext(itemPluginContext);

        instance = new BPMNItemPluginImpl();
        instance.setBpmnItemRepository(bpmnItemRepository);
    }

    /** Test {@link BPMNItemService#createBPMNItem}. */
    @Test
    public void testCreateBPMNItem() throws Exception {
        Source source = new StreamSource(BPMNItemPluginImplTest.class.getClassLoader().getResourceAsStream("test.bpmn"));
        //BPMNItem bpmnItem = bpmnItemService.createBPMNItem(source);
    }

    /** Test {@link BPMNItemService#getById}. */
    @Test
    public void testGetById() throws Exception {
        BPMNItemImpl expectedBPMNItem = createMock(BPMNItemImpl.class);

        //BPMNItem bpmnItem = bpmnItemService.getById(0L);
        //assertEquals(expectedBPMNItem, bpmnItem);
    }
}
