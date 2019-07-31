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

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
//import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
//    .HTTP_WHITEBOARD_CONTEXT_NAME;
//import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
//    .HTTP_WHITEBOARD_CONTEXT_SELECT;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
    .HTTP_WHITEBOARD_SERVLET_NAME;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
    .HTTP_WHITEBOARD_SERVLET_PATTERN;

/**
 * This servlet interprets all Apromore URLs and delegates their presentation
 * to the appropriate ZUML pages.
 */
@Component(
    service = Servlet.class,
    scope = ServiceScope.PROTOTYPE,
    property = {
//        HTTP_WHITEBOARD_CONTEXT_SELECT
//        + "=(" + HTTP_WHITEBOARD_CONTEXT_NAME + "=uiServletContext)",
        HTTP_WHITEBOARD_SERVLET_PATTERN + "=/html/*",
        HTTP_WHITEBOARD_SERVLET_NAME + "=ui" })
public class UIServlet extends HttpServlet {

    /** {@inheritDoc}
     *
     * This implementation produces a plain text constant.
     */
    @Override
    public void doGet(final HttpServletRequest req,
                      final HttpServletResponse resp)
        throws ServletException, IOException {

        String path = req.getPathInfo();

        log("GET path=\"" + path
            + "\", servlet-path=\"" + req.getServletPath() + "\"");

        if (path == null) {
            resp.sendRedirect(req.getServletPath() + "/");

        } else if ("/".equals(path)) {
            req.getServletContext()
               .getNamedDispatcher("zkLoader")
               .forward(new RequestWrapper(req, null, "/index.zul"), resp);

        } else {
            ResourceBundle bundle = ResourceBundle.getBundle("Labels");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND,
                           String.format(req.getLocale(),
                                         bundle.getString("http.not_found"),
                                         path));
        }
    }

    /**
     * Redirects a URL route to a particular ZUML page.
     */
    private static class RequestWrapper extends HttpServletRequestWrapper {

        /** Overrides the wrapped request's path info. */
        private final @Nullable String pathInfo;

        /** Overrides the wrapped request's servlet Path. */
        private final String servletPath;

        /**
         * @param request  wrapped request
         * @param newPathInfo  pass <code>null</code>
         * @param newServletPath  the ZUL page, e.g. <code>/index.zul</code>
         */
        RequestWrapper(final HttpServletRequest request,
                       final @Nullable String newPathInfo,
                       final String newServletPath) {

            super(request);

            this.pathInfo = newPathInfo;
            this.servletPath = newServletPath;
        }

        /** @return <code>null</code> */
        @Override
        @SuppressWarnings("nullness")  // Java EE not annotated
        public String getPathInfo() {
            return pathInfo;
        }

        /** @return <code>/index.zul</code> */
        @Override
        public String getServletPath() {
            return servletPath;
        }
    }
}
