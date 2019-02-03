package org.apromore.item_ui;

import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;

/**
 * {@link UIPlugin} for the Account/Logout command.
 */
@Component(service = {UIPlugin.class})
public final class LogoutAccountUIPlugin extends AbstractUIPlugin {

    /** Sole constructor. */
    public LogoutAccountUIPlugin() {
        super("account.group", "logoutAccount.label");
    }

    /** {@inheritDoc}
     *
     * This implementation is enabled while the user session is authorized.
     */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return context.getUser() != null;
    }

    /** {@inheritDoc}
     *
     * This implementation de-authorizes the user session.
     */
    @Override
    public void execute(final UIPluginContext context) {
        context.setUser(null);
    }
}
