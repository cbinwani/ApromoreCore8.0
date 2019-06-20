package org.apromore.ui.impl;

/*-
 * #%L
 * Apromore :: ui
 * %%
 * Copyright (C) 2019 The Apromore Initiative
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.lang.Library;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;

/**
 * Controller for the landing page.
 */
public final class MainWindowController extends SelectorComposer<Component> {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(MainWindowController.class);

    @Override
    public ComponentInfo doBeforeCompose(final Page page,
        final Component component,
        final ComponentInfo info) {

        super.doBeforeCompose(page, component, info);

        // Propagate the ui.theme configuration property to ZK
        Library.setProperty("org.zkoss.theme.preferred",
            (String) Util.blueprintContainer(component)
                         .getComponentInstance("uiTheme"));

        // Propagate the ui.sessionTimeout configuration property to ZK
        try {
            String sessionTimeout = (String)
                Util.blueprintContainer(component)
                    .getComponentInstance("uiSessionTimeout");

            if (sessionTimeout != null) {
                Sessions.getCurrent()
                        .getWebApp()
                        .getConfiguration()
                        .setDesktopMaxInactiveInterval(
                            Integer.parseInt(sessionTimeout)
                        );
            }

        } catch (NumberFormatException e) {
            LOGGER.warn("Malformed session timeout in configuration", e);
        }

        return info;
    }
}
