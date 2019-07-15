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

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
//import javax.transaction.Transactional;
import org.apromore.Caller;
import org.apromore.NotAuthorizedException;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
import org.apromore.folder.Folder;
import org.apromore.folder.FolderService;
import org.apromore.folder.PathAlreadyExistsException;
import org.apromore.folder.jpa.FolderRepository;
import org.apromore.folder.jpa.PathDAO;
import org.checkerframework.checker.nullness.qual.Nullable;
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
    @SuppressWarnings("nullness")
    private ItemPluginContext itemPluginContext;

    /** Factory service for folder-specific data access objects. */
    @Reference
    @SuppressWarnings("nullness")
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
    public Folder create(final InputStream inputStream, final Caller caller)
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
    public Folder createFolder(final Folder parentFolder,
                               final String name,
                               final Caller caller)
        throws NotAuthorizedException, PathAlreadyExistsException {

        try {
            LOGGER.info("Creating folder " + name + " in " + parentFolder);

            Item item = this.itemPluginContext.create(getType(), caller);
            Folder folder = new FolderImpl(item, folderRepository);
            createPath(parentFolder, name, folder, caller);

            return folder;

        } catch (ItemTypeException e) {
            throw new Error(e);
        }
    }

    @Override
    public void createPath(final Folder parentFolder,
                           final String name,
                           final Item   content,
                           final Caller caller)
        throws NotAuthorizedException, PathAlreadyExistsException {

        if (name.contains(SEPARATOR)) {
            throw new IllegalArgumentException("Name " + name
                + " contains the separator " + SEPARATOR);
        }

        PathDAO parentDAO = null;
        if (parentFolder != null) {
            parentDAO = folderRepository.findPathByItemId(parentFolder.getId());
        }

        // Validate that this path doesn't already exist
        if (folderRepository.findItemIdByParentAndName(parentDAO, name)
            != null) {

            throw new PathAlreadyExistsException(fullPath(parentFolder, name));
        }

        PathDAO dao = new PathDAO();
        dao.setItemId(content.getId());
        dao.setName(name);
        dao.setParent(parentDAO);
        folderRepository.addPath(dao);
    }

    @Override
    public void updatePath(final Folder parentFolder,
                           final String name,
                           final Item content,
                           final Caller caller)
        throws NotAuthorizedException {

        throw new UnsupportedOperationException();
        /*
        PathDAO dao = folderRepository.findPathBy
        dao.setItemId(content.getId());
        folderRepository.addPath(dao);
        */
    }

    /**
     * @param parentFolder  may be <code>null</code>
     * @param name  never <code>null</code>
     * @return full path
     */
    private String fullPath(final @Nullable Folder parentFolder,
                            final String           name) {

        if (parentFolder == null) {
            return name;

        } else {
            return fullPath(folderRepository.findPathByItemId(
                parentFolder.getId())) + SEPARATOR + name;
        }
    }

    /**
     * Derive the full path of a DAO.
     *
     * @param pathDAO  may be <code>null</code>
     * @return full path
     */
    @Nullable
    private String fullPath(final @Nullable PathDAO pathDAO) {
        if (pathDAO == null) {
            return null;

        } else if (pathDAO.getParent() == null) {
            return pathDAO.getName();

        } else {
            return fullPath(pathDAO.getParent()) + SEPARATOR
                + pathDAO.getName();
        }
    }

    @Override
    @Nullable
    public Folder findFolderById(final Long id, final Caller caller)
        throws NotAuthorizedException {

        Item item = itemPluginContext.getById(id, caller);
        if (item == null) {
            return null;
        }
        try {
            return new FolderImpl(item, folderRepository);

        } catch (ItemTypeException e) {
            return null;
        }
    }

    @Override
    @Nullable
    public Item findItemByFolderAndName(final @Nullable Folder folder,
                                        final String name,
                                        final Caller caller)
        throws NotAuthorizedException {

        LOGGER.info("Finding item in " + folder + " named " + name);

        PathDAO parent = null;
        if (folder != null) {
            parent = folderRepository.findPathByItemId(folder.getId());
        }
        LOGGER.info("  Folder " + folder + " path id " + parent);

        Long itemId = folderRepository.findItemIdByParentAndName(parent, name);
        if (itemId == null) {
            return null;
        }

        LOGGER.info("Found item in " + folder + " named " + name + ": "
            + itemId);

        return itemPluginContext.getById(itemId, caller);
    }

    @Override
    @Nullable
    public Item findItemByPath(final String path, final Caller caller)
        throws NotAuthorizedException {

        LOGGER.info("Finding item by path " + path);

        String parentPath = parentForPath(path);
        String name = nameForPath(path);

        Folder folder = null;
        if (parentPath != null) {
            folder = (Folder) findItemByPath(parentPath, caller);
        }

        return findItemByFolderAndName(folder, name, caller);
    }

    /**
     * @param path  never <code>null</code>
     * @return the parent prefix of the <i>path</i>, or <code>null</code> if
     *     this path is at the root of the folder hierarchy
     */
    @Nullable
    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    private static String parentForPath(final String path) {
        int i = path.lastIndexOf(SEPARATOR);
        LOGGER.info("parentForPath(" + path + ") = \""
            + (i == -1 ? null : path.substring(0, i)) + "\"");
        return i == -1 ? null : path.substring(0, i);
    }

    /**
     * @param path  never <code>null</code>
     * @return the leaf name of the <i>path</i>
     */
    private static String nameForPath(final String path) {
        int i = path.lastIndexOf(SEPARATOR);
        LOGGER.info("nameForPath(" + path + ") = \""
            + path.substring(i + 1, path.length()) + "\"");
        return path.substring(i + 1, path.length());
    }

    @Override
    public String findPathByItem(final Item item, final Caller caller) {
        PathDAO dao = folderRepository.findPathByItemId(item.getId());
        if (dao == null) {
            throw new AssertionError();
        }
        return pathToString(dao);
    }

    @Override
    public List<String> getRootFolderPaths(final Caller caller) {
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
