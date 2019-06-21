package org.apromore.file.impl;

/*-
 * #%L
 * Apromore :: file :: file-logic
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apromore.file.File;
import org.apromore.file.jpa.FileDAO;
import org.apromore.item.Item;
import org.apromore.item.spi.ItemWrapper;

/**
 * A {@link File} as a concrete {@link Item}.
 *
 */
class FileImpl extends ItemWrapper implements File {

    /** The serialized document. */
    private byte[] content;

    /**
     * @param item  a partially-constructed item from
     *     {@link org.apromore.item.jpa.ItemRepository}
     * @param dao  data access object for file-specific fields
     */
    FileImpl(final Item item, final FileDAO dao) {
        super(item);

        assert item.getId() == dao.getId();
        this.content = dao.getContent();
    }


    // Implementation of File

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }
}
