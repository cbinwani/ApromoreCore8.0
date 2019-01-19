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
     * Type identifier for BPMN items.
     */
    String TYPE = "XES 1.0";

    /**
     * @return a Java object model for the XES event log
     */
    XLog getXLog();

    /**
     * @return a stream containing the XML serialization of the XES event log
     */
    InputStream getXMLSerialization();
}
