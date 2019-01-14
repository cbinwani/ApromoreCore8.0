package org.apromore.item.jpa;

import java.util.List;

public interface ItemRepository {

    List<ItemDao> list();

    ItemDao get(Long id);

    void add(ItemDao newDao);

    void remove(Long id);
}
