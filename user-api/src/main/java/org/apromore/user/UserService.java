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

/**
 * User account services.
 */
public interface UserService {

    /**
     * Prompt the user to log in.
     *
     * @param reason  explanation to the user of why what they want to do
     *     requires then to authenticate
     * @param success  executed after the user successfully logs in
     * @param failure  executed after the login is cancelled
     */
    void authenticate(String reason, Runnable success, Runnable failure);

    /**
     * Verify that a given permission is granted to the current user session.
     *
     * @param permission  a named permission, e.g. <code>"create"</code>
     *     authorizing item storage
     * @throws NotAuthorizedException if the <i>permission</i> is not
     *     granted to the current user session
     */
    void authorize(String permission) throws NotAuthorizedException;

    /**
     * @return the authenticated user, or <code>null</code> if the user hasn't
     *     logged in
     */
    User getUser();
}
