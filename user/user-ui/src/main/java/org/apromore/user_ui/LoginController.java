package org.apromore.user_ui;

/*-
 * #%L
 * Apromore :: user :: user-ui
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

import java.util.ResourceBundle;
import javax.security.auth.login.LoginException;
import org.apromore.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * Controller for <code>login.zul</code>.
 */
public final class LoginController extends SelectorComposer<Window> {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(LoginController.class);

    /** @return localized text catalogue */
    public ResourceBundle getLabels() {
        return ResourceBundle.getBundle("Labels", Locales.getCurrent());
    }

    /** Username. */
    @Wire("#username")
    @SuppressWarnings("nullness")
    private Textbox usernameTextbox;

    /** Password. */
    @Wire("#password")
    @SuppressWarnings("nullness")
    private Textbox passwordTextbox;

    /**
     * @param event  clicked login button
     * @throws LoginException if login fails
     */
    @Listen("onClick = #loginButton")
    public void onClickLoginButton(final Event event) {
        try {
            UserService userService = (UserService)
                getSelf().getAttribute("UserService");
            Users.setUser(userService.authenticate(
                usernameTextbox.getValue(),
                passwordTextbox.getValue()));
            getSelf().detach();

        } catch (LoginException e) {
            Messagebox.show(getLabels().getString("login.failed_login"));
        }
    }

    /** @param event  clicked cancel button */
    @Listen("onClick = #cancelButton")
    public void onClickCancelButton(final Event event) {
        getSelf().detach();
    }
}
