package org.apromore.bpmn_ui;

import java.util.Iterator;
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.bpmn_item.BPMNItemService;
import org.apromore.item.Item;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Messagebox;

/**
 * Plugin providing an viewer/editor for {@link BPMNItem}s.
 */
@Component(service = {UIPlugin.class})
public final class BPMNUIPlugin extends AbstractUIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(AbstractUIPlugin.class);

    /** Service used to obtain the {@link BPMNItem} to view/edit. */
    @Reference
    private BPMNItemService bpmnItemService;

    /** Sole constructor. */
    public BPMNUIPlugin() {
        super("item.group", "editBPMN.label");
    }

    /** @return whether the selection is a single BPMN process model */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return context.getSelection()
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
        try {
            Iterator<Item> i = context.getSelection().iterator();
            if (!i.hasNext()) {
                Messagebox.show("Please select one BPMN model", "Attention",
                    Messagebox.OK, Messagebox.ERROR);
                return;
            }
            Item item = i.next();
            if (i.hasNext() || !(item instanceof BPMNItem)) {
                Messagebox.show("Please select just one BPMN model",
                    "Attention", Messagebox.OK, Messagebox.ERROR);

            } else {
                new BPMNUIWindowController(context, (BPMNItem) item);
            }

        } catch (Exception e) {
            Messagebox.show("Unable to edit BPMN model\n" + e.getMessage(),
                "Attention", Messagebox.OK, Messagebox.ERROR);
            LOGGER.error("Unable to edit BPMN model", e);
        }
    }
}
