package org.apromore.ui.spi;

import java.io.IOException;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

/**
 * @see <a href="https://www.zkoss.org/wiki/ZK%20Component%20Development%20Essentials/Creating%20a%20simple%20ZK%20Component/Implementing%20the%20Component">Creating a simple ZK Component</a>
 */
public class CustomZULComponent extends HtmlBasedComponent {

    private String _value = "component uninitialized";

    public String getValue() {
        return _value;
    }

    public void setValue(String newValue) {
        if (!_value.equals(newValue)) {
            _value = newValue;
            smartUpdate("value", _value);
        }
    }

    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "value", _value);
    }
}
