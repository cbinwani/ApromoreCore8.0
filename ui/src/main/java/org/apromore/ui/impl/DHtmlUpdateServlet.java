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

import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;

/**
 * Kludge to work around a race condition with ZK's ZUL layout servlet.
 */
public class DHtmlUpdateServlet
    extends org.zkoss.zk.au.http.DHtmlUpdateServlet {

    /** Logger.  Named after this class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(DHtmlUpdateServlet.class);

    /** Milliseconds delay before retrying {@link init}. */
    private static final int DELAY = 100;

    /**
     * This implementation does nothing but defer to its superclass.
     *
     * The intention of this method was original to trap the ZK
     * UiException which the superclass throws if the ZUL layout
     * servlet isn't ready.  However it appears that the very fact
     * of subclassing is enough to tip the race condition.
     */
    @Override
    public void init() throws ServletException {

        // Delay so DHtmlLayoutServlet can start
        try {
            Thread.sleep(DELAY);

        } catch (InterruptedException e2) {
            LOGGER.warn("Pause interrupted", e2);
        }

        try {
            super.init();

        } catch (UiException e) {
            LOGGER.warn("Race condition detected with "
                + DHtmlLayoutServlet.class.getName());
            throw e;
        }
    }
}
