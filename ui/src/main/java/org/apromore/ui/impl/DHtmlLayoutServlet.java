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
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.lang.Exceptions;
import org.zkoss.lang.Expectable;
import org.zkoss.mesg.Messages;
import org.zkoss.web.Attributes;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.web.servlet.http.Https;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.OperationException;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.http.I18Ns;
//import org.zkoss.zk.ui.http.Utils;
import org.zkoss.zk.ui.http.WebManager;
import org.zkoss.zk.ui.sys.SessionsCtrl;

/**
 * Debugging aid.
 */
public class DHtmlLayoutServlet
    extends org.zkoss.zk.ui.http.DHtmlLayoutServlet {

    /** Logger.  Named after this class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(DHtmlLayoutServlet.class);

    /**
     * This implementation adds logging.
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {

        LOGGER.info("Layout " + config.getServletName()
            + " initing with context" + config.getServletContext());

        try {
            super.init(config);

        } catch (UiException e) {
            LOGGER.warn("Layout exception", e);
            throw e;
        }
    }

    /**
     * This implementation adds logging.
     */
    @Override
    public void service(final ServletRequest request,
                        final ServletResponse response)
        throws IOException, ServletException {

        LOGGER.info("Layout serving request " + request);

        try {
            super.service(request, response);

        } catch (IOException | ServletException e) {
            LOGGER.warn("Layout exception", e);
            throw e;
        }
    }

    /**
     * This implementation adds logging.
     */
    @Override
    @SuppressWarnings({"checkstyle:FinalParameters", "checkstyle:LineLength", "checkstyle:NeedBraces", "nullness"})
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
        LOGGER.info("GETing \"" + request.getAttribute(Attributes.INCLUDE_SERVLET_PATH) + "\"");
                String path = Https.getThisPathInfo(request);
        LOGGER.info("GETing path \"" + path + "\"");
                final boolean bRichlet = path != null && path.length() > 0;
                if (!bRichlet)
                        path = Https.getThisServletPath(request);
                //              if (log.finerable()) log.finer("Creates from "+path);
        LOGGER.info("GETing path2 \"" + path + "\"");

                final Session sess = WebManager.getSession(getServletContext(), request);
                if (!SessionsCtrl.requestEnter(sess)) {
                        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, Messages.get(MZk.TOO_MANY_REQUESTS));
                        return;
                }
                try {
                        final Object old = I18Ns.setup(sess, request, response,
                                        sess.getWebApp().getConfiguration().getResponseCharset());
                        try {
        LOGGER.info("GETing process");
                                if (!process(sess, request, response, path, bRichlet))
                                        handleError(sess, request, response, path, null);
        LOGGER.info("GETing process2");
                        } catch (Throwable ex) {
                                handleError(sess, request, response, path, ex);
                        } finally {
                                I18Ns.cleanup(request, old);
                        }
                } finally {
                        SessionsCtrl.requestExit(sess);
                }
        LOGGER.info("GETed");
        }

        /** Handles exception being thrown when rendering a page.
         * @param err the exception being throw. If null, it means the page
         * is not found.
         */
        @SuppressWarnings({"checkstyle:FinalParameters", "checkstyle:InnerAssignment", "checkstyle:JavadocMethod", "checkstyle:LineLength", "checkstyle:MagicNumber", "checkstyle:NeedBraces"})
        private void handleError(Session sess, HttpServletRequest request, HttpServletResponse response, String path,
                        Throwable err) throws ServletException, IOException {
                //Utils.resetOwner();

                // ZK-3679
                Throwable cause;
                if (err instanceof OperationException && (cause = err.getCause()) instanceof Expectable)
                        err = cause;

                //Note: if not included, it is handled by Web container
                if (err != null && Servlets.isIncluded(request)) {
                        //Bug 1714094: we have to handle err, because Web container
                        //didn't allow developer to intercept errors caused by inclusion
                        final String errpg = sess.getWebApp().getConfiguration().getErrorPage(sess.getDeviceType(), err);
                        if (errpg != null) {
                                try {
                                        request.setAttribute("javax.servlet.error.message", Exceptions.getMessage(err));
                                        request.setAttribute("javax.servlet.error.exception", err);
                                        request.setAttribute("javax.servlet.error.exception_type", err.getClass());
                                        request.setAttribute("javax.servlet.error.status_code", new Integer(500));
                                        if (process(sess, request, response, errpg, false))
                                                return; //done

                                        LOGGER.warn("The error page not found: " + errpg);
                                } catch (IOException ex) { //eat it (connection off)
                                } catch (Throwable ex) {
                                        LOGGER.warn("Failed to load the error page: " + errpg, ex);
                                }
                        }
                }

                //Utils.handleError(getServletContext(), request, response, path, err);
        }
}
