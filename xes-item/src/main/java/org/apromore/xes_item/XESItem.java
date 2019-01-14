package org.apromore.xes_item;

import java.io.InputStream;
import org.apromore.item.Item;
import org.deckfour.xes.model.XLog;

/**
 * A XES event log as a concrete {@link Item}.
 *
 */
public interface XESItem extends Item {

    /**
     * @return a Java object model for the XES event log
     */
    public XLog getXLog();

    /**
     * @return a stream containing the XML serialization of the XES event log
     */
    public InputStream getXMLSerialization();
}
