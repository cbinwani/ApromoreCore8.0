package org.apromore.folder.impl;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
//import javax.transaction.Transactional;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
import org.apromore.folder.Folder;
import org.apromore.folder.FolderService;
import org.apromore.folder.PathAlreadyExistsException;
import org.apromore.folder.jpa.FolderRepository;
import org.apromore.folder.jpa.PathDAO;
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

    /** Path separator. */
    private static final String SEPARATOR = "/";

    /** Utility service for {@link ItemPlugin}s. */
    @Reference
    private ItemPluginContext itemPluginContext;

    /** Factory service for folder-specific data access objects. */
    @Reference
    private FolderRepository folderRepository;


    // ItemPlugin implementation

    /** {@inheritDoc}
     *
     * This implementation presumes that there's no file format for folders, so
     * it always fails.
     *
     * @throws ItemFormatException always
     */
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
        if (!getType().equals(item.getType())) {
            throw new ItemTypeException(getType(), item.getType());
        }
        return new FolderImpl(item, folderRepository);
    }


    // FolderService implementation

    @Override
    public Folder createFolder(final Folder parentFolder, final String name)
        throws NotAuthorizedException, PathAlreadyExistsException {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        if (name.contains(SEPARATOR)) {
            throw new IllegalArgumentException("Name " + name
                + " contains the separator " + SEPARATOR);
        }

        PathDAO parentDAO = null;
        if (parentFolder != null) {
            parentDAO = folderRepository.findPathByItemId(parentFolder.getId());
        }

        Item item = this.itemPluginContext.create(getType());

        PathDAO dao = new PathDAO();
        dao.setItemId(item.getId());
        dao.setName(name);
        dao.setParent(parentDAO);
        folderRepository.addPath(dao);

        try {
            Folder returned = new FolderImpl(item, folderRepository);
            LOGGER.info("Created folder " + name + " in " + parentFolder);
            return returned;

        } catch (ItemTypeException e) {
            throw new Error(e);
        }
    }

    @Override
    public Folder findFolderById(final Long id) throws NotAuthorizedException {
        Item item = this.itemPluginContext.getById(id);
        try {
            return new FolderImpl(item, folderRepository);

        } catch (ItemTypeException e) {
            return null;
        }
    }

    @Override
    public Item findItemByPath(final String path)
        throws NotAuthorizedException {

        return itemPluginContext.getById(
            folderRepository.findItemIdByPath(path));
    }

    @Override
    public String findPathByItem(final Item item) {
        PathDAO dao = folderRepository.findPathByItemId(item.getId());
        if (dao == null) {
            return null;
        }
        return pathToString(dao);
    }

    @Override
    public List<String> getRootFolderPaths() {
        List<PathDAO> p = folderRepository.findPathsByParent(null);
        List<String> s = p
              //folderRepository.findPathsByParent(null)
                              .stream()
                              .map(path -> path.getName())
                              .collect(Collectors.toList());

        LOGGER.info("Got root folder for paths, mapped from " + p + " to " + s);
        return s;
    }

    // Internal methods

    /**
     * @param path  possibly <code>null</code>
     * @return the full path naming the <i>path</i>
     */
    private String pathToString(final PathDAO path) {
        if (path == null) {
            return null;

        } else {
            PathDAO parentPath = path.getParent();
            if (parentPath == null) {
                return path.getName();

            } else {
                return pathToString(parentPath)
                    + SEPARATOR
                    + path.getName();
            }
        }
    }
}
