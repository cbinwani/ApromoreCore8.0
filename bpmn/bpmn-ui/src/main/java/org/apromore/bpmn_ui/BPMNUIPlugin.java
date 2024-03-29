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

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.Properties;
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.bpmn_item.BPMNItemService;
import org.apromore.item.Item;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.Selection;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 * Plugin providing an viewer/editor for {@link BPMNItem}s.
 */
@Component(service = {UIPlugin.class})
public final class BPMNUIPlugin extends AbstractUIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(BPMNUIPlugin.class);

    /** Service used to obtain the {@link BPMNItem} to view/edit. */
    @Reference
    @SuppressWarnings("nullness")
    private BPMNItemService bpmnItemService;

    /** Sole constructor. */
    public BPMNUIPlugin() {
        super("item.group", "editBPMN.label", "editBPMN.iconSclass");
    }

    /** @return whether the selection is a single BPMN process model */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return Selection.getSelection()
                        .stream()
                        .filter(item -> BPMNItem.TYPE.equals(item.getType()))
                        .count() == 1;
    }

    /**
     * If the selection contains a single BPMN model, open it in the editor.
     *
     * Otherwise, present a message box explaining that the selection is
     * inappropriate.
     * This shouldn't normally happen, since {@link isEnabled} should prevent
     * the command being invoked in the presence of an inappropriate selection.
     */
    @Override
    public void execute(final UIPluginContext context) {
        LOGGER.info("BPMNUIPlugin execute");
        try {
            Iterator<Item> i = Selection.getSelection().iterator();
            if (!i.hasNext()) {
                Messagebox.show("Please select one BPMN model", "Attention",
                    Messagebox.OK, Messagebox.ERROR);
                return;
            }
            Item item = i.next();
            if (i.hasNext() || !(item instanceof BPMNItem)) {
                Messagebox.show("Please select just one BPMN model",
                    "Attention", Messagebox.OK, Messagebox.ERROR);
                return;

            }
            Reader reader = new InputStreamReader(
                BPMNUIPlugin.class
                    .getClassLoader()
                    .getResourceAsStream("zul/bpmnEditor.zul"),
                "UTF-8"
            );
            Properties properties = new Properties();
            properties.setProperty("test", "Test value");
            Window window = (Window) Executions.createComponentsDirectly(reader,
                "zul", context.getParentComponent(), properties);
            Executions.getCurrent().postEvent(
                new Event("onInit", window, item));
            LOGGER.info("BPMNUIPlugin executed");

            //new BPMNUIWindowController(context, (BPMNItem) item);

        } catch (Exception e) {
            Messagebox.show("Unable to edit BPMN model\n" + e.getMessage(),
                "Attention", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("Unable to edit BPMN model", e);
        }
    }
}
