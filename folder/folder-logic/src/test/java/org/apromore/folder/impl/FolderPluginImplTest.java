package org.apromore.folder.impl;

/*-
 * #%L
 * Apromore :: folder
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

import org.apromore.folder.Folder;
import org.apromore.folder.FolderService;
import org.apromore.folder.PathAlreadyExistsException;
import org.apromore.item.Item;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.spi.ItemPluginContext;
import org.checkerframework.checker.nullness.qual.Nullable;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** Test suite for {@link FolderPluginImpl}. */
public class FolderPluginImplTest {

    @Before
    public void setup() {

    }

    /** Test {@link FolderService#createFolder}. */
    @Ignore
    @Test
    public void testCreateFolder_atRoot()
        throws PathAlreadyExistsException, NotAuthorizedException {
        // ?
    }

    /** Test {@link FolderService#findFoldertById}. */
    @Ignore
    @Test
    public void testFindFolderById() throws Exception {
        @Nullable FolderImpl expectedFolder = createMock(FolderImpl.class);
        if (expectedFolder == null) {
            throw new Exception("Unable to create expected folder");
        }

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        FolderService folderService = new FolderPluginImpl();

        Folder folder = folderService.findFolderById(0L);
        if (folder == null) {
            throw new AssertionError();
        }
        assertEquals(expectedFolder, folder);
    }
}
