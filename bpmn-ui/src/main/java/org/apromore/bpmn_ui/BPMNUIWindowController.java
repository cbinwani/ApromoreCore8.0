package org.apromore.bpmn_ui;

import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import org.apromore.bpmn_item.BPMNItem;
import org.apromore.ui.spi.CustomZULComponent;
import org.apromore.ui.spi.UIPluginContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
//import org.zkoss.zk.ui.select.annotation.Listen;
//import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 * Controller for the BPMN editor ZK component.
 */
public final class BPMNUIWindowController extends SelectorComposer<Component> {

    //@Wire("#bpmnUIWindow")
    //public Window window;

    //@Wire("#bpmn1")
    //public CustomZULComponent bpmn1;

    /**
     * @param context  utility service provided to
     *     {@link org.apromore.ui.spi.UIPlugin}s
     * @param bpmnItem  the BPMN model to view/edit
     * @throws IOException if the content <i>bpmnItem</i> can't be streamed
     */
    BPMNUIWindowController(final UIPluginContext context,
                           final BPMNItem bpmnItem) throws IOException {

        Window window = (Window) context.createComponent(
            BPMNUIWindowController.class.getClassLoader(),
            "zul/bpmnEditor.zul",
            null
        );
        CustomZULComponent bpmn1 =
            (CustomZULComponent) window.getFellow("bpmn1");
        bpmn1.setValue(new String(bpmnItem.getXMLSerialization()
                                          .readAllBytes(), UTF_8));

        ((Button) window.getFellow("saveButton")).addEventListener("onClick",
            new EventListener<Event>() {

            public void onEvent(final Event event) throws Exception {
                save();
            }
        });

        ((Button) window.getFellow("closeButton")).addEventListener("onClick",
            new EventListener<Event>() {

            public void onEvent(final Event event) throws Exception {
                window.detach();
            }
        });
    }

    /** Save changes to the BPMN model. */
    //@Listen("onClick = #save")
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

    /*
    @Listen("onClick = #cancel")
    public void cancel() {
        window.detach();
    }
    */
}
