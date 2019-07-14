package org.apromore.folder_ui;

/*-
 * #%L
 * Apromore :: folder-ui
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

import java.util.List;
import java.util.ResourceBundle;
import org.apromore.folder.Folder;
import org.apromore.folder.FolderService;
import org.apromore.folder.PathAlreadyExistsException;
import org.apromore.item.Item;
import org.apromore.item.ItemService;
import org.apromore.item.NotAuthorizedException;
import org.apromore.ui.spi.Selection;
import org.apromore.ui.spi.UIPluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
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
 * Controller for <code>selectItem.zul</code>.
 */
public class SelectItemController extends SelectorComposer<Component> {

    /**
     * When creating a new folder, this is the highest number that
     * will be appended to the default name to avoid clashes.
     *
     * For instance, "New folder 100".
     */
    private static final int FOLDER_NAME_RETRY_LIMIT = 100;

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(SelectItemController.class);

    /**
     * Session attribute for the user's current location in the folder
     * hierarchy.
     *
     * This will be a {@link Folder} instance, or <code>null</code> if the user
     * is at the root of the folder hierarchy.
     */
    public static final String USER_FOLDER_ATTRIBUTE = "user.folder";

    /** @return localized text catalogue */
    public ResourceBundle getLabels() {
        return ResourceBundle.getBundle("Labels", Locales.getCurrent());
    }

    /** Window. */
    @Wire
    @SuppressWarnings("nullness")
    private Window win;

    /** UI plugin context. */
    @SuppressWarnings("nullness")
    private UIPluginContext context = (UIPluginContext)
        Executions.getCurrent().getArg().get("UIPluginContext");

    /** Used to access the details of the selected folders. */
    @SuppressWarnings("nullness")
    private FolderService folderService = (FolderService)
        Executions.getCurrent().getArg().get("FolderService");

    /** Used to access the details of the selected items. */
    @SuppressWarnings("nullness")
    private ItemService itemService = (ItemService)
        Executions.getCurrent().getArg().get("ItemService");

    /** @param context2 */
    private void refresh(final UIPluginContext context2) {
    }

    /** @param mouseEvent  clicked home */
    @Listen("onClick = #homeButton")
    @SuppressWarnings("nullness")
    public void onClickHomeButton(final MouseEvent mouseEvent) {
        context.putSessionAttribute(USER_FOLDER_ATTRIBUTE, null);
        refresh(context);
    }

    /** @param mouseEvent  clicked create folder */
    @Listen("onClick = #createFolderButton")
    public void onClickCreateFolderButton(final MouseEvent mouseEvent) {
        int counter = 1;
        String newFolderName = "New folder";
        Folder parentFolder = (Folder)
            context.getSessionAttribute(USER_FOLDER_ATTRIBUTE);

        // Beware: this loop is awful code and you need to be
        // particularly alert while modifying it
        do {
            try {
                folderService.createFolder(parentFolder,
                                           newFolderName,
                                           context.caller());
                refresh(context);
                return;

            } catch (PathAlreadyExistsException e) {
                // Generate a new folder name and retry
                counter++;
                newFolderName = "New folder " + counter;
                continue;

            } catch (Throwable e) {
                LOGGER.info("Unable to create folder", e);
                Messagebox.show("Unable to create folder\n"
                    + e.getMessage(),
                    "Attention", Messagebox.OK, Messagebox.ERROR);
                return;
            }
        } while (counter <= FOLDER_NAME_RETRY_LIMIT);

        // Achievement!  Created more than FOLDER_NAME_RETRY_LIMIT
        // folders without renaming any.  Proud of yourself?
        Messagebox.show("Too many new folders.\n"
            + "Rename or delete some folders.",
            "Attention", Messagebox.OK, Messagebox.ERROR);
    }

    /** @param mouseEvent  clicked enter folder */
    @Listen("onClick = #enterFolderButton")
    public void onClickEnterFolderButton(final MouseEvent mouseEvent) {
        for (Item selectedItem: Selection.getSelection()) {
            if (selectedItem instanceof Folder) {
                LOGGER.info("Entering folder " + selectedItem.getId());
                context.putSessionAttribute(USER_FOLDER_ATTRIBUTE,
                    selectedItem);
                refresh(context);

            } else {
                Messagebox.show("Selection is not a folder",
                    "Attention", Messagebox.OK, Messagebox.ERROR);
            }
        }
    }

    /** @param mouseEvent  clicked remove item */
    @Listen("onClick = #removeItemButton")
    public void onClickRemoveItemButton(final MouseEvent mouseEvent) {

        Folder folder = (Folder)
            context.getSessionAttribute(USER_FOLDER_ATTRIBUTE);

        for (Item selectedItem: Selection.getSelection()) {
            LOGGER.info("Removing " + selectedItem);
            //folderService.removePath(folder, );
        }
    }

    /** Current folder label. */
    @Wire("#currentFolderLabel")
    @SuppressWarnings("nullness")
    private Label currentFolderLabel;

    /** Listbox. */
    @Wire("#listbox")
    @SuppressWarnings("nullness")
    private Listbox listbox;

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    public void doFinally() {

        //Label currentFolderLabel =
        //    (Label) window.getFellow("currentFolderLabel");
        Folder currentFolder =
            (Folder) context.getSessionAttribute(USER_FOLDER_ATTRIBUTE);
        currentFolderLabel.setValue(currentFolder == null
            ? "/"
            : folderService.findPathByItem(currentFolder, context.caller()));

        //Listbox listbox = (Listbox) window.getFellow("listbox");
        listbox.setItemRenderer(new ListitemRenderer<Item>() {
            public void render(final Listitem listitem,
                               final Item     item,
                               final int      index) {

                String name = "error";
                Long itemId = null;
                String type = "error";

                if (item != null) {
                    String path =
                        folderService.findPathByItem(item, context.caller());
                    name = path.substring(path.lastIndexOf("/") + 1,
                        path.length());
                    itemId = item.getId();
                    type = item.getType();
                }

                listitem.appendChild(new Listcell(Integer.valueOf(index)
                                                         .toString()));
                listitem.appendChild(new Listcell(name));
                listitem.appendChild(new Listcell("" + itemId));
                listitem.appendChild(new Listcell(type));
                listitem.appendChild(new Listcell("-"));
            }
        });

        ListModelList<Item> model = new ListModelList<>();

        Folder folder = (Folder)
            context.getSessionAttribute(USER_FOLDER_ATTRIBUTE);
        List<String> paths;
        if (folder == null) {
            paths = folderService.getRootFolderPaths(context.caller());
            LOGGER.info("Got root folder paths " + paths);

        } else {
            paths = folder.getPaths();
            LOGGER.info("Got folder paths " + paths);
        }

        for (String path: paths) {
            try {
                Item item =
                    folderService.findItemByFolderAndName(folder,
                                                          path,
                                                          context.caller());
                if (item == null) {
                    throw new AssertionError("getPaths returned non-Item");
                }
                model.add(item);

            } catch (NotAuthorizedException e) {
                // Silently hide unauthorized content
                LOGGER.warn("Unauthorized path " + path, e);
            }
        }

        model.setSelection(Selection.getSelection());
        listbox.setModel(model);

        listbox.setPaginal((Paging) win.getFellow("paging"));

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
    }
}
