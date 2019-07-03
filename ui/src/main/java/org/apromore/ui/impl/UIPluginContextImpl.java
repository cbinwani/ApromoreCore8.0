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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import org.apromore.ui.spi.UIPluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

/** {@inheritDoc UIPluginContext}. */
class UIPluginContextImpl implements UIPluginContext {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(MenubarController.class);

    /** A canvas just about anyone is allowed to scribble upon. */
    private Component parent;

    /** @param newParent  parent for plugins to add content to */
    UIPluginContextImpl(final Component newParent) {
        this.parent = newParent;
    }

    @Override
    public Component createComponent(final ClassLoader classLoader,
                                     final String      uri,
                                     final Map<?, ?>   arguments) {
        try {
            InputStream in = classLoader.getResourceAsStream(uri);
            if (in == null) {
                throw new IOException("No resource " + uri
                    + " found in bundle classpath");
            }

            parent.getChildren().clear();

            return Executions.createComponentsDirectly(
                new InputStreamReader(in, "UTF-8"),
                "zul",
                parent,
                arguments);

        } catch (IOException e) {
            throw new Error("ZUL resource " + uri
                + " could not be created as as ZK component", e);
        }
    }

    @Override
    public Component getParentComponent() {
        return parent;
    }

    @Override
    public void setComponent(final Component component) {
        parent.getChildren().clear();
        parent.getChildren().add(component);
    }

    @Override
    public Object getSessionAttribute(final String attribute) {

        return Sessions.getCurrent().getAttribute(attribute);
    }

    @Override
    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    public void putSessionAttribute(final String attribute,
                                    final Object newValue) {

        Sessions.getCurrent().setAttribute(attribute, newValue);
    }
}
