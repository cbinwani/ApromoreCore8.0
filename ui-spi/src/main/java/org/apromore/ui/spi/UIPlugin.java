package org.apromore.ui.spi;

/*-
 * #%L
 * Apromore :: ui-spi
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

import java.awt.image.RenderedImage;

/**
 * Service provider interface for adding commands to the user interface.
 *
 * The expected implementation is to add menu items to invoke the
 * commands.
 */
public interface UIPlugin {

    /** @return the menu in which this plugin's menuitem will appear */
    String getGroupLabel();

    /**
     * Specify the icon as a CSS style class.
     *
     * A typical value to return would be <code>z-icon-bell</code>,
     * where "bell" is the name of a glyph from the Font Awesome
     * catalogue.
     *
     * @return the icon's CSS style class
     * @see <a href="https://www.zkoss.org/wiki/ZK_Component_Reference/Base_Components/LabelImageElement#IconSclass">ZK Component Reference: IconSclass</a>
     * @see <a href="https://fontawesome.com/icons">Font Awesome</a>
     */
    String getIconSclass();

    /**
     * Specify the icon as an explicit image.
     *
     * @return the icon appearing on the plugin's menuitem
     */
    RenderedImage getIcon();

    /** @return the text appearing on the plugin's menuitem */
    String getLabel();

    /**
     * @param context  provided by UI
     * @return whether the plugin is applicable to the given selection
     */
    boolean isEnabled(UIPluginContext context);

    /**
     * Invoked when the menu item is selected.
     *
     * @param context  provided by UI
     */
    void execute(UIPluginContext context);
}
