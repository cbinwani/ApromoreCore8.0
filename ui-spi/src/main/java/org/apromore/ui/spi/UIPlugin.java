package org.apromore.ui.spi;

import java.awt.image.RenderedImage;

/**
 * Service provider interface for adding commands to the user interface.
 *
 * The expected implementation is to add menu items to invoke the
 * commands.
 */
public interface UIPlugin {

    /** @return the menu in which this plugin's menuitem will appear */
    String getGroupLabel();

    /** @return the icon appearing on the plugin's menuitem */
    RenderedImage getIcon();

    /** @return the text appearing on the plugin's menuitem */
    String getLabel();

    /**
     * @param context  provided by UI
     * @return whether the plugin is applicable to the given selection
     */
    boolean isEnabled(UIPluginContext context);

    /**
     * Invoked when the menu item is selected.
     *
     * @param context  provided by UI
     */
    void execute(UIPluginContext context);
}
