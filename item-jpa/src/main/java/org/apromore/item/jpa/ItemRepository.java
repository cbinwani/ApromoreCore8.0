package org.apromore.item.jpa;

import java.util.List;

/**
 * Factory service for {@link ItemDAO}.
 */
public interface ItemRepository {

    /** @return all items */
    List<ItemDAO> list();

    /**
     * @param id  primary key
     * @return the corresponding item fields
     */
    ItemDAO get(Long id);

    /** @param newDAO  new item fields to be persisted */
    void add(ItemDAO newDAO);

    /** @param id  primary key */
    void remove(Long id);
}
