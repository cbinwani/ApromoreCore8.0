
package org.apromore.user.impl;

/*-
 * #%L
 * Apromore
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

import org.junit.Test;
import org.apromore.user.UserService;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * Test suite for {@link UserServiceImpl}.
 */
public class UserServiceImplTest {

    /**
     * Test case for {@Link UserServiceImpl#getAuthorization} for a null
     * (unauthenticated) user.
     */
    @Test
    @SuppressWarnings("nullness")
    public void testGetAuthorizationForNullUser() {
        UserAdmin userAdmin = new UserServiceImpl();
        Authorization authorization = userAdmin.getAuthorization(null);
        assert authorization.getRoles().length == 0;
        assert !authorization.hasRole("dummy");
        assert authorization.hasRole(Role.USER_ANYONE);
    }
}
