package org.apromore.item.impl;

/*-
 * #%L
 * Apromore :: item
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
import org.apromore.item.jpa.ItemDAO;

/**
 * Implementation of {@link Item}.
 */
class ItemImpl implements Item {

    /** Primary key. */
    private Long id;

    /** Concrete type identifier. */
    private String type;

    /** @param dao  data access object */
    ItemImpl(final ItemDAO dao) {
        this.id = dao.getId();
        this.type = dao.getType();
    }


    // Accessors

    /** @return primary key */
    public Long getId() {
        return id;
    }

    /**
     * @return a MIME- or UTI-like identifier that associates this Item with
     *     an appropriate {@link org.apromore.item.spi.ItemPlugin}
     *     implementation
     */
    public String getType() {
        return type;
    }
}
