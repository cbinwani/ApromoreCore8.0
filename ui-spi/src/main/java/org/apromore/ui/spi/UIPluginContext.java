package org.apromore.ui.spi;

/*-
 * #%L
 * Apromore :: ui-spi
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

import java.util.Map;
import org.apromore.user.User;
import org.zkoss.zk.ui.Component;

/**
 * Passed to {@link UIPlugin#execute} and {@link UIPlugin#isEnabled}.
 *
 * This provides information such as the selection and view controller hosting.
 */
public interface UIPluginContext {

    /**
     * @param bundleClassLoader  the classloader for the bundle containing the
     *     ZUL markup
     * @param uri  location within the <i>bundleClassLoader</i> of ZUL markup
     *     to create this component
     * @param arguments  see {@link org.zkoss.zk.ui.Component}'s
     *     <code>createComponent</code> methods
     * @return a ZK component corresponding to the ZUL markup
     * @throws Error  if the ZUL markup isn't available; this is considered a
     *     programming error (rather than an I/O exception) since ZUL access is
     *     a linking issue
     */
    Component createComponent(ClassLoader bundleClassLoader,
                              String      uri,
                              Map<?, ?>   arguments);

    /**
     * @return the ZK component representing the canvas area of the main
     *     window
     */
    Component getParentComponent();

    /**
     * @param component  the ZK component representing the canvas area of the
     *     main window
     */
    void setComponent(Component component);

    /**
     * @return the currently authenticated user, or <code>null</code> for an
     *     anonymous user session
     */
    User getUser();

    /**
     * @param newUser  the new authenticated user, or <code>null</code> to
     *     de-authenticate the current user session (i.e. log out).
     */
    void setUser(User newUser);

    /**
     * @param attribute  a session attribute name
     * @return the value of the session attribute, possibly <code>null</code>
     */
    Object getSessionAttribute(String attribute);

    /**
     * @param attribute  a session attribute name
     * @param newValue  the desired new value, possible <code>null</code>
     */
    void putSessionAttribute(String attribute, Object newValue);
}
