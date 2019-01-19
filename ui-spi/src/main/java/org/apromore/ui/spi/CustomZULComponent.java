package org.apromore.ui.spi;

import java.io.IOException;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

/**
 * @see <a href="https://www.zkoss.org/wiki/ZK%20Component%20Development%20Essentials/Creating%20a%20simple%20ZK%20Component/Implementing%20the%20Component">Creating a simple ZK Component</a>
 */
public class CustomZULComponent extends HtmlBasedComponent {

    private String value = "component uninitialized";

    public String getValue() {
        return value;
    }

    public void setValue(final String newValue) {
        if (!value.equals(newValue)) {
            value = newValue;
            smartUpdate("value", value);
        }
    }

    protected void renderProperties(final ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "value", value);
    }
}
