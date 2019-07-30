package org.apromore.user_ui;

/*-
 * #%L
 * Apromore :: user-ui
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

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

/**
 * {@link UIPlugin} for the Account/Preferences command.
 */
@Component(service = {UIPlugin.class})
public final class PreferencesUIPlugin extends AbstractUIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(PreferencesUIPlugin.class);

    /** Sole constructor. */
    public PreferencesUIPlugin() {
        super("account.group",
              "preferences.label",
              "preferences.iconSclass");
    }

    /** {@inheritDoc}
     *
     * This implementation is enabled while the user session is authorized.
     */
    @Override
    public boolean isEnabled(final UIPluginContext context) {
        return Users.getUser() != null;
    }

    /** {@inheritDoc}
     *
     * This implementation presents the preferences pane.
     */
    @Override
    public void execute(final UIPluginContext context) {
        try {
            Reader reader = new InputStreamReader(
                PreferencesUIPlugin.class
                    .getClassLoader()
                    .getResourceAsStream("zul/preferences.zul"),
                "UTF-8"
            );
            Properties properties = new Properties();
            properties.setProperty("test", "Test value");
            Window window = (Window) Executions.createComponentsDirectly(reader,
                "zul", context.getParentComponent(), properties);
            window.setAttribute("UIPluginContext", context);

        } catch (IOException e) {
            throw new Error("ZUL resource preferences.zul could not be created "
                + "as a ZK component", e);
        }
    }
}
