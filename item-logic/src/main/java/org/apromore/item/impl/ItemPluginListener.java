package org.apromore.item.impl;

/*-
 * #%L
 * Apromore :: item
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

import java.util.Map;
import org.apromore.item.spi.ItemPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Callbacks for dynamically binding {@link ItemPlugin}s.
 */
public final class ItemPluginListener {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(ItemPluginListener.class);

    /**
     * @param itemPlugin  the bound plugin
     * @param properties  extension point
     */
    public void onBind(final ItemPlugin itemPlugin, final Map properties) {
        LOGGER.debug("Bind item plugin " + itemPlugin + " with properties "
                     + properties);
        /*
        for (Component component: components) {
            Desktop desktop = component.getDesktop();
            if (desktop == null) {
                LOGGER.warn("Not notifying component because it has no "
                            "desktop: " + component);

            } else {
                LOGGER.debug("Notifying component " + component);
                EventQueues.lookup("q", desktop.getSession(), true)
                           .publish(new Event("onBind", component, uiPlugin));
                LOGGER.debug("Notified component " + component);
            }
        }
        */
    }

    /**
     * @param itemPlugin  the unbound plugin
     * @param properties  extension point
     */
    public void onUnbind(final ItemPlugin itemPlugin, final Map properties) {
        LOGGER.debug("Unbind item plugin " + itemPlugin + " with properties "
                     + properties);
        /*
        for (Component component: components) {
            Desktop desktop = component.getDesktop();
            if (desktop == null) {
                LOGGER.warn("Not notifying component because it has no "
                            "desktop: " + component);

            } else {
                LOGGER.debug("Notifying component " + component);
                EventQueues.lookup("q", desktop.getSession(), true)
                           .publish(new Event("onUnbind", component, uiPlugin));
                LOGGER.debug("Notified component " + component);
            }
        }
        */
    }
}
