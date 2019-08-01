package org.apromore.folder_ui;

/*-
 * #%L
 * Apromore :: folder :: folder-ui
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

import java.io.IOException;
//import java.util.Arrays;
//import java.util.ResourceBundle;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
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
        HTTP_WHITEBOARD_SERVLET_PATTERN + "=/folder/*",
        HTTP_WHITEBOARD_SERVLET_NAME + "=folder" })
public class FolderServlet extends HttpServlet {

    /**
     * This HTTP servlet request attribute tells the forwarded servlet what
     * the original {@link HttpServletRequest#getPathInfo} was.
     *
     * In this particular case, the path info identifies the folder to
     * display.
     */
    static final String PATH_ATTRIBUTE = "org.apromore.folder_ui.PathInfo";

    /** {@inheritDoc}
     *
     * This implementation forwards the request to <code>folder.zul</code>.
     */
    @Override
    public void doGet(final HttpServletRequest req,
                      final HttpServletResponse resp)
        throws ServletException, IOException {

        String path = req.getPathInfo();

        /*
        if (req.getCookies() != null) {
            String sessionId = Arrays.asList(req.getCookies())
                .stream()
                .filter(cookie -> "JSESSIONID".equals(cookie.getName()))
                .findAny()
                .get()
                .getValue();
            log("GET sessionId " + sessionId);
        }
        if (req.getSession() != null) {
            log("GET session.id " + req.getSession().getId());
        }
        */

        if (path == null) {
            resp.sendRedirect(req.getServletPath() + "/");

        } else {
            req.setAttribute(PATH_ATTRIBUTE, path);
            req.getServletContext()
               .getNamedDispatcher("zkLoader")
               .forward(new RequestWrapper(req, null, "/folder.zul"), resp);
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
