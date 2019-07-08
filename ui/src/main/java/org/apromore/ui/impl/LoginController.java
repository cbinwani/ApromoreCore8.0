package org.apromore.ui.impl;

/*-
 * #%L
 * Apromore :: ui
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

import java.security.Principal;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * Controller for <code>login.zul</code>.
 */
public final class LoginController extends SelectorComposer<Component> {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(LoginController.class);

    /**
     * Attribute key on the ZK {@link org.zkoss.zk.ui.Session} containing the
     * authenticated user as a username {@link String}.
     */
    public static final String ZK_SESSION_USER_ATTRIBUTE = "user";

    /** @see javax.security.auth.login.Configuration */
    private String loginConfigurationName = "Uninitialized";;

    /** Which {@link Principal} class identifies a user?
     *
     * These classes are typically proprietary to the login module.
     * There may be more than one reasonable choice, e.g.
     * <code>com.sun.security.auth.UserPrincipal</code> with name
     * <code>jsmith</code> versus
     * <code>com.sun.security.auth.LdapPrincipal</code> with name
     * <code>uid=jsmith,ou=staff,o=acme</code>.
     */
    private Class userPrincipalClass = Object.class;

    /** Login window. */
    @Wire
    @SuppressWarnings("nullness")
    private Window window;

    /** Reason. */
    @Wire("#reasonLabel")
    @SuppressWarnings("nullness")
    private Label reasonLabel;

    /** Username. */
    @Wire("#username")
    @SuppressWarnings("nullness")
    private Textbox usernameTextbox;

    /** Password. */
    @Wire("#password")
    @SuppressWarnings("nullness")
    private Textbox passwordTextbox;

    @Override
    public void doFinally() {
        String reason = "Dummy value";
        reasonLabel.setValue(reason);
    }

    /**
     * @param event  clicked login button
     * @throws LoginException if login fails
     */
    @Listen("onClick = #loginButton")
    @SuppressWarnings("checkstyle:TodoComment")
    public void onClickLoginButton(final Event event) throws LoginException {

                String username = usernameTextbox.getValue();
                String password = passwordTextbox.getValue();
                LOGGER.debug("Login user " + username);

                LoginContext loginContext = new LoginContext(
                    loginConfigurationName, new CallbackHandler() {

                    public void handle(final Callback[] callbacks)
                        throws UnsupportedCallbackException {

                        for (Callback callback: callbacks) {
                            if (callback instanceof NameCallback) {
                                ((NameCallback) callback).setName(username);

                            } else if (callback instanceof PasswordCallback) {
                                ((PasswordCallback) callback).setPassword(
                                    password.toCharArray()
                                );

                            } else {
                                throw new UnsupportedCallbackException(callback,
                                    "Unimplemented callback");
                            }
                        }
                    }
                });

                loginContext.login();
                try {
                    for (Principal principal: loginContext.getSubject()
                                                          .getPrincipals()) {
                        LOGGER.debug("Principal: " + principal + "  class: "
                            + principal.getClass());
                    }

                    final String user = loginContext
                        .getSubject()
                        .getPrincipals()
                        .stream()
                        .filter(principal -> userPrincipalClass
                            .isAssignableFrom(principal.getClass()))
                        .findAny()  // TODO: validate a unique result
                        .get()
                        .getName();
                    window.detach();
                    //userService.setUser(user);

                    // TODO: failure isn't invoked in the case of a
                    // LoginException

                } finally {
                    loginContext.logout();
                }

/*
                if (success != null) {
                    success.run();
                }
*/
            }

    /** @param event  clicked cancel button */
    @Listen("onClick = #cancelButton")
    public void onClickCancelButton(final Event event) /* throws Exception */ {

        window.detach();
/*
        if (failure != null) {
            failure.run();
        }
*/
    }
}
