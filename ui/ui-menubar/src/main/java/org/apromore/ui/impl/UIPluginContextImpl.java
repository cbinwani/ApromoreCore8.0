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
import java.util.Map;
import org.apromore.Caller;
import org.apromore.ui.spi.UIPluginContext;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.osgi.service.useradmin.Authorization;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

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

    /** Used to determine authorization. */
    private UserAdmin userAdmin;

    /**
     * @param newParent  parent for plugins to add content to
     * @param newUserAdmin  used to authorize access to business logic
     */
    UIPluginContextImpl(final Component newParent,
                        final UserAdmin newUserAdmin) {

        this.parent = newParent;
        this.userAdmin = newUserAdmin;
    }

    @Override
    @SuppressWarnings("nullness")  // Executions.createComponentsDirectly
                                   // isn't annotated
    public Component createComponent(final ClassLoader classLoader,
                                     final String      uri,
                           final @Nullable Map<?, ?>   arguments) {
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

    /**
     * @return the authenticated user, or <code>null</code> if the
     *     session isn't authenticated.
     */
    private static @Nullable User getUser() {
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
    @SuppressWarnings("nullness")  // Session.setAttribute isn't annotated
    private static void setUser(final @Nullable User user) {
        Sessions.getCurrent().setAttribute(ZK_SESSION_USER_ATTRIBUTE, user);
        EventQueues.lookup("q", Sessions.getCurrent(), true)
                   .publish(new Event("onLogin"));
    }

    @Override
    public Caller caller() {
        return new Caller() {
            /**
             * Evaluating the authorization here means that this instance
             * can be passed outside of the thread holding the ZK session.
             */
            @SuppressWarnings("nullness")  // UserAdmin.getAuthorization
                                           // isn't annotated
            private final Authorization authorization2 =
                userAdmin.getAuthorization(getUser());

            @Override
            public Authorization authorization() {
                return authorization2;
            }
        };
    }
}
