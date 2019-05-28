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

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.apromore.folder.FolderService;
import org.apromore.item.ItemService;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

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
     * This will be a {@link org.apromore.folder.Folder} instance, or
     * <code>null</code> if the user is at the root of the folder hierarchy.
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
        super("item.group", "selectItem.label", "selectItem.iconSclass");
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
        //refresh(context);

        try {
            Reader reader = new InputStreamReader(
                SelectItemUIPlugin.class
                    .getClassLoader()
                    .getResourceAsStream("zul/selectItem.zul"),
                "UTF-8"
            );
            Map<String, Object> arg = new HashMap<>();
            arg.put("UIPluginContext", context);
            arg.put("FolderService", folderService);
            arg.put("ItemService", itemService);
            Window window = (Window) Executions.createComponentsDirectly(reader,
                "zul", context.getParentComponent(), arg);

        } catch (IOException e) {
            throw new Error("ZUL resource login.zul could not be created as a "
                + "ZK component", e);
        }
    }
}
