package org.apromore.bpmn_item;

import java.io.InputStream;
import org.apromore.item.Item;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;

/**
 * A BPMN 2.0 process model as a concrete {@link Item}.
 *
 */
public interface BPMNItem extends Item {

    /**
     * @return a Java object model for the BPMN process model
     */
    BPMNDiagram getBPMNDiagram();

    /**
     * @return a stream containing the XML serialization of the BPMN process model
     */
    InputStream getXMLSerialization();
}
