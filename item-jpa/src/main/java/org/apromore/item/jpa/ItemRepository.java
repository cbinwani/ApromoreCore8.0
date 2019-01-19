package org.apromore.item.jpa;

import java.util.List;

public interface ItemRepository {

    List<ItemDAO> list();

    ItemDAO get(Long id);

    void add(ItemDAO newDAO);

    void remove(Long id);
}
