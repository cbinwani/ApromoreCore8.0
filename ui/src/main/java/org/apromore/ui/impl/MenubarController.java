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

import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.apromore.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
//import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

/**
 * Controller for the main {@link Menubar}.
 */
public final class MenubarController extends SelectorComposer<Menubar>
    implements EventListener<Event> {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(MenubarController.class);

    /** The canvas. */
    @Wire("#parent")
    @SuppressWarnings("nullness")
    private Component parent;

    /** Login support. */
    @SuppressWarnings("nullness")
    private UserService userService;

    @Override
    public void doAfterCompose(final Menubar menubar) throws Exception {
        super.doAfterCompose(menubar);

        userService = (UserService)
            Util.blueprintContainer(menubar)
                .getComponentInstance("userService");

        // Listen for changes to the UIPlugin list
        UIPluginListener uiPluginListener = (UIPluginListener)
            Util.blueprintContainer(menubar)
                .getComponentInstance("uiPluginListener");
        uiPluginListener.addListener(menubar);
        EventQueues.lookup("q", menubar.getDesktop().getSession(), true)
                   .subscribe(this);

        // Replace the menubar in the ZUL with a dynamically-generated one based
        // on the UIPlugin list
        generateMenubar(menubar, getUIPluginContext());
    }

    /** {@inheritDoc}
     *
     * Updates the user interface in response to changes in the
     * {@link UIPlugin}s or user session.
     *
     * The following events are recognized:
     * <dl>
     * <dt>onBind</dt>   <dd>new {@link UIPlugin} appeared</dd>
     * <dt>onLogin</dt>  <dd>user authentication of this session changed</dd>
     * <dt>onSelect</dt> <dd>items were selected/deselected in this session</dd>
     * <dt>onUnbind</dt> <dd>existing {@link UIPlugin} went away</dd>
     * </dl>
     *
     * @param event  sent by the ZK queue
     */
    @Override
    public void onEvent(final Event event) {
        switch (event.getName()) {
        case "onBind":    // new UIPlugin appeared
        case "onLogin":   // User property changed
        case "onSelect":  // Selection property changed
        case "onUnbind":  // existing UIPlugin went away
            generateMenubar(getSelf(), getUIPluginContext());
            break;
        default:
            LOGGER.warn("Main menubar ignoring unrecognized event: "
                + event.getName());
        }
    }

    /** @return a freshly created plugin context */
    private UIPluginContext getUIPluginContext() {
        return new UIPluginContextImpl(parent, userService);
    }

    /**
     * Regenerates the main window's menubar.
     *
     * @param newMenubar  the menubar to update
     * @param uiPluginContext  used to determine menu item enablement
     */
    @SuppressWarnings("nullness")  // Menupopup.insertBefore isn't annotated
    private void generateMenubar(final Menubar newMenubar,
                                 final UIPluginContext uiPluginContext) {

        // If present, this comparator expresses the preferred ordering for
        // menus along the the menu bar
        Comparator<String> ordering = new ExplicitComparator(
            (String) Util.blueprintContainer(getSelf())
                         .getComponentInstance("uiMenuOrder")
        );

        List<UIPlugin> uiPlugins = (List<UIPlugin>)
            Util.blueprintContainer(getSelf())
                .getComponentInstance("uiPlugins");
        LOGGER.info("generateMenubar: locale = " + Locales.getCurrent()
            + ", uiPlugins = " + uiPlugins);

        SortedMap<String, Menu> menuMap = new TreeMap<>(ordering);

        for (final UIPlugin plugin: uiPlugins) {
            String menuName = "Uninitialized";
            try {
                menuName = plugin.getGroupLabel();
                if (menuName == null) {
                    menuName = "Default";
                }
            } catch (Throwable e) {
                menuName = e.toString();
            }

            // Create a new menu if this is the first menu item within it
            if (!menuMap.containsKey(menuName)) {
                Menu menu = new Menu(menuName);
                menu.appendChild(new Menupopup());
                menuMap.put(menuName, menu);
            }
            assert menuMap.containsKey(menuName);

            // Create the menu item
            try {
                Menu menu = menuMap.get(menuName);
                Menuitem menuitem = new Menuitem();
                menuitem.setIconSclass(plugin.getIconSclass());
                //menuitem.setImageContent(plugin.getIcon());
                menuitem.setLabel(plugin.getLabel());
                menuitem.setDisabled(!plugin.isEnabled(uiPluginContext));

                // Insert the menu item alphabetically into the menu
                Menuitem precedingMenuitem = null;
                List<Menuitem> existingMenuitems =
                    menu.getMenupopup().getChildren();
                for (Menuitem existingMenuitem: existingMenuitems) {
                    if (menuitem.getLabel()
                                .compareTo(existingMenuitem.getLabel()) <= 0) {
                        precedingMenuitem = existingMenuitem;
                        break;
                    }
                }
                menu.getMenupopup().insertBefore(menuitem, precedingMenuitem);

                menuitem.addEventListener("onClick",
                    new EventListener<Event>() {

                    @Override
                    public void onEvent(final Event event) throws Exception {
                        plugin.execute(uiPluginContext);
                    }
                });

            } catch (Throwable e) {
                LOGGER.error("Unable to create menu item in " + menuName, e);
            }
        }

        // Replace the existing menubar contents
        newMenubar.getChildren().clear();
        newMenubar.getChildren().addAll(menuMap.values());
    }
}
