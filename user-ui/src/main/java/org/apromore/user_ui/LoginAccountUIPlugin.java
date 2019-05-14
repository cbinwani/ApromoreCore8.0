package org.apromore.user_ui;

import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.apromore.user.UserService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * {@link UIPlugin} for the Account/Login command.
 */
@Component(service = {UIPlugin.class})
public final class LoginAccountUIPlugin extends AbstractUIPlugin {

    /** The service used to authorize user sessions. */
    @Reference
    private UserService userService;

    /** Sole constructor. */
    public LoginAccountUIPlugin() {
        super("account.group", "loginAccount.label");
    }


    // Implementation overriding AbstractUIPlugin

    /** {@inheritDoc}
     *
     * This implementation is enabled whenever the user session isn't
     * authorized.
     */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return context.getUser() == null;
    }

    /** {@inheritDoc}
     *
     * This implementation prompts the user to authorize the user session.
     */
    @Override
    public void execute(final UIPluginContext context) {
        userService.authenticate(null, null, null);
    }
}
