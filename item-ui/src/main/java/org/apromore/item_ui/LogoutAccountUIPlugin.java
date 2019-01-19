package org.apromore.item_ui;

import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;

@Component(service = {UIPlugin.class})
public final class LogoutAccountUIPlugin extends AbstractUIPlugin {

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
    public boolean isEnabled(final UIPluginContext context) {
        return context.getUser() != null;
    }

    /** Invoked when the menu item is selected */
    @Override
    public void execute(final UIPluginContext context) {
        context.setUser(null);
    }
}
