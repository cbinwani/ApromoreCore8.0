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

/**
 * {@link UIPlugin} for the Item/Logout command.
 */
@Component(service = {UIPlugin.class})
public final class SelectItemUIPlugin extends AbstractUIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(SelectItemUIPlugin.class);

    /** Used to access the details of the selected items.. */
    @Reference
    private ItemService itemService;

    /*
    public SelectItemUIPlugin(ItemService itemService) {
        this.itemService = itemService;
    }
    */

    /** {@inheritDoc}
     *
     * This implementation hardcodes the value <code>"Item"</code>.
     */
    @Override
    public String getGroupLabel() {
        return "Item";
    }

    /** {@inheritDoc}
     *
     * This implementation hardcodes the value <code>"Select Item"</code>.
     */
    @Override
    public String getLabel() {
        return "Select Item";
    }

    /** {@inheritDoc}
     *
     * This implementation is always enabled.
     */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return true;
    }

    /** {@inheritDoc}
     *
     * This implementation displays a listbox of the selected items.
     */
    @Override
    public void execute(final UIPluginContext context) {
        Window window = (Window) context.createComponent(
            SelectItemUIPlugin.class.getClassLoader(),
            "zul/selectItem.zul",
            null);
        Listbox listbox = (Listbox) window.getFellow("listbox");

        listbox.setItemRenderer(new ListitemRenderer<Item>() {
            public void render(final Listitem listitem,
                               final Item     item,
                               final int      index) {

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
            public void onEvent(final KeyEvent keyEvent) throws Exception {
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
            public void onEvent(final SelectEvent selectEvent)
                throws Exception {

                context.setSelection(
                   ((Selectable<Item>) listbox.getModel()).getSelection()
                );
            }
        });
    }
}
