package org.apromore.bpmn_ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.bpmn_item.BPMNItemService;
import org.apromore.item.Item;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.zkoss.zul.Messagebox;

@Component(service = {UIPlugin.class})
public class BPMNUIPlugin extends AbstractUIPlugin {

    @Reference
    private BPMNItemService bpmnItemService;

    @Override
    public String getGroupLabel() {
        return "Item";
    }

    @Override
    public String getLabel() {
        return "Edit BPMN";
    }

    /** @return whether the selection is a single BPMN process model */
    @Override
    public boolean isEnabled(UIPluginContext context) {
        return context.getSelection()
                      .stream()
                      .filter(item -> "BPMN 2.0".equals(item.getType()))
                      .count() == 1;
    }

    /** Invoked when the menu item is selected */
    @Override
    public void execute(UIPluginContext context) {
        try {
            Iterator<Item> i = context.getSelection().iterator();
            if (!i.hasNext()) {
                Messagebox.show("Please select one BPMN model", "Attention", Messagebox.OK, Messagebox.ERROR);
                return;
            }
            Item item = i.next();
            if (i.hasNext() || !(item instanceof BPMNItem)) {
                Messagebox.show("Please select just one BPMN model", "Attention", Messagebox.OK, Messagebox.ERROR);

            } else {
                new BPMNUIWindowController(context, (BPMNItem) item);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
