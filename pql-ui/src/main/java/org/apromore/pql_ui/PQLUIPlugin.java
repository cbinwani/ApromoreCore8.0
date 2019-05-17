package org.apromore.pql_ui;

/*-
 * #%L
 * Apromore :: pql-ui
 * %%
 * Copyright (C) 2019 The Apromore Initiative
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

import org.apromore.pql.PQLService;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;


/**
 * Metrics service.
 */
@Component(service = {UIPlugin.class})
public final class PQLUIPlugin extends AbstractUIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(PQLUIPlugin.class);

    /** Used to evaluate PQL queries. */
    @Reference
    private PQLService pqlService;

    /** Sole constructor. */
    public PQLUIPlugin() {
        super("analyze.group", "search.label", "search.iconSclass");
    }

    /** {@inheritDoc}
     *
     * This implementation allows the entry and evaluation of PQL queries.
     */
    @Override
    public void execute(final UIPluginContext context) {

        Window window = (Window) context.createComponent(
            PQLUIPlugin.class.getClassLoader(), "zul/query.zul", null);

        ((Button) window.getFellow("OKButton")).addEventListener("onClick",
            new EventListener<Event>() {

            public void onEvent(final Event event) throws Exception {
                //runProcessComputation(bpmnItem, context, window);
                window.detach();
            }
        });

        ((Button) window.getFellow("CancelButton")).addEventListener("onClick",
            new EventListener<Event>() {

            public void onEvent(final Event event) throws Exception {
                window.detach();
            }
        });

        window.doModal();
    }
}
