package org.apromore.xes_item;

import java.io.Writer;
import javax.xml.transform.Source;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;

public interface XESItemService {

    /**
     * @param source  a valid XML serialization of a XES document, or <code>null</code> to create an empty document.
     * @return an instance representing the stored document
     * @throws ItemFormatException if the <var>source</var> can't be interpreted as XES
     * @throws NotAuthorizedException if the caller's credentials do not permit item creation
     */
    public XESItem createXESItem(Source source) throws ItemFormatException, NotAuthorizedException;

    public XESItem getById(Long id) throws NotAuthorizedException;
}
