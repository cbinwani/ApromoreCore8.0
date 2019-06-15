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

import org.apromore.item.Item;

/**
 * Class that wraps an {@link Item}.
 *
 * This is a convenience for plugin providers needing to implement concrete
 * subtypes of {@link Item}.
 */
public class ItemWrapper implements Item {

    /** The wrapped instance. */
    private Item item;

    /** @param wrappedItem  the wrapped instance */
    public ItemWrapper(final Item wrappedItem) {
        this.item = wrappedItem;
    }


    // Implementation of Item

    /** @return {@inheritDoc}  Returns the wrapped instance's id. */
    public Long getId() {
        return item.getId();
    }

    /** @return {@inheritDoc}  Returns the wrapped instance's type. */
    public String getType() {
        return item.getType();
    }
}
