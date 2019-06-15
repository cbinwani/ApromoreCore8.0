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

import org.apromore.item.ItemService;
import org.apromore.item.Selection;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.apromore.user.UserService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Messagebox;

/**
 * {@link UIPlugin} for the Item/Download Item command.
 */
@Component(service = {UIPlugin.class})
public final class DownloadItemUIPlugin extends AbstractUIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(DownloadItemUIPlugin.class);

    /**
     * Used to store uploaded items.
     */
    @Reference
    private ItemService itemService;

    /**
     * Used to prompt the user to login if they try to upload while
     * unauthenticated.
     */
    @Reference
    private UserService userService;

    /** Sole constructor. */
    public DownloadItemUIPlugin() {
        super("item.group", "downloadItem.label", "downloadItem.iconSclass");
    }

    /** {@inheritDoc}
     *
     * This implementation is always enabled.
     */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return !Selection.getSelection().isEmpty();
    }

    /** {@inheritDoc}
     *
     * This implementation downloads the selected items.
     */
    @Override
    @SuppressWarnings("checkstyle:JavadocMethod")  // buggy @inheritDoc warning
    public void execute(final UIPluginContext context) {
        Messagebox.show("Not implemented",
                        "Attention",
                        Messagebox.OK,
                        Messagebox.ERROR);
    }
}
