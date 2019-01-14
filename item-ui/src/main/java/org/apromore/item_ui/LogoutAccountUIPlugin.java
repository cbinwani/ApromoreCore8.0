package org.apromore.item_ui;

import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPluginContext;

public class LogoutAccountUIPlugin extends AbstractUIPlugin {

    @Override
    public String getGroupLabel() {
        return "Account";
    }

    /** @return the text appearing on the plugin's menuitem */
    @Override
    public String getLabel() {
        return "Logout";
    }

    /** @return whether the plugin is applicable to the given selection */
    @Override
    public boolean isEnabled(UIPluginContext context) {
        return context.getUser() != null;
    }

    /** Invoked when the menu item is selected */
    @Override
    public void execute(UIPluginContext context) {
        context.setUser(null);
    }
}
