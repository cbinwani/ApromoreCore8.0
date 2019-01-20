package org.apromore.item.spi;

import org.apromore.item.Item;
import org.apromore.item.NotAuthorizedException;

/**
 * Factory for obtaining {@link Item} instances.
 */
public interface ItemPluginContext {

    /**
     * This method should only be used by service providers, who are responsible
     * for creating a concrete subtype of the given <i>type</i>.
     *
     * @param type  used to initialize the created {@link Item}
     * @return an incomplete {@link Item}; the caller is responsible for
     *     completing the concrete subtype
     * @throws NotAuthorizedException if a lack of authorization prevents the
     *     item from being created
     */
    Item create(String type) throws NotAuthorizedException;

    /**
     * @param id  the primary key identifier of an existing {@link Item}, never
     *     <code>null</code>
     * @return either the unique {@link Item} with the given <i>id</i>, or
     *     <code>null</code> if no such item exists
     * @throws NotAuthorizedException if a lack of authorization prevents the
     *     item from being created
     */
    Item getById(Long id) throws NotAuthorizedException;
}
