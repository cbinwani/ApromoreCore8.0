package org.apromore.user;

/*-
 * #%L
 * Apromore :: user-api
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

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

/**
 * {@link User} attributes of the ZK session.
 */
public abstract class Users {

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
    public static User getUser() {
        return (User) Sessions.getCurrent().getAttribute(ATTRIBUTE);
    }

    /**
     * @param newUser  the new authenticated user, or <code>null</code> to
     *     de-authenticate the current user session (i.e. log out).
     */
    public static void setUser(final User newUser) {
        Sessions.getCurrent().setAttribute(ATTRIBUTE, newUser);
        EventQueues.lookup("q", Sessions.getCurrent(), true)
                   .publish(new Event("onLogin"));
    }
}
