package org.apromore.bpmn_item;

import javax.xml.transform.Source;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;

/**
 * Factory service for {@link BPMNItem} instances.
 */
public interface BPMNItemService {

    /**
     * @param source  a valid XML serialization of a BPMN 2.0 document, or
     *     <code>null</code> to create an empty document.
     * @return an instance representing the stored document
     * @throws ItemFormatException if the <i>source</i> can't be interpreted as
     *     BPMN 2.0
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     item creation
     */
    BPMNItem createBPMNItem(Source source) throws ItemFormatException,
        NotAuthorizedException;

    /**
     * @param id  primary key
     * @return the corresponding BPMN item if one exists, <code>null</code>
     *     otherwise (which includes the case of a non-BPMN item existing with
     *     that id)
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the existing item
     */
    BPMNItem getById(Long id) throws NotAuthorizedException;
}
