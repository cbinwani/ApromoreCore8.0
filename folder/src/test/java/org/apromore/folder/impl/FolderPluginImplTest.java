package org.apromore.folder.impl;

import org.apromore.folder.Folder;
import org.apromore.folder.FolderService;
import org.apromore.folder.PathAlreadyExistsException;
import org.apromore.item.Item;
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
        FolderImpl expectedFolder = createMock(FolderImpl.class);

        ItemPluginContext itemPluginContext = createMock(ItemPluginContext.class);
        FolderService folderService = new FolderPluginImpl();

        Folder folder = folderService.findFolderById(0L);
        assertEquals(expectedFolder, folder);
    }
}
