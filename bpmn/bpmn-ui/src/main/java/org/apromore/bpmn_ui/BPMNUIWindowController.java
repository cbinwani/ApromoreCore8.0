package org.apromore.bpmn_ui;

/*-
 * #%L
 * Apromore :: bpmn-ui
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

import com.google.common.io.ByteStreams;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.ui.bpmn_io.CustomZULComponent;
//import org.apromore.ui.spi.UIPluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
//import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 * Controller for the BPMN editor ZK component.
 */
public final class BPMNUIWindowController extends SelectorComposer<Component> {

    /** Logger. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(BPMNUIWindowController.class);

    /** Window. */
    @Wire("#bpmnUIWindow")
    private Window window;

    /** Embedded BPMN editor widget. */
    @Wire("#bpmn1")
    private CustomZULComponent bpmn1;

    /** The model (both in the BPMN and MVC senses). */
    private BPMNItem bpmnItem;

    @Override
    public void doFinally() {
        LOGGER.info("doFinally");
        window.addEventListener("onInit", new EventListener<Event>() {

            public void onEvent(final Event event) throws Exception {
                try {
                    LOGGER.info("Init " + event);
                    bpmnItem = (BPMNItem) event.getData();
                    bpmn1.setValue(new String(
                        ByteStreams.toByteArray(bpmnItem.getXMLSerialization()),
                        UTF_8));  // Uglier, but back-compatible to Java 1.8

                } catch (IOException e) {
                    LOGGER.error("Unable to convert BPMN", e);
                }
            }
        });
    }

    /** Save changes to the BPMN model. */
    @Listen("onClick = #saveButton")
    private void save() {
        try {
            Messagebox.show("Saved BPMN model", "Attention", Messagebox.OK,
                Messagebox.NONE);

        } catch (Exception e) {
            Messagebox.show("Save BPMN failed", "Attention", Messagebox.OK,
                Messagebox.ERROR);
            e.printStackTrace();
        }
    }

    /** Close the BPMN editor. */
    @Listen("onClick = #closeButton")
    public void close() {
        window.detach();
    }
}
