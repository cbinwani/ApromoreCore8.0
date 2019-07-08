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

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.InputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;

/**
 * A base implementation of {@link UIPlugin}.
 */
public abstract class AbstractUIPlugin implements UIPlugin {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(AbstractUIPlugin.class);

    /**
     * Key into the "Labels" {@link ResourceBundle} within the subclass bundle.
     */
    private String groupLabelKey;

    /**
     * Key into the "Labels" {@link ResourceBundle} within the subclass bundle.
     */
    private String labelKey;

    /**
     * Key into rhe "Labels" {@link ResourceBundle} within the subclass bundle.
     */
    private String iconSclassKey;

    /**
     * @param newGroupLabelKey  localization key for the menu
     * @param newLabelKey  localization key for the menu item
     */
/*
    protected AbstractUIPlugin(final String newGroupLabelKey,
                               final String newLabelKey) {

        this.groupLabelKey = newGroupLabelKey;
        this.labelKey      = newLabelKey;
        this.iconSclassKey = null;
    }
*/

    /**
     * @param newGroupLabelKey  localization key for the menu
     * @param newLabelKey  localization key for the menu item
     * @param newIconSclassKey  location key for the menu icon CSS style class
     */
    protected AbstractUIPlugin(final String newGroupLabelKey,
                               final String newLabelKey,
                               final String newIconSclassKey) {

        this.groupLabelKey = newGroupLabelKey;
        this.labelKey      = newLabelKey;
        this.iconSclassKey = newIconSclassKey;
    }

    @Override
    public String getGroupLabel() {
        return getLocalizedString(groupLabelKey);
    }

    /** {@inheritDoc}
     *
     * Default implementation is to return
     * <code>"z-icon-puzzle-piece z-icon-lg"</code>.
     */
    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    public String getIconSclass() {
        return iconSclassKey == null
            ? "z-icon-puzzle-piece z-icon-lg"
            : getLocalizedString(iconSclassKey);
    }

    /** {@inheritDoc}
     *
     * Default implementation is to return the resource <code>/icon.png</code>
     * from the classpath.
     */
    public RenderedImage getIcon() {
        try {
            InputStream in = getClass().getClassLoader()
                                       .getResourceAsStream("/icon.png");
            if (in == null) {
                // Fall back to a default icon
                in = AbstractUIPlugin.class.getClassLoader()
                                           .getResourceAsStream("/icon.png");
            }
            BufferedImage icon = ImageIO.read(in);
            in.close();
            return icon;

        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to read icon.png from classpath");
        }
    }

    @Override
    public String getLabel() {
        return getLocalizedString(labelKey);
    }

    /** {@inheritDoc}
     *
     * If not overidden by concrete implementations, defaults to always enabled.
     */
    public boolean isEnabled(final UIPluginContext context) {
        return true;
    }

    /** {@inheritDoc}
     *
     * If not overidden by concrete implementations, defaults to doing nothing
     * and logging a warning message.
     */
    public void execute(final UIPluginContext context) {
        LOGGER.warn("Executed UI plugin with missing implementation: "
            + getClass());
    }

    /**
     * Localization convenience.
     *
     * Place localized strings into property files within the subclass's bundle,
     * naming them <code>/Labels.properties</code>,
     * <code>/Labels_en_US.properties</code>, etc.
     * This method uses the locale of the user's web browser, which is attached
     * to the calling thread by ZK.
     *
     * @param key  a {@link ResourceBundle} key
     * @return the value corresponding to <i>key</i> from the
     *     {@link ResourceBundle} with prefix "Labels" in the receiver's
     *     classloader, for the ZK locale of the current thread.
     */
    protected String getLocalizedString(final String key) {
        ResourceBundle bundle = ResourceBundle.getBundle(
            "Labels",
            Locales.getCurrent(),
            getClass().getClassLoader()
        );
        return bundle.getString(key);
    }
}
