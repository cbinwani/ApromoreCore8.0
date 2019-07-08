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

import java.util.ResourceBundle;
import org.apromore.ui.spi.UIPluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Window;
import org.zkoss.zul.theme.Themes;

/**
 * Controller for <code>preferences.zul</code>.
 */
public class PreferencesController extends SelectorComposer<Component> {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(PreferencesController.class);

    /** @return localized text catalogue */
    public ResourceBundle getLabels() {
        return ResourceBundle.getBundle("Labels", Locales.getCurrent());
    }

    /** Window. */
    @SuppressWarnings("nullness")
    @Wire private Window win;

    /** The current ZK theme. */
    @Wire("#themeListbox")
    @SuppressWarnings("nullness")
    private Listbox themeListbox;

    /** {@inheritDoc} */
    @Override
    public void doFinally() {
        ListModelArray model = new ListModelArray(Themes.getThemes());
        model.addToSelection(Themes.getCurrentTheme());
        themeListbox.setModel(model);
    }

    /** @param event  button click */
    @Listen("onClick = #okButton")
    public void onClickOk(final Event event) {
        win.detach();
    }

    /** @param selectEvent  theme selected */
    @Listen("onSelect = #themeListbox")
    @SuppressWarnings("nullness")  // Themes.setTheme not annotated
    public void onSelectTheme(final SelectEvent<Listitem, String> selectEvent) {
        String newTheme = selectEvent.getReference().getValue();
        LOGGER.info(String.format("Changed ZK theme: %s", newTheme));
        UIPluginContext context =
            (UIPluginContext) win.getAttribute("UIPluginContext");
        Themes.setTheme(Executions.getCurrent(), newTheme);
        Executions.sendRedirect("");
    }
}
