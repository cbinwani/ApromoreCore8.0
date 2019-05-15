package org.apromore.folder.jpa;

/*-
 * #%L
 * Apromore :: folder-jpa
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

import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Data access object for {@link org.apromore.folder.Folder}.
 */
@Entity
@Table(name = "PATH")
public final class PathDAO {

    /** Primary key. */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Contained item identifier. */
    @Column(name = "ITEM_ID", unique = false, nullable = false)
    private Long itemId;

    /** Item name, exclusive of path. */
    @Column(name = "NAME", unique = false, nullable = false)
    private String name;

    /** Parent folder, possibly <code>null</code>. */
    @ManyToOne
    @JoinColumn(name = "PARENT_PATH", unique = false, nullable = true)
    private PathDAO parent;

    /** @return primary key */
    public Long getId() {
        return id;
    }

    /** @param newId  primary key */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /** @return item content */
    public Long getItemId() {
        return itemId;
    }

    /** @param newItemId  new content */
    public void setItemId(final Long newItemId) {
        this.itemId = newItemId;
    }

    /** @return non-empty text */
    public String getName() {
        return name;
    }

    /** @param newName  non-empty text */
    public void setName(final String newName) {
        this.name = newName;
    }

    /** @return parent folder, possibly <code>null</code> */
    public PathDAO getParent() {
        return parent;
    }

    /** @param newParent  new parent folder, possibly <code>null</code> */
    public void setParent(final PathDAO newParent) {
        this.parent = newParent;
    }

    @Override
    public String toString() {
        return super.toString() + " id=" + id + " name=" + name;
    }
}
