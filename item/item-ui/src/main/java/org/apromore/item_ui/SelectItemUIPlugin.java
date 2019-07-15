package org.apromore.item_ui;

/*-
 * #%L
 * Apromore :: item-ui
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

import org.apromore.Caller;
import org.apromore.NotAuthorizedException;
import org.apromore.item.Item;
import org.apromore.item.ItemService;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.Selection;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import static org.osgi.service.event.EventConstants.EVENT_TOPIC;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
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
 * {@link UIPlugin} for the Item/Select Item command.
 *
 * This class forwards OSGi EventAdmin to a ZK EventQueue, since ZK can
 * only perform updates within its own {@link EventListener}s.
 */
@Component(
    service = {EventHandler.class, UIPlugin.class},
    property = EVENT_TOPIC + "=org/apromore/item/*"
)
public final class SelectItemUIPlugin extends AbstractUIPlugin
    implements EventHandler {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(SelectItemUIPlugin.class);

    /** Used to access the details of the selected items. */
    @Reference
    @SuppressWarnings("nullness")
    private ItemService itemService;

    /** Sole constructor. */
    public SelectItemUIPlugin() {
        super("item.group", "selectItem.label", "selectItem.iconSclass");
    }

    /**
     * @param model  a list of items to update against {@link #itemService}
     * @param caller  authority to access items
     * @throws NotAuthorizedException if the <i>caller</i> lacks authority
     *     to access the list of items
     */
    private void updateModel(final ListModelList<Item> model,
                             final Caller caller)
        throws NotAuthorizedException {

        model.clear();
        model.addAll(itemService.getAll(caller));
        model.setSelection(Selection.getSelection());
    }


    // Implementation of EventHandler

    @Override
    public void handleEvent(final Event event) {
        EventQueues.lookup("q", EventQueues.APPLICATION, true).publish(
            new org.zkoss.zk.ui.event.Event(event.getTopic()));
    }


    // Implementation of UIPlugin

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
    public void execute(final UIPluginContext context)
        throws NotAuthorizedException {

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
        updateModel(model, context.caller());
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

                Selection.setSelection(
                   ((Selectable<Item>) listbox.getModel()).getSelection()
                );
            }
        });

        EventQueues.lookup("q", EventQueues.APPLICATION, true)
                   .subscribe(new EventListener<org.zkoss.zk.ui.event.Event>() {

            public void onEvent(final org.zkoss.zk.ui.event.Event event)
                throws Exception {

                switch (event.getName()) {
                case "org/apromore/item/CREATE":
                case "org/apromore/item/REMOVE":
                    updateModel(model, context.caller());
                    break;
                default:
                }
            }
        });
    }
}
