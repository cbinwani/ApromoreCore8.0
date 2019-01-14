package org.apromore.ui.spi;

import java.awt.image.RenderedImage;

public interface UIPlugin {

    /** @return the menu in which this plugin's menuitem will appear */
    public String getGroupLabel();

    /** @return the icon appearing on the plugin's menuitem */
    public RenderedImage getIcon();

    /** @return the text appearing on the plugin's menuitem */
    public String getLabel();

    /**
     * @param context  provided by UI
     * @return whether the plugin is applicable to the given selection
     */
    public boolean isEnabled(UIPluginContext context);

    /*
     * Invoked when the menu item is selected
     *
     * @param context  provided by UI
     */
    public void execute(UIPluginContext context);
}
