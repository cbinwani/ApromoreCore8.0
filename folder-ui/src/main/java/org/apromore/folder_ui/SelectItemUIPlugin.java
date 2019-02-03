package org.apromore.folder_ui;

import org.apromore.folder.Folder;
import org.apromore.folder.FolderService;
import org.apromore.folder.FolderAlreadyExistsException;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

/**
 * {@link UIPlugin} for the Item/Select Item mkII command.
 */
@Component(service = {UIPlugin.class})
public final class SelectItemUIPlugin extends AbstractUIPlugin {

    /**
     * When creating a new folder, this is the highest number that
     * will be appended to the default name to avoid clashes.
     *
     * For instance, "New folder 100".
     */
    private static final int FOLDER_NAME_RETRY_LIMIT = 100;

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(SelectItemUIPlugin.class);

    /**
     * Session attribute for the user's current location in the folder
     * hierarchy.
     *
     * This will be a {@link Folder} instance, or <code>null</code> if the user
     * is at the root of the folder hierarchy.
     */
    public static final String USER_FOLDER_ATTRIBUTE = "user.folder";

    /** Used to access the details of the selected folders. */
    @Reference
    private FolderService folderService;

    /** Used to access the details of the selected items. */
    @Reference
    private ItemService itemService;

    /** Sole constructor. */
    public SelectItemUIPlugin() {
        super("item.group", "selectItem.label");
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
    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    @Override
    public void execute(final UIPluginContext context) {
        Window window = (Window) context.createComponent(
            SelectItemUIPlugin.class.getClassLoader(),
            "zul/selectItem.zul",
            null);

        Label currentFolderLabel =
            (Label) window.getFellow("currentFolderLabel");

        Folder currentFolder =
            (Folder) context.getSessionAttribute(USER_FOLDER_ATTRIBUTE);

        currentFolderLabel.setValue(currentFolder == null
                                    ? "/"
                                    : currentFolder.toString());

        Button createFolderButton =
            (Button) window.getFellow("createFolderButton");

        createFolderButton.addEventListener("onClick",
            new EventListener<MouseEvent>() {

            public void onEvent(final MouseEvent mouseEvent) throws Exception {
                int counter = 1;
                String newFolderName = "New folder";
                Folder parentFolder = (Folder)
                    context.getSessionAttribute(USER_FOLDER_ATTRIBUTE);

                // Beware: this loop is awful code and you need to be
                // particularly alert while modifying it
                do {
                    try {
                        folderService.createFolder(parentFolder, newFolderName);
                        return;

                    } catch (FolderAlreadyExistsException e) {
                        // Generate a new folder name and retry
                        newFolderName = "New folder " + counter;
                        continue;

                    } catch (Throwable e) {
                        LOGGER.info("Unable to create folder", e);
                        Messagebox.show("Unable to create folder\n"
                            + e.getMessage(),
                            "Attention", Messagebox.OK, Messagebox.ERROR);
                        return;
                    }
                } while (counter++ < FOLDER_NAME_RETRY_LIMIT);

                // Achievement!  Created more that FOLDER_NAME_RETRY_LIMIT
                // folders without renaming any.  Proud of yourself?
                Messagebox.show("Too many new folders.\n"
                    + "Rename or delete some folders.",
                    "Attention", Messagebox.OK, Messagebox.ERROR);
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
            Folder folder = (Folder)
                context.getSessionAttribute(USER_FOLDER_ATTRIBUTE);

            model.addAll(folderService.getItemsInFolder(folder));

        } catch (NotAuthorizedException e) {
            LOGGER.warn("Not authorized to read folder content", e);
            Messagebox.show("Not authorized to read folder content\n"
                            + e.getMessage(),
                            "Attention", Messagebox.OK, Messagebox.EXCLAMATION);
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
