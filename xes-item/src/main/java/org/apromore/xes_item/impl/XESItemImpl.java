package org.apromore.xes_item.impl;

/*-
 * #%L
 * Apromore :: xes-item
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.apromore.item.Item;
import org.apromore.item.spi.ItemWrapper;
import org.apromore.xes_item.XESItem;
import org.apromore.xes_item.jpa.XESItemDAO;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;

/**
 * A XES event log as a concrete {@link Item}.
 *
 */
class XESItemImpl extends ItemWrapper implements XESItem {

    /** The serialized XES 1.0 document. */
    private byte[] xmlSerialization;

    /**
     * @param item  a partially-constructed item from
     *     {@link org.apromore.item.jpa.ItemRepository}
     * @param dao  data access object for XES-specific fields
     */
    XESItemImpl(final Item item, final XESItemDAO dao) {
        super(item);

        assert item.getId() == dao.getId();
        this.xmlSerialization = dao.getXmlSerialization();
    }


    // Implementation of XESItem

    @Override
    public XLog getXLog() {
        try {
            XesXmlParser parser = new XesXmlParser();
            List<XLog> logs = parser.parse(getXMLSerialization());
            return logs.get(0);

        } catch (Exception e) {
            throw new Error("Internal error: validated XES failed to re-parse",
                e);
        }
    }


    // Accessors

    @Override
    public InputStream getXMLSerialization() {
        return new ByteArrayInputStream(xmlSerialization);
    }
}
