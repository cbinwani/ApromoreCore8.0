package org.apromore.ui.bpmn_io;

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

import java.io.IOException;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

/**
 * @see <a href="https://www.zkoss.org/wiki/ZK%20Component%20Development%20Essentials/Creating%20a%20simple%20ZK%20Component/Implementing%20the%20Component">Creating a simple ZK Component</a>
 */
public final class CustomZULComponent extends HtmlBasedComponent {

    /** A BPMN 2.0 serialization. */
    private String value = "component uninitialized";

    /** @return a BPMN 2.0 serialization */
    public String getValue() {
        return value;
    }

    /** @param newValue  a BPMN 2.0 serialization */
    public void setValue(final String newValue) {
        if (!value.equals(newValue)) {
            value = newValue;
            smartUpdate("value", value);
        }
    }

    @Override
    protected void renderProperties(final ContentRenderer renderer)
        throws IOException {

        super.renderProperties(renderer);
        render(renderer, "value", value);
    }
}
