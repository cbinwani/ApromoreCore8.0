package org.apromore.bpmn_item.jpa;

/*-
 * #%L
 * Apromore :: bpmn-item-jpa
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
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Data access object for <code>org.apromore.bpmn_item.BPMNItem</code>.
 */
@Entity
@Table(name = "BPMN_ITEM")
public final class BPMNItemDAO {

    /**
     * Primary key.
     *
     * Note that this isn't automatically @Generated.
     * The id must be obtained from this XES item's corresponding
     * {@link org.apromore.item.jpa.ItemDAO}.
     */
    @Id  // Note: not @Generated; the id must be supplied by an ItemDAO
    private Long id = -1L;

    /**
     * The BPMN 2.0 XML document text.
     *
     * This is stored as bytes rather than characters because technically XML is
     * binary and contains its own character encoding information.
     */
    @Column(name = "XML_SERIALIZATION", unique = false, nullable = false)
    private byte[] xmlSerialization = {};

    /** @return primary key */
    public Long getId() {
        return id;
    }

    /** @param newId  primary key */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /** @return BPMN 2.0 document text */
    public byte[] getXmlSerialization() {
        return xmlSerialization;
    }

    /** @param newXmlSerialization  BPMN 2.0 document text */
    public void setXmlSerialization(final byte[] newXmlSerialization) {
        this.xmlSerialization = newXmlSerialization;
    }
}
