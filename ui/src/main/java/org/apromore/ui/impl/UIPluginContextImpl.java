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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import javax.security.auth.login.LoginException;
import org.apromore.Caller;
import org.apromore.ui.spi.UIPluginContext;
import org.apromore.user.UserService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.service.useradmin.User;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/** {@inheritDoc UIPluginContext}. */
class UIPluginContextImpl implements UIPluginContext {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(MenubarController.class);

    /**
     * Attribute key on the ZK {@link org.zkoss.zk.ui.Session} containing the
     * authenticated user as a username {@link String}.
     */
    public static final String ZK_SESSION_USER_ATTRIBUTE = "user";

    /** A canvas just about anyone is allowed to scribble upon. */
    private Component parent;

    /** Used for login. */
    private UserService userService;

    /**
     * @param newParent  parent for plugins to add content to
     * @param newUserService  used to authenicate login attempts
     */
    UIPluginContextImpl(final Component newParent,
                        final UserService newUserService) {

        this.parent = newParent;
        this.userService = newUserService;
    }

    @Override
    @SuppressWarnings("nullness")  // Executions.createComponentsDirectly
                                   // isn't annotated
    public Component createComponent(final ClassLoader classLoader,
                                     final String      uri,
                           @Nullable final Map<?, ?>   arguments) {
        try {
            InputStream in = classLoader.getResourceAsStream(uri);
            if (in == null) {
                throw new IOException("No resource " + uri
                    + " found in bundle classpath");
            }

            LOGGER.info("Parent = " + parent);
            LOGGER.info("Parent.children = " + parent.getChildren());
            parent.getChildren().clear();

            return Executions.createComponentsDirectly(
                new InputStreamReader(in, "UTF-8"),
                "zul",
                parent,
                arguments);

        } catch (IOException e) {
            throw new Error("ZUL resource " + uri
                + " could not be created as as ZK component", e);
        }
    }

    @Override
    public Component getParentComponent() {
        return parent;
    }

    @Override
    public void setComponent(final Component component) {
        parent.getChildren().clear();
        parent.getChildren().add(component);
    }

    @Override
    public Object getSessionAttribute(final String attribute) {

        return Sessions.getCurrent().getAttribute(attribute);
    }

    @Override
    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    public void putSessionAttribute(final String attribute,
                                    final Object newValue) {

        Sessions.getCurrent().setAttribute(attribute, newValue);
    }

    @Override
    @SuppressWarnings({"checkstyle:TodoComment", "nullness"})
    public void authenticate(final String reason, final Runnable success,
        final Runnable failure) {

        Window window;

        if (getUser() != null) {
            if (success != null) {
                success.run();
            }
            return;
        }

        try {
            Reader reader = new InputStreamReader(getClass()
                .getClassLoader()
                .getResourceAsStream("zul/login.zul"), "UTF-8");
            window = (Window) Executions.createComponentsDirectly(reader, "zul",
                null, null);

        } catch (IOException e) {
            throw new Error("ZUL resource login.zul could not be created as as "
                + "ZK component", e);
        }
        assert window != null;

        ((Label) window.getFellow("reasonLabel")).setValue(reason);

        window.getFellow("loginButton").addEventListener("onClick",
            new EventListener<Event>() {

            public void onEvent(final Event event) throws LoginException {
                String username =
                    ((Textbox) window.getFellow("username")).getValue();
                String password =
                    ((Textbox) window.getFellow("password")).getValue();
                LOGGER.debug("Login user " + username);
                User user = userService.authenticate(username, password);
                window.detach();
                setUser(user);

                // TODO: failure isn't invoked in the case of a
                // LoginException

                if (success != null) {
                    success.run();
                }
            }
        });

        window.getFellow("cancelButton").addEventListener("onClick",
            new EventListener<Event>() {

            public void onEvent(final Event event) throws Exception {
                window.detach();
                if (failure != null) {
                    failure.run();
                }
            }
        });

        window.doModal();
    }

    /**
     * @return the authenticated user, or <code>null</code> if the
     *     session isn't authenticated.
     */
    private static User getUser() {
        return (User) Sessions.getCurrent()
                              .getAttribute(ZK_SESSION_USER_ATTRIBUTE);
    }

    /**
     * Mutator for the <i>user</i> property.
     *
     * Queue "q" receives an "onLogin" event whenever the authenticated
     * user changes.
     *
     * @param user  the authenticated user, or <code>null</code> to
     *     deauthenticate this user session
     */
    private static void setUser(final User user) {
        Sessions.getCurrent().setAttribute(ZK_SESSION_USER_ATTRIBUTE, user);
        EventQueues.lookup("q", Sessions.getCurrent(), true)
                   .publish(new Event("onLogin"));
    }

    @Override
    public Caller caller() {
        return new org.apromore.AbstractCaller();
    }
}