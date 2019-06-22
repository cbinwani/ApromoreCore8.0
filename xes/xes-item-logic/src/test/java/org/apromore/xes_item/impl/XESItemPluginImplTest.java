package org.apromore.xes_item.impl;

/*-
 * #%L
 * Apromore :: xes-item
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
import org.apromore.xes_item.XESItem;
import org.apromore.xes_item.XESItemService;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.spi.ItemPluginContext;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** Test suite for {@link XESItemPluginImpl}. */
public class XESItemPluginImplTest {

    @Before
    public void setup() {

    }

    /** Test {@link XESItemService#createXESItem} on an input source.
     *
     * @param filename  a path within the current classloader containing the test document
     * @return the created XES item for verification.
     */
    private XESItem testCreateXESItem(String filename) throws ItemFormatException, NotAuthorizedException {

        Item nakedItem = createMock(Item.class);
        expect(nakedItem.getId()).andReturn(0L);
        replay(nakedItem);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        expect(itemPluginContext.create(XESItem.TYPE)).andReturn(nakedItem);
        replay(itemPluginContext);

        XESItemService xesItemService = new XESItemPluginImpl();

        Source source = new StreamSource(XESItemPluginImplTest.class.getClassLoader().getResourceAsStream(filename));
        XESItem xesItem = xesItemService.createXESItem(source);

        verify(itemPluginContext);

        return xesItem;
    }

    /** Test {@link XESItemService#createXESItem} on an XES file. */
    @Ignore
    @Test
    public void testCreateXESItem_validXES() throws ItemFormatException, NotAuthorizedException {
        testCreateXESItem("repairExample_complete_lifecycle_only.xes");
    }

    /** Test {@link XESItemService#createXESItem} on non-XES file. */
    @Test(expected=ItemFormatException.class)
    public void testCreateXESItem_nonXES() throws ItemFormatException, NotAuthorizedException {
        testCreateXESItem("repairExample.bpmn");
    }

    /** Test {@link XESItemService#getById}. */
    @Ignore
    @Test
    public void testGetById() throws Exception {
        XESItemImpl expectedXESItem = createMock(XESItemImpl.class);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        XESItemService xesItemService = new XESItemPluginImpl();

        XESItem xesItem = xesItemService.getById(0L);
        assertEquals(expectedXESItem, xesItem);
    }
}