package org.apromore.item_ui;

import org.apromore.item.Item;
import org.apromore.item.ItemService;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

@Component(service = {UIPlugin.class})
public class SelectItemUIPlugin extends AbstractUIPlugin {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(SelectItemUIPlugin.class);

    @Reference
    private ItemService itemService;

    /*
    public SelectItemUIPlugin(ItemService itemService) {
        this.itemService = itemService;
    }
    */

    @Override
    public String getGroupLabel() {
        return "Item";
    }

    /** @return the text appearing on the plugin's menuitem */
    @Override
    public String getLabel() {
        return "Select Item";
    }

    /** @return whether the plugin is applicable to the given selection */
    @Override
    public boolean isEnabled(UIPluginContext context) {
        return true;
    }

    /** Invoked when the menu item is selected */
    @Override
    public void execute(UIPluginContext context) {
        Window window = (Window) context.createComponent(
            SelectItemUIPlugin.class.getClassLoader(),
            "zul/selectItem.zul",
            null);
        Listbox listbox = (Listbox) window.getFellow("listbox");

        listbox.setItemRenderer(new ListitemRenderer<Item>() {
            public void render(Listitem listitem, Item item, int index) {
                listitem.appendChild(new Listcell(Integer.valueOf(index)
                                                         .toString()));
                listitem.appendChild(new Listcell("" + item.getId()));
                listitem.appendChild(new Listcell(item.getType()));
                listitem.appendChild(new Listcell("-"));
            }
        });

        ListModelList<Item> model = new ListModelList<>();
        model.addAll(itemService.getAll());
        model.setSelection(context.getSelection());
        listbox.setModel(model);

        listbox.setPaginal((Paging) window.getFellow("paging"));

        listbox.addEventListener("onKeyPress", new EventListener<KeyEvent>() {
            public void onEvent(KeyEvent keyEvent) throws Exception {
                LOGGER.debug("Key press " + keyEvent);
                if ((keyEvent.isCtrlKey() && keyEvent.getKeyCode() == 'a')) {
                    if (listbox.getSelectedCount() > 0) {
                        listbox.selectAll();

                    } else {
                        listbox.clearSelection();
                    }
                }
            }
        });

        listbox.addEventListener("onSelect", new EventListener<SelectEvent>() {
            public void onEvent(SelectEvent selectEvent) throws Exception {
                context.setSelection(
                   ((Selectable<Item>) listbox.getModel()).getSelection()
                );
            }
        });
    }
}
