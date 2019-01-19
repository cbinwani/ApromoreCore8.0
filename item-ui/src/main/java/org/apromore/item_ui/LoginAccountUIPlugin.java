package org.apromore.item_ui;

import org.apromore.ui.UIService;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = {UIPlugin.class})
public class LoginAccountUIPlugin extends AbstractUIPlugin {

    @Reference
    private UIService uiService;

    public LoginAccountUIPlugin() {
        this.groupLabel = "Account";
        this.label = "Login";
    }


    // Implementation overriding AbstractUIPlugin

    /** @return whether the plugin is applicable to the given selection */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return context.getUser() == null;
    }

    /** Invoked when the menu item is selected */
    @Override
    public void execute(final UIPluginContext context) {
        uiService.authenticate(null, null, null);
    }
}
