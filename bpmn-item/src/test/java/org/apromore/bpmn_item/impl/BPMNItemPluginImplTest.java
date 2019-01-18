package org.apromore.bpmn_item.impl;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.bpmn_item.BPMNItemService;
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

/** Test suite for {@link BpmnItemPluginImpl}. */
public class BPMNItemPluginImplTest {

    @Before
    public void setup() {

    }

    /** Test {@link BPMNItemService#createBPMNItem}. */
    @Ignore
    @Test
    public void testCreateBPMNItem() throws Exception {
        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);

        BPMNDiagramImporter bpmnDiagramImporter = createMock(BPMNDiagramImporter.class);

        BPMNItemService bpmnItemService = new BPMNItemPluginImpl();

        Source source = new StreamSource(BPMNItemPluginImplTest.class.getClassLoader().getResourceAsStream("test.bpmn"));
        //BPMNItem bpmnItem = bpmnItemService.createBPMNItem(source);
    }

    /** Test {@link BPMNItemService#getById}. */
    @Ignore
    @Test
    public void testGetById() throws Exception {
        BPMNItemImpl expectedBPMNItem = createMock(BPMNItemImpl.class);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        BPMNDiagramImporter bpmnDiagramImporter = createMock(BPMNDiagramImporter.class);
        BPMNItemService bpmnItemService = new BPMNItemPluginImpl();

        BPMNItem bpmnItem = bpmnItemService.getById(0L);
        assertEquals(expectedBPMNItem, bpmnItem);
    }
}
