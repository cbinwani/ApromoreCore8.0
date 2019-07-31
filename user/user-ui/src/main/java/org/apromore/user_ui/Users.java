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

//import java.util.Arrays;
import javax.servlet.http.HttpSession;
import org.apromore.ui.session.UISession;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.osgi.service.useradmin.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

/**
 * {@link User} attributes of the ZK session.
 */
public abstract class Users {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(Users.class);

    /**
     * Key of the authenticated user attribute on the ZK session.
     *
     * The attribute is <code>null</code> if the session is unauthenticated.
     */
    private static final String ATTRIBUTE = "user";

    /**
     * @return the currently authenticated user, or <code>null</code> for an
     *     anonymous user session
     */
    public static @Nullable User getUser() {
        //return (User) Sessions.getCurrent().getAttribute(ATTRIBUTE);
        return (User) uiSession().get(ATTRIBUTE);
    }

    /**
     * Change the authenticated user.
     *
     * The {@link org.zkoss.zk.ui.event.EventQueue} "q" will receive an
     * "onLogin" {@link Event}.
     *
     * @param newUser  the new authenticated user, or <code>null</code> to
     *     de-authenticate the current user session (i.e. log out).
     */
    @SuppressWarnings("nullness")  // Session.setAttribute not annotated
    public static void setUser(final @Nullable User newUser) {
        //Sessions.getCurrent().setAttribute(ATTRIBUTE, newUser);
        uiSession().put(ATTRIBUTE, newUser);

        EventQueues.lookup("q", Sessions.getCurrent(), true)
                   .publish(new Event("onLogin"));
    }

    /** @return the singleton session attribute map */
    private static UISession uiSession() {
        LOGGER.info("UISESSIONing");
        HttpSession httpSession = (HttpSession)
            Sessions.getCurrent().getNativeSession();
        LOGGER.info("UISESSIONing HTTP session {}", httpSession);
        BundleContext bundleContext = (BundleContext)
            httpSession.getServletContext().getAttribute("osgi-bundlecontext");
        LOGGER.info("UISESSIONing bundle context {}", bundleContext);
        /*
        UISession result = (UISession)
            Arrays.asList(bundleContext.getBundle().getRegisteredServices())
                  .stream()
                  .filter(ref -> ref instanceof BlueprintContainer)
                  .map(ref -> (BlueprintContainer) ref)
                  .findAny()
                  .get()
                  .getComponentInstance("uiSession");
        */
        for (ServiceReference ref: bundleContext.getBundle()
                                                .getRegisteredServices()) {
            LOGGER.info("UISESSIONed reference {}", ref);
            Object service = bundleContext.getService(ref);
            if (service instanceof BlueprintContainer) {
                LOGGER.info("UISESSIONed component ids "
                    + ((BlueprintContainer) service).getComponentIds());
                return (UISession) ((BlueprintContainer) service)
                    .getComponentInstance("uiSession");
            }
        }

        throw new RuntimeException("Unable to find a BlueprintContainer");
    }
}
