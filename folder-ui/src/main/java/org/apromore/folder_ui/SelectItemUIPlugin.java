package org.apromore.folder_ui;

//import org.apromore.folder.Folder;
import org.apromore.folder.FolderService;
import org.apromore.item.Item;
import org.apromore.item.ItemService;
import org.apromore.item.NotAuthorizedException;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

/**
 * {@link UIPlugin} for the Item/Select Item mkII command.
 */
@Component(service = {UIPlugin.class})
public final class SelectItemUIPlugin extends AbstractUIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(SelectItemUIPlugin.class);

    /** Used to access the details of the selected folders. */
    @Reference
    private FolderService folderService;

    /** Used to access the details of the selected items. */
    @Reference
    private ItemService itemService;

    /**
     * Sole constructor.
     *
     * Hardcodes the label and group label as "Item/Select Item mkII".
     */
    public SelectItemUIPlugin() {
        this.groupLabel = "Item";
        this.label      = "Select Item mkII";
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

        Button createFolderButton =
            (Button) window.getFellow("createFolderButton");

        createFolderButton.addEventListener("onClick",
            new EventListener<MouseEvent>() {

            public void onEvent(final MouseEvent mouseEvent) throws Exception {
                LOGGER.info("Button click " + mouseEvent);
            }
        });

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
        try {
            model.addAll(folderService.getItemsInFolder(null));  // TODO

        } catch (NotAuthorizedException e) {
            // TODO: inform the user that they can't see inside this folder
            LOGGER.warn("Unable to read folder content", e);
        }

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
