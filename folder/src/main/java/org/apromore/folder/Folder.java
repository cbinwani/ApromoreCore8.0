package org.apromore.folder;

import java.util.List;
import org.apromore.item.Item;

/**
 * A folder as a concrete {@link Item}.
 *
 * Unlike most {@link Item}s which are immutable, a folder has a mutable
 * ordered list of paths, its contents.
 * Use {@link FolderService#findItemByPath} to convert paths to {@link Item}s.
 */
public interface Folder extends Item {

    /**
     * Type identifier for folders.
     */
    String TYPE = "Folder";

    /**
     * Append an item to the contents of this folder.
     *
     * @param name  the leaf name of the new item
     * @param content  the content of the new document
     */
    void append(String name, Item content);

    /**
     * @return the ordered list of full paths contained by this folder
     */
    List<String> getPaths();
}
