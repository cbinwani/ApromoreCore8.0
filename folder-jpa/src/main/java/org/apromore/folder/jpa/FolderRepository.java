package org.apromore.folder.jpa;

import java.util.List;

/**
 * Factory service for {@link PathDAO}.
 */
public interface FolderRepository {

    /** @param newDAO  new folder fields to be persisted */
    void addPath(PathDAO newDAO);

    /**
     * @param parent  may by <code>null</code>
     * @param name  never <code>null</code>
     * @return primary key of the {@link org.apromore.item.Item} content if it
     *     exists, <code>null</code> otherwise
     */
    Long findItemIdByParentAndName(PathDAO parent, String name);

    /**
     * @param parent  may by <code>null</code>
     * @return all paths with the given <i>parent</i>
     */
    List<PathDAO> findPathsByParent(PathDAO parent);

    /**
     * @param id  primary key
     * @return the corresponding path
     */
    PathDAO findPath(Long id);

    /**
     * Look up a path by its content.
     *
     * Note that more than one path may have the same content; this occurs
     * whenever a generic {@link org.apromore.item.Item} is copied, for
     * instance.
     * {@link org.apromore.folder.Folder}s are the exception, and never have
     * multiple path.
     *
     * @param itemId  primary key of an {@link org.apromore.item.Item}
     * @return the corresponding path, or <code>null</code> if the number of
     *      paths with that item isn't exactly one
     */
    PathDAO findPathByItemId(Long itemId);

    /** @param id  primary key */
    void removePath(Long id);
}
