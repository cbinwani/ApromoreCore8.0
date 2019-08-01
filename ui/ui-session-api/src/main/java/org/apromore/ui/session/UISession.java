package org.apromore.ui.session;

/*-
 * #%L
 * Apromore :: ui-session
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

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.zkoss.zk.ui.Sessions;

/**
 * Holds per-HTTP session attributes.
 */
public interface UISession extends Map<String, Object> {

   /**
    * @return the shared session
    * @throws RuntimeException if "uiSession" service isn't present
    */
   static UISession getCurrent() {
       HttpSession httpSession = (HttpSession)
            Sessions.getCurrent().getNativeSession();
        BundleContext bundleContext = (BundleContext)
            httpSession.getServletContext().getAttribute("osgi-bundlecontext");
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
            Object service = bundleContext.getService(ref);
            if (service instanceof BlueprintContainer) {
                return (UISession) ((BlueprintContainer) service)
                    .getComponentInstance("uiSession");
            }
        }

        throw new RuntimeException("Unable to find a BlueprintContainer");
   }
}
