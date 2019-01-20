package org.apromore.xes_item;

import javax.xml.transform.Source;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;

/**
 * Factory service for {@link XESItem} instances.
 */
public interface XESItemService {

    /**
     * @param source  a valid XML serialization of a XES document, or
     *     <code>null</code> to create an empty document.
     * @return an instance representing the stored document
     * @throws ItemFormatException if the <i>source</i> can't be interpreted
     *     as XES
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     item creation
     */
    XESItem createXESItem(Source source) throws ItemFormatException,
        NotAuthorizedException;

    /**
     * @param id  primary key
     * @return the corresponding XES item if one exists, <code>null</code>
     *     otherwise (which includes the case of a non-XES item existing with
     *     that id)
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the existing item
     */
    XESItem getById(Long id) throws NotAuthorizedException;
}
