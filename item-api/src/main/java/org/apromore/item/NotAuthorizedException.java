package org.apromore.item;

/*-
 * #%L
 * Apromore :: item-api
 * %%
 * Copyright (C) 2018 - 2019 The Apromore Initiative
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
 * The caller lacks authorization to perform the throwing function.
 *
 * In the case where the caller is unauthenticated, this exception
 * should prompt the user to authenticate and reattempt the function.
 */
public class NotAuthorizedException extends Exception {

    /**
     * @param newPermission  the permission sought and denied
     */
    public NotAuthorizedException(final String newPermission) {
        super("Missing permission: " + newPermission);
    }
}
