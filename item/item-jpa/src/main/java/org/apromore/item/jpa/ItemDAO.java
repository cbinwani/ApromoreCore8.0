package org.apromore.item.jpa;

/*-
 * #%L
 * Apromore :: item-jpa
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

import javax.persistence.Entity;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Data access object for common fields of <code>org.apromore.item.Item</code>
 * instances.
 *
 * The fields specific to any particular concrete subtype are stored in
 * their own supplementary DAOs.
 */
@Entity
@Table(name = "ITEM")
public final class ItemDAO {

    /** Primary key. */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** The type identifier of the concrete subclass. */
    private String type;

    /** @return primary key */
    public Long getId() {
        return id;
    }

    /** @param newId  the type identifier of the concrete subclass */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /** @return the type identifier of the concrete subclass */
    public String getType() {
        return type;
    }

    /** @param newType  the type identifier of the concrete subclass */
    public void setType(final String newType) {
        this.type = newType;
    }
}
