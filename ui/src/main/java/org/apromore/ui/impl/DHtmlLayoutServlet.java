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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.UiException;

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
}
