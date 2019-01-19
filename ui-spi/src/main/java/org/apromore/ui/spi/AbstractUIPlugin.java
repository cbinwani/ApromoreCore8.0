package org.apromore.ui.spi;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base implementation of {@link UIPlugin}.
 */
public abstract class AbstractUIPlugin implements UIPlugin {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(AbstractUIPlugin.class);

    protected String groupLabel = "Default";
    protected String label      = "Default";

    public String getGroupLabel() {
        return groupLabel;
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
            return null;
        }
    }

    public String getLabel() {
        return label;
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
}
