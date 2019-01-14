package org.apromore.bpmn_item;

import java.io.Writer;
import javax.xml.transform.Source;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;

public interface BPMNItemService {

    /**
     * @param source  a valid XML serialization of a BPMN 2.0 document, or <code>null</code> to create an empty document.
     * @return an instance representing the stored document
     * @throws ItemFormatException if the <var>source</var> can't be interpreted as BPMN 2.0
     * @throws NotAuthorizedException if the caller's credentials do not permit item creation
     */
    public BPMNItem createBPMNItem(Source source) throws ItemFormatException, NotAuthorizedException;

    public BPMNItem getById(Long id) throws NotAuthorizedException;
}
