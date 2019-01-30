package org.apromore.folder.impl;

import org.apromore.item.Item;
import org.apromore.item.spi.ItemWrapper;
import org.apromore.folder.Folder;
import org.apromore.folder.jpa.FolderDAO;

/**
 * A folder as a concrete {@link Item}.
 *
 */
class FolderImpl extends ItemWrapper implements Folder {

    /**
     * The parent folder, or <code>null</code> if this folder is at the top of
     * the folder hierarchy.
     */
    private Folder parent;

    /** The folder name, not including any path prefix. */
    private String name;

    /**
     * @param item  a partially-constructed item from
     *     {@link org.apromore.item.jpa.ItemRepository}
     * @param dao  data access object for folder-specific fields
     */
    FolderImpl(final Item item, final FolderDAO dao) {
        super(item);

        assert item.getId() == dao.getId();
        //this.parent = dao.getParent();
        this.name   = dao.getName();
    }

    /** {@inheritDoc} */
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    public Folder getParentFolder() {
        return parent;
    }

    /** {@inheritDoc}
     *
     * This implementation returns the folder name.
     */
    public String toString() {
        return name;
    }

    // Implementation of SortedMap<String, Item> from Folder
}
