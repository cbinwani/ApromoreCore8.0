package org.apromore.item;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;

/**
 * Factory for obtaining {@link Item} instances.
 */
public interface ItemService {

    /**
     * This creates a concrete subtype of {@link Item} from a serialization, automatically determining the particular format via the available plugins.
     *
     * Item types without a serialization format (e.g. folders) can't be created by using this method.
     *
     * @param inputStream  serialized form of this type of item
     * @return the created {@link Item}
     * @throws IOException if the <var>inputStream</var> failed to deliver the serialized data
     * @throws ItemFormatException if the serialized data cannot be interpreted as the expected type
     * @throws NotAuthorizedException if a lack of authorization prevents the item from being created
     */
    Item create(InputStream inputStream) throws IOException, ItemFormatException, NotAuthorizedException;

    /**
     * @return every {@link Item} in the repository which the caller is authorized to know about, in no guaranteed order
     */
    List<Item> getAll();

    /**
     * @param id  the primary key identifier of an existing {@link Item}, never <code>null</code>
     * @return either the unique {@link Item} with the given <var>id</var>, or <code>null</code> if no such item exists
     * @throws NotAuthorizedException if a lack of authorization prevents the item from being created
     */
    Item getById(Long id) throws NotAuthorizedException;
}
