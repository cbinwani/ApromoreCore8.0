package org.apromore.xes_item.jpa;

import java.util.List;

public interface XesItemRepository {

    List<XesItemDao> list();

    XesItemDao get(Long id);

    void add(XesItemDao newDao);

    void remove(Long id);
}
