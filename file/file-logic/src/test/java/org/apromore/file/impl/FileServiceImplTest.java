package org.apromore.file.impl;

/*-
 * #%L
 * Apromore :: file :: file-logic
 * %%
 * Copyright (C) 2019 The Apromore Initiative
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

import java.io.InputStream;
import org.apromore.file.File;
import org.apromore.file.FileService;
import org.apromore.file.jpa.FileRepository;
import org.apromore.item.Item;
import org.apromore.item.spi.ItemPluginContext;
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

/** Test suite for {@link FileServiceImpl}. */
public class FileServiceImplTest {

    /** Initialize the instance to be tested. */
    @Before
    public void setup() throws Exception {
        // null implementation
    }

    /** Test {@link FileService#createFile}. */
    @Test @Ignore
    public void testCreateFile() throws Exception {
        Item nakedItem = createMock(Item.class);
        expect(nakedItem.getId()).andReturn(0L);
        expect(nakedItem.getId()).andReturn(0L);
        replay(nakedItem);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        expect(itemPluginContext.create(File.TYPE)).andReturn(nakedItem);
        replay(itemPluginContext);

        FileRepository fileRepository = createMock(FileRepository.class);

        FileServiceImpl instance = new FileServiceImpl();
        instance.setItemPluginContext(itemPluginContext);

        InputStream in = FileServiceImplTest.class.getClassLoader().getResourceAsStream("test.bpmn");
        assertNotNull(in);
        assertNotNull(instance);
        File file = instance.createFile(in);
    }

    /** Test {@link FileService#getById}. */
    @Test(expected = NullPointerException.class)
    public void testGetById() throws Exception {
        Item nakedItem = createMock(Item.class);
        expect(nakedItem.getId()).andReturn(0L);
        //expect(nakedItem.getId()).andReturn(0L);
        replay(nakedItem);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        expect(itemPluginContext.getById(0L)).andReturn(nakedItem);
        replay(itemPluginContext);

        FileRepository fileRepository = createMock(FileRepository.class);

        FileServiceImpl instance = new FileServiceImpl();
        instance.setFileRepository(fileRepository);
        instance.setItemPluginContext(itemPluginContext);

        assertNull(instance.getById(0L));
    }
}
