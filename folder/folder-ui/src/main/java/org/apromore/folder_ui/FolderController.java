package org.apromore.folder_ui;

/*-
 * #%L
 * Apromore :: folder-ui
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

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * Controller for <code>folder.zul</code>.
 *
 * The incoming request must have the {@link #PATH_ATTRIBUTE} attribute set.
 */
public class FolderController extends SelectorComposer<Window> {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(FolderController.class);

    /** Name of the servlet attribute which holds the folder path to display. */
    static final String PATH_ATTRIBUTE = "org.apromore.folder_ui.PathInfo";

    /** Window. */
    @Wire
    @SuppressWarnings("nullness")
    private Window win;

    /** @param mouseEvent  clicked home */
    @Listen("onClick = #testButton")
    @SuppressWarnings("nullness")
    public void onClickTestButton(final MouseEvent mouseEvent) {
        LOGGER.info("Test context path [{}]",
            Executions.getCurrent().getContextPath());
        LOGGER.info("Test servlet request [{}]",
            Executions.getCurrent().getNativeRequest());
    }

    /** Current folder label. */
    @Wire("#pathLabel")
    @SuppressWarnings("nullness")
    private Label pathLabel;

    /** {@inheritDoc} */
    @Override
    public void doFinally() {
        LOGGER.info("pathLabel {}", pathLabel);
        if (pathLabel != null) {
            pathLabel.setValue("Initialized");
        }
        LOGGER.info("Context path [{}]",
            Executions.getCurrent().getContextPath());
        LOGGER.info("Servlet request [{}]",
            Executions.getCurrent().getNativeRequest());
        HttpServletRequest request = (HttpServletRequest)
            Executions.getCurrent().getNativeRequest();
        LOGGER.info("Servlet path [{}]", request.getServletPath());
        LOGGER.info("Path info [{}]", request.getPathInfo());
        LOGGER.info("Attribute [{}]",
            request.getAttribute("org.apromore.folder_ui.PathInfo"));
    }
}
