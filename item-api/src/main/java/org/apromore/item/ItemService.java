package org.apromore.item;

/*-
 * #%L
 * Apromore :: item-api
 * %%
 * Copyright (C) 2018 - 2019 The Apromore Initiative
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.io.InputStream;
import java.io.IOException;
import java.util.List;

/**
 * Factory for obtaining {@link Item} instances.
 */
public interface ItemService {

    /**
     * This creates a concrete subtype of {@link Item} from a serialization,
     * automatically determining the particular format via the available
     * plugins.
     *
     * Item types without a serialization format (e.g. folders) can't be created
     * by using this method.
     *
     * @param inputStream  serialized form of this type of item
     * @return the created {@link Item}
     * @throws IOException if the <i>inputStream</i> failed to deliver the
     *     serialized data
     * @throws ItemFormatException if the serialized data cannot be interpreted
     *     as the expected type
     * @throws NotAuthorizedException if a lack of authorization prevents the
     *     item from being created
     */
    Item create(InputStream inputStream) throws IOException,
        ItemFormatException, NotAuthorizedException;

    /**
     * @return every {@link Item} in the repository which the caller is
     *     authorized to know about, in no guaranteed order
     */
    List<Item> getAll();

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
