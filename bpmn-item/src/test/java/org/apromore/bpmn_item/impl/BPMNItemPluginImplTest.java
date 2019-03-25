package org.apromore.bpmn_item.impl;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.bpmn_item.BPMNItemService;
import org.apromore.bpmn_item.jpa.BPMNItemRepository;
import org.apromore.item.Item;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.service.bpmndiagramimporter.BPMNDiagramImporter;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** Test suite for {@link BPMNItemPluginImpl}. */
public class BPMNItemPluginImplTest {

    /** Initialize the instance to be tested. */
    @Before
    public void setup() throws Exception {

        //bpmnDiagramImporter.setItemPluginContext(itemPluginContext);

    }

    /** Test {@link BPMNItemService#createBPMNItem}. */
    @Test
    public void testCreateBPMNItem() throws Exception {
        Item nakedItem = createMock(Item.class);
        expect(nakedItem.getId()).andReturn(0L);
        expect(nakedItem.getId()).andReturn(0L);
        replay(nakedItem);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        expect(itemPluginContext.create(BPMNItem.TYPE)).andReturn(nakedItem);
        replay(itemPluginContext);

        BPMNItemRepository bpmnItemRepository = createMock(BPMNItemRepository.class);
        BPMNDiagramImporter bpmnDiagramImporter = createMock(BPMNDiagramImporter.class);

        BPMNItemPluginImpl instance = new BPMNItemPluginImpl();
        instance.setBpmnItemRepository(bpmnItemRepository);
        instance.setBPMNDiagramImporter(bpmnDiagramImporter);
        instance.setItemPluginContext(itemPluginContext);

        Source source = new StreamSource(BPMNItemPluginImplTest.class.getClassLoader().getResourceAsStream("test.bpmn"));
        BPMNItem bpmnItem = instance.createBPMNItem(source);
    }

    /** Test {@link BPMNItemService#getById}. */
    @Test(expected = NullPointerException.class)
    public void testGetById() throws Exception {
        Item nakedItem = createMock(Item.class);
        expect(nakedItem.getId()).andReturn(0L);
        //expect(nakedItem.getId()).andReturn(0L);
        replay(nakedItem);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        expect(itemPluginContext.getById(0L)).andReturn(nakedItem);
        replay(itemPluginContext);

        BPMNItemRepository bpmnItemRepository = createMock(BPMNItemRepository.class);
        BPMNDiagramImporter bpmnDiagramImporter = createMock(BPMNDiagramImporter.class);

        BPMNItemPluginImpl instance = new BPMNItemPluginImpl();
        instance.setBpmnItemRepository(bpmnItemRepository);
        instance.setBPMNDiagramImporter(bpmnDiagramImporter);
        instance.setItemPluginContext(itemPluginContext);

        assertNull(instance.getById(0L));
    }
}
