package org.apromore.folder.jpa;

import java.util.List;

/**
 * Factory service for {@link XESItemDAO}.
 */
public interface FolderRepository {

    /** @param newDAO  new folder fields to be persisted */
    void add(FolderDAO newDAO);

    /**
     * @param parentId  may by <code>null</code>
     * @return all folders with the given <i>parent</i>
     */
    List<FolderDAO> findByParentId(Long parentId);

    /**
     * @param id  private key
     * @return the corresponding folder
     */
    FolderDAO get(Long id);

    /** @param id  primary key */
    void remove(Long id);
}
