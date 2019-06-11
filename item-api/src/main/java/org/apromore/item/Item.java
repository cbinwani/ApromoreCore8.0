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

/**
 * An item is any entity stored within the Apromore repository, such as an event
 * log or a process model.
 *
 * Conceptually, items are abstract; a separate SPI
 * <code>org.apromore.item.spi.ItemPlugin</code> provides concrete subtypes of
 * items, e.g. BPMN models.
 *
 * Items are persistent.
 *
 * Items have access controls based on JAAS Principals.
 */
public interface Item {

    /**
     * Every item has a unique primary key.
     *
     * Furthermore, each item ought to have some concrete subtype instance with
     * the same key, e.g. a <code>org.apromore.bpmn_item.BPMNItem</code>.
     * "Ought", because if the subtype
     * <code>org.apromore.item.spi.ItemPlugin</code>
     * is uninstalled, an item might be orphaned until the plugin is
     * reinstalled.
     *
     * @return primary key
     */
    Long getId();

    /**
     * Identifies the subtype of the item.
     *
     * This is the case even if the service provider is unavailable, and is used
     * to associate existing items with a newly added service provider.
     *
     * @return MIME- or UTI-style identifier for the content of this item, e.g
     *     <code>org.omg.bpmn</code> for a BPMN process model
     * @see <code>org.apromore.bpmn_item.BPMNItem#TYPE</code>
     * @see <code>org.apromore.xes_item.XESItem#TYPE</code>
     */
    String getType();
}
