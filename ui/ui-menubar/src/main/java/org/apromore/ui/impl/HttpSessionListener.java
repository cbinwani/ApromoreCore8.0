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

import javax.servlet.ServletContextEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.http.WebManager;

/**
 * Debugging aid.
 */
public class HttpSessionListener
    extends org.zkoss.zk.ui.http.HttpSessionListener {

    /** Logger.  Named after this class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(HttpSessionListener.class);

    /** {@inheritDoc}
     *
     * This implementation adds logging.
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {

        LOGGER.info("Servlet context initialized: "
            + event.getServletContext());

        super.contextInitialized(event);

        LOGGER.info("ZK web manager "
            + WebManager.getWebManagerIfAny(event.getServletContext()));
    }
}
