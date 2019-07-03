package org.apromore.user_ui;

/*-
 * #%L
 * Apromore :: user-ui
 * %%
 * Copyright (C) 2019 The Apromore Initiative
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
        super("account.group", "loginAccount.label", "loginAccount.iconSclass");
    }


    // Implementation overriding AbstractUIPlugin

    /** {@inheritDoc}
     *
     * This implementation is enabled whenever the user session isn't
     * authorized.
     */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return Users.getUser() == null;
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
