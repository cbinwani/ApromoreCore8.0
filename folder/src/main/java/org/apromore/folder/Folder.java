package org.apromore.folder;

//import java.util.SortedItem;
import org.apromore.item.Item;

/**
 * A folder as a concrete {@link Item}.
 *
 * As far as practical, this works similarly to {@link java.io.File}.
 */
public interface Folder extends Item /*, SortedMap<String, Item>*/ {

    /**
     * Type identifier for folders.
     */
    String TYPE = "Folder";

    /**
     * @return the name of the folder
     */
    String getName();

    /**
     * @return the folder in which this folder is located, or <code>null</code>
     *      if this folder is at the top level of the folder hierarchy
     */
    Folder getParentFolder();
}
