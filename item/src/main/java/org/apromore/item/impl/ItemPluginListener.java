package org.apromore.item.impl;

import java.util.Map;
import org.apromore.item.spi.ItemPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ItemPluginListener {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(ItemPluginListener.class);

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
