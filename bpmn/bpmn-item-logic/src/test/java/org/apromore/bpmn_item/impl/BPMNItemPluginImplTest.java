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

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apromore.Caller;
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
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** Test suite for {@link BPMNItemPluginImpl}. */
public class BPMNItemPluginImplTest {

    private Caller caller = new org.apromore.AbstractCaller();

    /** Initialize the instance to be tested. */
    @Before
    public void setup() throws Exception {

        //bpmnDiagramImporter.setItemPluginContext(itemPluginContext);
    }

    /** Test {@link BPMNItemService#createBPMNItem}. */
    @Test
    public void testCreateBPMNItem() throws Exception {
        Item nakedItem = createMock(Item.class);
        assert nakedItem != null: "@AssumeAssertion(nullness)";
        expect(nakedItem.getId()).andReturn(0L);
        expect(nakedItem.getId()).andReturn(0L);
        replay(nakedItem);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        assert itemPluginContext != null: "@AssumeAssertion(nullness)";
        expect(itemPluginContext.create(BPMNItem.TYPE, caller)).andReturn(nakedItem);
        replay(itemPluginContext);

        BPMNItemRepository bpmnItemRepository = createMock(BPMNItemRepository.class);
        assert bpmnItemRepository != null: "@AssumeAssertion(nullness)";
        BPMNDiagramImporter bpmnDiagramImporter = createMock(BPMNDiagramImporter.class);
        assert bpmnDiagramImporter != null: "@AssumeAssertion(nullness)";

        BPMNItemPluginImpl instance = new BPMNItemPluginImpl();
        instance.setBpmnItemRepository(bpmnItemRepository);
        instance.setBPMNDiagramImporter(bpmnDiagramImporter);
        instance.setItemPluginContext(itemPluginContext);

        Source source = new StreamSource(BPMNItemPluginImplTest.class.getClassLoader().getResourceAsStream("test.bpmn"));
        BPMNItem bpmnItem = instance.createBPMNItem(source, caller);
    }

    /** Test {@link BPMNItemService#getById}. */
    @Test
    public void testGetById() throws Exception {
        Item nakedItem = createMock(Item.class);
        assert nakedItem != null: "@AssumeAssertion(nullness)";
        expect(nakedItem.getId()).andReturn(0L);
        //expect(nakedItem.getId()).andReturn(0L);
        replay(nakedItem);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        assert itemPluginContext != null: "@AssumeAssertion(nullness)";
        expect(itemPluginContext.getById(0L, caller)).andReturn(nakedItem);
        replay(itemPluginContext);

        BPMNItemRepository bpmnItemRepository = createMock(BPMNItemRepository.class);
        assert bpmnItemRepository != null: "@AssumeAssertion(nullness)";
        BPMNDiagramImporter bpmnDiagramImporter = createMock(BPMNDiagramImporter.class);
        assert bpmnDiagramImporter != null: "@AssumeAssertion(nullness)";

        BPMNItemPluginImpl instance = new BPMNItemPluginImpl();
        instance.setBpmnItemRepository(bpmnItemRepository);
        instance.setBPMNDiagramImporter(bpmnDiagramImporter);
        instance.setItemPluginContext(itemPluginContext);

        assertTrue(instance.getById(0L, caller) == null);
    }
}
