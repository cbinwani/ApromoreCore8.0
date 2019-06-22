package org.apromore.file.spi;

/*-
 * #%L
 * Apromore :: file :: file-spi
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
import org.apromore.file.File;

/**
 * Class that wraps an {@link File}.
 *
 * This is a convenience for plugin providers needing to implement concrete
 * subtypes of {@link File}.
 */
public class FileWrapper implements File {

    /** The wrapped instance. */
    private File file;

    /** @param wrappedFile  the wrapped instance */
    public FileWrapper(final File wrappedFile) {
        this.file = wrappedFile;
    }


    // Implementation of File

    /** @return {@inheritDoc}  Returns the wrapped instance's id. */
    public Long getId() {
        return file.getId();
    }

    /** @return {@inheritDoc}  Returns the wrapped instance's type. */
    public String getType() {
        return file.getType();
    }

    /** {@inheritDoc
     *
     * This implementation defers to the wrapped instance.
     */
    @Override
    public InputStream getInputStream() {
        return file.getInputStream();
    }
}
