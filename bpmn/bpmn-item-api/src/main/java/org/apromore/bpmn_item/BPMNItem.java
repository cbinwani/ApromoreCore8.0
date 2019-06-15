package org.apromore.bpmn_item;

/*-
 * #%L
 * Apromore :: bpmn-item
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

import java.io.InputStream;
import org.apromore.item.Item;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;

/**
 * A BPMN 2.0 process model as a concrete {@link Item}.
 *
 */
public interface BPMNItem extends Item {

    /**
     * Type identifier for BPMN items.
     */
    String TYPE = "BPMN 2.0";

    /**
     * @return a Java object model for the BPMN process model
     */
    BPMNDiagram getBPMNDiagram();

    /**
     * @return a stream containing the XML serialization of the BPMN process
     *     model
     */
    InputStream getXMLSerialization();
}
