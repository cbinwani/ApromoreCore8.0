package org.apromore.xes_item.impl;

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
