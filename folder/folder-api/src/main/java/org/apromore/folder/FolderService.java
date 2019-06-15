package org.apromore.folder;

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

import java.util.List;
import org.apromore.item.Item;
import org.apromore.item.NotAuthorizedException;

/**
 * Factory service for {@link Folder} instances, plus utilities for path-based
 * {@link Item} access.
 *
 * A path is a specially formatted @{link String} containing a list of
 * substrings separated by <code>'/'</code>.
 */
public interface FolderService {

    /**
     * @param parentFolder the parent folder; pass <code>null</code> to indicate
     *     the root of the folder hierarchy
     * @param name  the name of the created folder, which cannot be
     *     <code>null</code>, empty, or already present in the <i>location</i>
     * @return an instance representing the stored document
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     item creation
     * @throws PathAlreadyExistsException if the <i>parentPath</i> already
     *     contains an item with the given <i>name</i>
     */
    Folder createFolder(Folder parentFolder, String name)
        throws NotAuthorizedException, PathAlreadyExistsException;

    /**
     * @param parentFolder the parent folder; pass <code>null</code> to indicate
     *     the root of the folder hierarchy
     * @param name  the name of the created folder, which cannot be
     *     <code>null</code> or already present in the <i>lparentFolder</i>
     * @param content  the initial content at the new path
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     item creation
     * @throws PathAlreadyExistsException if the <i>parentPath</i> already
     *     contains an item with the given <i>name</i>
     */
    void createPath(Folder parentFolder, String name, Item content)
        throws NotAuthorizedException, PathAlreadyExistsException;

    /**
     * @param parentFolder the parent folder; pass <code>null</code> to indicate
     *     the root of the folder hierarchy
     * @param name  the name of an item in the folder, which cannot be
     *     <code>null</code>, and must already be present in the <i>location</i>
     * @param content  the new content at the path
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     item modification
     */
    void updatePath(Folder parentFolder, String name, Item content)
        throws NotAuthorizedException;

    /**
     * @param id  primary key
     * @return the corresponding folder if one exists, <code>null</code>
     *     otherwise (which includes the case of another type of item existing
     *     with that id)
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the existing item
     */
    Folder findFolderById(Long id) throws NotAuthorizedException;

    /**
     * @param folder  the parent folder, or <code>null</code> for the root of
     *     the folder hierarchy
     * @param name  never <code>null</code>
     * @return the content in <i>folder</i> named <i>name</i>
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the item
     */
    Item findItemByFolderAndName(Folder folder, String name)
        throws NotAuthorizedException;

    /**
     * @param path  the full pathname of an existing {@link Item}
     * @return  the corresponding {@link Item}, or <code>null</code> if the
     *     <i>path</i> doesn't identify any existing {@link Item}
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the existing item
     */
    Item findItemByPath(String path) throws NotAuthorizedException;

    /**
     * @param item  never <code>null</code>
     * @return the unique path to this <i>item</i>
     */
    String findPathByItem(Item item);

    /**
     * @return paths contained by the root folder, never <code>null</code>
     */
    List<String> getRootFolderPaths();
}
