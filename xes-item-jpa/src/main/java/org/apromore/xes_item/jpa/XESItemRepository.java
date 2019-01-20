package org.apromore.xes_item.jpa;

import java.util.List;

/**
 * Factory service for {@link XESItemDAO}.
 */
public interface XESItemRepository {

    /** @return all XES items */
    List<XESItemDAO> list();

    /**
     * @param id  private key
     * @return the corresponding XES item fields
     */
    XESItemDAO get(Long id);

    /** @param newDAO  new XES item fields to be persisted */
    void add(XESItemDAO newDAO);

    /** @param id  primary key */
    void remove(Long id);
}
