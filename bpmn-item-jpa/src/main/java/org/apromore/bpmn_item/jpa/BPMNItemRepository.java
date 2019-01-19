package org.apromore.bpmn_item.jpa;

import java.util.List;

/** */
public interface BPMNItemRepository {

    /** @return */
    List<BPMNItemDAO> list();

    /**
     * @param id  foo
     * @return bar
     */
    BPMNItemDAO get(Long id);

    /** @param newDAO */
    void add(BPMNItemDAO newDAO);

    /** @param id */
    void remove(Long id);
}
