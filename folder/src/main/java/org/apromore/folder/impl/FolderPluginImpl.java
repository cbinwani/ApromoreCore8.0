package org.apromore.folder.impl;

import java.io.InputStream;
import java.util.List;
//import javax.transaction.Transactional;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
import org.apromore.folder.Folder;
import org.apromore.folder.FolderAlreadyExistsException;
import org.apromore.folder.FolderService;
import org.apromore.folder.jpa.FolderDAO;
import org.apromore.folder.jpa.FolderRepository;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Both an {@link FolderService} and an {@link ItemPlugin} providing
 * folder support.
 */
@Component(service = {FolderService.class, ItemPlugin.class})
public final class FolderPluginImpl
    implements FolderService, ItemPlugin<Folder> {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(FolderPluginImpl.class);

    /** Utility service for {@link ItemPlugin}s. */
    @Reference
    private ItemPluginContext itemPluginContext;

    /** Factory service for folder-specific data access objects. */
    @Reference
    private FolderRepository folderRepository;


    // ItemPlugin implementation

    @Override
    public Folder create(final InputStream inputStream)
        throws ItemFormatException, NotAuthorizedException {

        throw new ItemFormatException(getType());
    }

    @Override
    public String getType() {
        return Folder.TYPE;
    }

    @Override
    public Folder toConcreteItem(final Item item) throws ItemTypeException {
        FolderDAO dao = folderRepository.get(item.getId());
        if (dao == null) {
             throw new ItemTypeException(getType(), item.getType());
        }
        return new FolderImpl(item, dao);
    }


    // FolderService implementation

    @Override
    public Folder createFolder(final Folder location, final String name)
        throws FolderAlreadyExistsException, NotAuthorizedException {

        // Unless location is null, we need to look up the parent's DAO
        FolderDAO locationDAO = null;
        if (location != null) {
            locationDAO = folderRepository.get(location.getId());
        }

        Item item = this.itemPluginContext.create(getType());

            FolderDAO dao = new FolderDAO();
            dao.setId(item.getId());
            dao.setName(name);
            dao.setParent(locationDAO);
            folderRepository.add(dao);

            return new FolderImpl(item, dao);
    }

    @Override
    public Folder getById(final Long id) throws NotAuthorizedException {
        Item item = this.itemPluginContext.getById(id);
        FolderDAO dao = folderRepository.get(id);
        return new FolderImpl(item, dao);
    }

    @Override
    public List<Item> getItemsInFolder(final Folder location)
        throws NotAuthorizedException {

        return new java.util.ArrayList<>();  // TODO
    }
}
