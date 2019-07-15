package org.apromore.item.spi;

/*-
 * #%L
 * Apromore :: item-spi
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

import org.apromore.Caller;
import org.apromore.NotAuthorizedException;
import org.apromore.item.Item;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Factory for obtaining {@link Item} instances.
 */
public interface ItemPluginContext {

    /**
     * This method should only be used by service providers, who are responsible
     * for creating a concrete subtype of the given <i>type</i>.
     *
     * @param type  used to initialize the created {@link Item}
     * @param caller  authorization to create {@link Item}s.
     * @return an incomplete {@link Item}; the caller is responsible for
     *     completing the concrete subtype
     * @throws NotAuthorizedException if a lack of authorization prevents the
     *     item from being created
     */
    Item create(String type, Caller caller) throws NotAuthorizedException;

    /**
     * @param id  the primary key identifier of an existing {@link Item}, never
     *     <code>null</code>
     * @param caller  authorization to access {@link Item}s.
     * @return either the unique {@link Item} with the given <i>id</i>, or
     *     <code>null</code> if no such item exists
     * @throws NotAuthorizedException if a lack of authorization prevents the
     *     item from being accessed
     */
    @Nullable
    Item getById(Long id, Caller caller) throws NotAuthorizedException;
}
