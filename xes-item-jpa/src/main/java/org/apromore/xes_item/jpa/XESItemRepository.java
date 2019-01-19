package org.apromore.xes_item.jpa;

import java.util.List;

public interface XESItemRepository {

    List<XESItemDAO> list();

    XESItemDAO get(Long id);

    void add(XESItemDAO newDAO);

    void remove(Long id);
}
