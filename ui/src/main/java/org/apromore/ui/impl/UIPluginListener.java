package org.apromore.ui.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apromore.ui.spi.UIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

/**
 * Callbacks for dynamically binding {@link UIPlugin}s.
 */
public final class UIPluginListener {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(UIPluginListener.class);

    /** Listeners. */
    private final Set<Component> components = new HashSet<>();

    /** @param component  new listener */
    void addListener(final Component component) {
        LOGGER.debug("Add listener " + component);
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
        components.add(component);
    }

    /**
     * @param uiPlugin  the bound plugin
     * @param properties  extension point
     */
    public void onBind(final UIPlugin uiPlugin, final Map properties) {
        onEvent("onBind", uiPlugin, properties);
    }

    /**
     * @param uiPlugin  the unbound plugin
     * @param properties  extension point
     */
    public void onUnbind(final UIPlugin uiPlugin, final Map properties) {
        onEvent("onUnbind", uiPlugin, properties);
    }

    /**
     * @param eventName  only "onBind" or "onUnbind" are expected
     * @param uiPlugin  the bound or unbound plugin
     * @param properties  extension point
     */
    private void onEvent(final String eventName,
                         final UIPlugin uiPlugin,
                         final Map properties) {

        LOGGER.debug("Sending " + eventName + " event to " + uiPlugin
            + " with properties " + properties);

        for (final Component component: components) {
            Desktop desktop = component.getDesktop();
            if (desktop == null) {
                LOGGER.debug("Not notifying component because it has no "
                    + "desktop: " + component);

            } else {
                LOGGER.debug("Notifying component " + component);
                EventQueues.lookup("q", desktop.getSession(), true)
                           .publish(new Event(eventName, component, uiPlugin));
                LOGGER.debug("Notified component " + component);
            }
        }

        // Garbage-collect components no longer attached to any desktop
        components.removeAll(components.stream()
                                       .filter(c -> c.getDesktop() == null)
                                       .collect(Collectors.toSet()));

        LOGGER.debug("Sent " + eventName + " event to " + uiPlugin
            + " with properties " + properties);

    }
}
