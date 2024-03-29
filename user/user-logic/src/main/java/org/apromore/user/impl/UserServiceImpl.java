package org.apromore.user.impl;

/*-
 * #%L
 * Apromore :: user-logic
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

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;
import java.security.Principal;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.apromore.user.UserService;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
//import org.osgi.service.component.annotations.Component;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.osgi.service.useradmin.UserAdminEvent;
import org.osgi.service.useradmin.UserAdminListener;
import org.osgi.service.useradmin.UserAdminPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User account management service.
 */
//@Component(service = { UserAdmin.class, UserService,class })
public final class UserServiceImpl implements UserAdmin, UserService {

    /** Configuration. */
    public @interface Config {
        /**
         * @return which login configuration to use
         * @see {@link javax.security.auth.login.Configuration}
         */
        String jaas_loginConfigurationName() default "karaf";

        /** @return which {@link Principal} class identifies a group? */
        String jaas_groupPrincipalClass()
            default "org.apache.karaf.jaas.boot.principal.GroupPrincipal";

        /** @return which {@link Principal} class identifies a role? */
        String jaas_rolePrincipalClass()
            default "org.apache.karaf.jaas.boot.principal.RolePrincipal";

        /**
         * Which {@link Principal} class identifies a user?
         *
         * These classes are typically proprietary to the login module.
         * There may be more than one reasonable choice, e.g.
         * <code>com.sun.security.auth.UserPrincipal</code> with name
         * <code>jsmith</code> versus
         * <code>com.sun.security.auth.LdapPrincipal</code> with name
         * <code>uid=jsmith,ou=staff,o=acme</code>.
         *
         * @return which {@link Principal} class identifies a user?
         */
        String jaas_userPrincipalClass()
            default "org.apache.karaf.jaas.boot.principal.UserPrincipal";
    }

    /** @param config  automatically supplied by OSGi runtime */
    @Activate
    protected void activate(final Config config) {
        LOGGER.info("Activate with config " + config);
        setLoginConfigurationName(config.jaas_loginConfigurationName());
        //setGroupPrincipalClass(config.jaas_groupPrincipalClass
    }

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(UserServiceImpl.class);

    /** Registered listeners for {@link Role} changes. */
    private Set<UserAdminListener> listeners = java.util.Collections.emptySet();

    /** @see {@link javax.security.auth.login.Configuration} */
    private String loginConfigurationName = "Uninitialized";

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

    /** Which {@link Principal} class identifies a role? */
    private Class rolePrincipalClass = Object.class;


    // Property accessors

    /** @param newLoginConfigurationName  JAAS login configuration name */
    public void setLoginConfigurationName(
        final String newLoginConfigurationName) {
        this.loginConfigurationName = newLoginConfigurationName;
    }

    /**
     * @param newUserPrincipalClass  of the various principals associated
     *     with a subject, which one is used as the user id?
     */
    public void setUserPrincipalClass(final Class newUserPrincipalClass) {
        this.userPrincipalClass = newUserPrincipalClass;
    }

    /**
     * @param newRolePrincipalClass  of the various principals associated
     *     with a subject, which ones are used for roles?
     */
    public void setRolePrincipalClass(final Class newRolePrincipalClass) {
        this.rolePrincipalClass = newRolePrincipalClass;
    }


    // Implementation of UserAdmin

    @Override
    @SuppressWarnings("nullness")  // UserAdminPermission not annotated
    public Role createRole(final String name, final int type) {

        // Check for UserAdminPermission "admin"
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(
                new UserAdminPermission(UserAdminPermission.ADMIN, null)
            );
        }

        Role role;

        // Validate type
        switch (type) {
        case -1:  // a kludge to shush the compiler
            role = null;
            break;

        case Role.GROUP:
            throw new UnsupportedOperationException();
            //break;

        case Role.USER:
            throw new UnsupportedOperationException();
            //break;

        default:
            throw new IllegalArgumentException(type
                + " is not a valid Role type");
        }

        ServiceReference service = null;
        UserAdminEvent event = new UserAdminEvent(service, type, role);
        for (UserAdminListener listener: listeners) {
            listener.roleChanged(event);
        }

        return role;
    }

    @Override
    public boolean removeRole(final String name) {
        return false;
    }

    @Override
    public Role getRole(final String name) {
        throw new Error("Not implemented");
    }

    @Override
    public Role[] getRoles(final String filter) throws InvalidSyntaxException {
        throw new Error("Not implemented");
    }

    @Override
    public User getUser(final String key, final String value) {
        throw new Error("Not implemented");
    }

    @Override
    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    public Authorization getAuthorization(final User user) {
        return new Authorization() {

            private final String[] emptyRoles = {};

            /** Role names. */
            private String[] roles =
                user == null ? emptyRoles
                             : (String[]) user.getProperties().get("roles");

            @Override
            public String getName() {
                return user.getName();
            }

            @Override
            public boolean hasRole(final String name) {
                return Role.USER_ANYONE.equals(name)
                    || Arrays.asList(roles).contains(name);
            }

            @Override
            public String[] getRoles() {
                return roles;
            }
        };
    }


    // JAAS logic

    @Override
    public User authenticate(final String username, final String password)
        throws LoginException {

        LOGGER.debug("Authenticating user " + username);

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
                LOGGER.info("Principal: " + principal
                    + "  name: " + principal.getName()
                    + "  class: " + principal.getClass());
            }

            final String user = loginContext
                .getSubject()
                .getPrincipals()
                .stream()
                .filter(principal -> userPrincipalClass
                    .isAssignableFrom(principal.getClass()))
                .findAny()  // TO DO: validate a unique result
                .get()
                .getName();

            final String[] roles = loginContext
                .getSubject()
                .getPrincipals()
                .stream()
                .filter(principal -> rolePrincipalClass
                    .isAssignableFrom(principal.getClass()))
                .map(principal -> principal.getName())
                .toArray(String[]::new);

            return new User() {

                // Role

                @Override
                public String getName() {
                    return user;
                }

                @Override
                public int getType() {
                    return Role.USER;
                }

                @Override
                public Dictionary getProperties() {
                    Hashtable properties = new Hashtable();
                    properties.put("roles", roles);
                    return properties;
                }

                // User

                @Override
                public Dictionary getCredentials() {
                    return new Hashtable();
                }

                @Override
                public boolean hasCredential(final String key,
                                             final Object value) {
                    return false;
                }
            };

            // TO DO: failure isn't invoked in the case of a
            // LoginException

        } finally {
            loginContext.logout();
        }
    }
}
