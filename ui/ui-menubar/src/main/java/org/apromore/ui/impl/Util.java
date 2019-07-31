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

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

/**
 * Servlet context lookup utility.
 */
public abstract class Util {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(Util.class);

    /**
     * @param component  a ZK component
     * @return the OSGi Blueprint container from the servlet context
     * @throws Error  if there is no Blueprint context
     */
    public static BlueprintContainer blueprintContainer(
        final Component component) {

        BundleContext bundleContext = (BundleContext) component
            .getDesktop()
            .getWebApp()
            .getServletContext()
            .getAttribute("osgi-bundlecontext");
        for (ServiceReference serviceReference
            : bundleContext.getBundle().getRegisteredServices()) {

            Object service = bundleContext.getService(serviceReference);
            if (service instanceof BlueprintContainer) {
                return (BlueprintContainer) service;
            }
        }

        throw new Error("No blueprint context");
    }
}
