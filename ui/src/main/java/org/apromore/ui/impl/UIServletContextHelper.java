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

//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.context.ServletContextHelper;
//import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
//    .HTTP_WHITEBOARD_CONTEXT_NAME;
//import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
//    .HTTP_WHITEBOARD_CONTEXT_PATH;

/**
 * Used to place all UI plugins into the same servlet context.
 *
 * @see https://blog.osoco.de/2016/10/http-whiteboard-simply-simple-part-iii/
 */
//@Component(
//    service  = ServletContextHelper.class,
//    scope    = ServiceScope.BUNDLE,
//    property = {
//        HTTP_WHITEBOARD_CONTEXT_NAME + "=" + UIServletContextHelper.NAME,
//        HTTP_WHITEBOARD_CONTEXT_PATH + "=/" })
public class UIServletContextHelper extends ServletContextHelper {

    /**
     * This value is passed to the
     * {@link org.osgi.service.http.whiteboard.HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT}
     * property of whiteboard servlet components to add them to this context.
     */
    public static final String NAME = "uiServletContext";
}
