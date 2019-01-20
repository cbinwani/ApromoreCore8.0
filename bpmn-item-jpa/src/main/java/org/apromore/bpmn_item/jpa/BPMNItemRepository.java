package org.apromore.bpmn_item.jpa;

import java.util.List;

/**
 * Factory service for {@link BPMNItemDAO}.
 */
public interface BPMNItemRepository {

    /** @return all BPMN items */
    List<BPMNItemDAO> list();

    /**
     * @param id  private key
     * @return the corresponding BPMN item fields
     */
    BPMNItemDAO get(Long id);

    /** @param newDAO  new BPMN item fields to be persisted */
    void add(BPMNItemDAO newDAO);

    /** @param id  primary key */
    void remove(Long id);
}
