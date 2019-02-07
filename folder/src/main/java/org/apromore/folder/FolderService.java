package org.apromore.folder;

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
     * @param id  primary key
     * @return the corresponding folder if one exists, <code>null</code>
     *     otherwise (which includes the case of another type of item existing
     *     with that id)
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the existing item
     */
    Folder findFolderById(Long id) throws NotAuthorizedException;

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
