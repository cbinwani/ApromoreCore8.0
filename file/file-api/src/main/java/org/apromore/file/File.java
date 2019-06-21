package org.apromore.file;

/*-
 * #%L
 * Apromore :: file :: file-api
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
import org.apromore.item.Item;

/**
 * A sequence of bytes as a concrete {@link Item}.
 *
 */
public interface File extends Item {

    /**
     * Type identifier for files.
     */
    String TYPE = "File";

    /**
     * @return a stream containing the file content
     */
    InputStream getInputStream();
}
