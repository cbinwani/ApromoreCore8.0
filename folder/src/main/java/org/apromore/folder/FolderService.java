package org.apromore.folder;

import java.util.List;
import org.apromore.item.Item;
import org.apromore.item.NotAuthorizedException;

/**
 * Factory service for {@link Folder} instances.
 */
public interface FolderService {

    /**
     * @param location  the parent folder; pass <code>null</code> to indicate
     *     the root of the folder hierarchy
     * @param name  the name of the created folder, which cannot be
     *     <code>null</code>, empty, or already present in the <i>location</i>
     * @return an instance representing the stored document
     * @throws FolderAlreadyExistsException if the <i>location</i> already
     *     contains an item with the given <i>name</i>
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     item creation
     */
    Folder createFolder(Folder location, String name)
        throws FolderAlreadyExistsException, NotAuthorizedException;

    /**
     * @param id  primary key
     * @return the corresponding folder if one exists, <code>null</code>
     *     otherwise (which includes the case of another type of item existing
     *     with that id)
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the existing item
     */
    Folder getById(Long id) throws NotAuthorizedException;

    /**
     * @param location  a folder; pass <code>null</code> to indicate the root
     *     of the folder hierarchy
     * @return the ordered contents of the <i>location</i>
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the contents of the <i>location</i>
     */
    List<Item> getItemsInFolder(Folder location) throws NotAuthorizedException;
}
