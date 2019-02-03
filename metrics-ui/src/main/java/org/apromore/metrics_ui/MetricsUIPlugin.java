/*
 * Copyright © 2009-2018 The Apromore Initiative.
 *
 * This file is part of "Apromore".
 *
 * "Apromore" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore" is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package org.apromore.metrics_ui;

// Java 2 Standard Edition packages
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

// Third party packages
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListModelMap;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

// Local packages
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.item.Item;
import org.apromore.service.metrics.MetricsService;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.apromore.xes_item.XESItem;

/**
 * Metrics service.
 *
 * @author Adriano Augusto 18/04/2016
 */
@Component(service = {UIPlugin.class})
public final class MetricsUIPlugin extends AbstractUIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(MetricsUIPlugin.class);

    /** Used to calculate measurements on the selected item. */
    @Reference
    private MetricsService metricsService;

    /** Sole constructor. */
    public MetricsUIPlugin() {
        super("analyze.group", "measure.label");
    }

    /** {@inheritDoc}
     *
     * This implementation is enabled if a single BPMN model or XES log is
     * selection.
     */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return context.getSelection()
                      .stream()
                      .filter(item -> Arrays.asList(BPMNItem.TYPE, XESItem.TYPE)
                                            .contains(item.getType()))
                      .count() == 1;
    }

    /** {@inheritDoc}
     *
     * This implementation calculates the measurements of the selected item
     * and presents it to the viewer.
     */
    @Override
    public void execute(final UIPluginContext context) {

        Set<Item> selection = context.getSelection();
        if (selection.size() != 1) {
            Messagebox.show("Please select just one item", "Attention",
                Messagebox.OK, Messagebox.ERROR);
            return;
        }

        Item item = selection.stream().findAny().get();
        switch (item.getType()) {
        case BPMNItem.TYPE:
            configureProcessComputation((BPMNItem) item, context);
            break;

        case XESItem.TYPE:
            runLogComputation((XESItem) item, context);
            break;

        default:
            Messagebox.show("Unsupported item type: " + item.getType(),
                "Attention", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Prompt the user for which measurements are desired, then present the
     * selected measurements.
     *
     * @param bpmnItem  the BPMN process to measure
     * @param context used to display the results window
     */
    private void configureProcessComputation(final BPMNItem bpmnItem,
                                             final UIPluginContext context) {

        Window window = (Window) context.createComponent(
            MetricsUIPlugin.class.getClassLoader(), "zul/metrics.zul", null);

        ((Button) window.getFellow("OKButton")).addEventListener("onClick",
            new EventListener<Event>() {

            public void onEvent(final Event event) throws Exception {
                runProcessComputation(bpmnItem, context, window);
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

    /**
     * Present the measurements of a BPMN process to the user.
     *
     * @param bpmnItem a BPMN process
     * @param context used to display the results window
     * @param w  the settings window, containing the various radio buttons which
     *     determine which measurements to calculate
     */
    private void runProcessComputation(final BPMNItem        bpmnItem,
                                       final UIPluginContext context,
                                       final Window          w) {

        Map<String, String> bpmnMetrics = metricsService.computeMetrics(
            bpmnItem.getBPMNDiagram(), f(w, "size"), f(w, "cfc"), f(w, "acd"),
            f(w, "mcd"), f(w, "cnc"), f(w, "density"), f(w, "structuredness"),
            f(w, "separability"), f(w, "duplicates"));

        if (bpmnMetrics == null) {
            Messagebox.show("Unable to compute metrics", "Attention",
                Messagebox.OK, Messagebox.ERROR);
            return;
        }

        // Present the map of results in a window
        Window window = (Window) context.createComponent(
            MetricsUIPlugin.class.getClassLoader(), "zul/metrics2.zul", null);
        Listbox listbox = (Listbox) window.getFellow("listbox");
        listbox.setModel(new ListModelMap(bpmnMetrics));
    }

    /**
     * @param settings   the settings window
     * @param metricName  key for a particular named setting
     * @return whether the radio button corresponding to the <i>key</i> is
     *     selected
     */
    private static boolean f(final Window settings, final String metricName) {
        return ((Radiogroup) settings.getFellow(metricName))
                   .getSelectedIndex() == 0;
    }

    /**
     * Present the measurements of an XES log to the user.
     *
     * @param xesItem an XES log
     * @param context used to display the results window
     */
    private void runLogComputation(final XESItem xesItem,
                                   final UIPluginContext context) {

        Map<String, String> xesMetrics =
            metricsService.computeMetrics(xesItem.getXLog());

        if (xesMetrics == null) {
            Messagebox.show("Unable to compute metrics", "Attention",
                Messagebox.OK, Messagebox.ERROR);
            return;
        }

        // Present the map of results in a window
        Window window = (Window) context.createComponent(
            MetricsUIPlugin.class.getClassLoader(), "zul/metrics2.zul", null);
        Listbox listbox = (Listbox) window.getFellow("listbox");
        listbox.setModel(new ListModelMap(xesMetrics));
    }
}
