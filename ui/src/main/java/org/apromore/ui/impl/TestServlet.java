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
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;
//import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
    .HTTP_WHITEBOARD_CONTEXT_NAME;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
    .HTTP_WHITEBOARD_CONTEXT_SELECT;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
    .HTTP_WHITEBOARD_SERVLET_NAME;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants
    .HTTP_WHITEBOARD_SERVLET_PATTERN;
*/

/**
 * Folder paths have URLs, and this servlet presents them.
 */
//@Component(
//    service = Servlet.class,
//    scope = ServiceScope.PROTOTYPE,
//    property = {
//        HTTP_WHITEBOARD_CONTEXT_SELECT
//        + "=(" + HTTP_WHITEBOARD_CONTEXT_NAME + "=org.apromore.ui)",
//        HTTP_WHITEBOARD_SERVLET_PATTERN + "=/folder/*",
//        HTTP_WHITEBOARD_SERVLET_NAME + "=Folder" })
public class TestServlet extends HttpServlet {

    /** {@inheritDoc}
     *
     * This implementation produces a plain text constant.
     */
    @Override
    public void doGet(final HttpServletRequest req,
                      final HttpServletResponse resp)
        throws ServletException, IOException {

        log("GET");

        String path = req.getPathInfo();

        // Redirect empty path to the root path "/"
        if (path == null) {
            Locale locale = req.getLocale();
            ResourceBundle bundle =
                ResourceBundle.getBundle("Labels", locale);
            String newURL = resp.encodeRedirectURL(req.getServletPath() + "/");
            resp.setHeader("Location", newURL);
            resp.sendError(HttpServletResponse.SC_MOVED_PERMANENTLY,
                String.format(locale,
                              bundle.getString("http.root_folder_at"),
                              newURL));
        }

        // Present the path
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        try (PrintWriter writer = resp.getWriter()) {
            writer.print("Representation of the item at the path ");
            writer.println(path);
        }
    }
}
