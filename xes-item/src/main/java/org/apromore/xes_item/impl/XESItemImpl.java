package org.apromore.xes_item.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
//import java.nio.charset.Charset;
import java.util.List;
import javax.persistence.UniqueConstraint;
import org.apromore.item.Item;
import org.apromore.item.spi.ItemWrapper;
import org.apromore.xes_item.XESItem;
import org.apromore.xes_item.jpa.XesItemDao;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;

/**
 * A XES event log as a concrete {@link Item}.
 *
 */
class XESItemImpl extends ItemWrapper implements XESItem {

    private Long id;

    private byte[] xmlSerialization;

    XESItemImpl(XesItemDao dao) {
        this.id = dao.getId();
        this.xmlSerialization = dao.getXmlSerialization();
    }


    // Implementation of XESItem

    public XLog getXLog() {
        try {
            XesXmlParser parser = new XesXmlParser();
            List<XLog> logs = parser.parse(getXMLSerialization());
            return logs.get(0);

        } catch (Exception e) {
            throw new Error("Internal error: validated XES failed to re-parse", e);
        }
    }


    // Accessors

    /** @return primary key */
    public Long getId() {
        return id;
    }

    public void setId(Long newId) {
        this.id = newId;
    }

    public InputStream getXMLSerialization() {
        return new ByteArrayInputStream(xmlSerialization);
    }

    public void setXMLSerialization(byte[] newXMLSerialization) {
        this.xmlSerialization = newXMLSerialization;
    }
}
