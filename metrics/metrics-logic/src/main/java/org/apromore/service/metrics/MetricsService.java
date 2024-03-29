package org.apromore.service.metrics;

/*-
 * #%L
 * Apromore :: metrics
 * %%
 * Copyright (C) 2016 - 2019 The Apromore Initiative
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

//import org.apromore.graph.canonical.Canonical;
import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;

import java.util.Map;

/**
 * Created by Adriano Augusto on 18/04/2016.
 */
public interface MetricsService {

    Map<String, String> computeMetrics(BPMNDiagram model, boolean size, boolean cfc, boolean acd,
                                       boolean mcd, boolean cnc, boolean density,
                                       boolean structuredness, boolean separability,
                                       boolean duplicates);

    //Map<String, String> computeCanonicalMetrics(Canonical diagram);

    Map<String, String> computeMetrics(XLog log);

}
