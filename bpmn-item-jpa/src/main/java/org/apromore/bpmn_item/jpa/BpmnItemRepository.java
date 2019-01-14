package org.apromore.bpmn_item.jpa;

import java.util.List;

public interface BpmnItemRepository {

    List<BpmnItemDao> list();

    BpmnItemDao get(Long id);

    void add(BpmnItemDao newDao);

    void remove(Long id);
}
