package org.apromore.bpmn_item.jpa;

import java.util.List;

public interface BPMNItemRepository {

    List<BPMNItemDAO> list();

    BPMNItemDAO get(Long id);

    void add(BPMNItemDAO newDao);

    void remove(Long id);
}
