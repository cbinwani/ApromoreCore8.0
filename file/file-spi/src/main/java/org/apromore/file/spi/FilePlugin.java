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
import java.io.IOException;
import org.apromore.file.File;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;

/**
 * Services required to support a particular {@link File} format.
 *
 * @param <T>  the interface of the format
 */
public interface FilePlugin<T extends File> {

    /**
     * @param inputStream  no guarantee is made as to whether the stream will be
     *     closed after this method returns
     * @return a concrete subclass of {@link Item}
     * @throws IOException  if the <i>inputStream</i> cannot be read
     * @throws ItemFormatException  if the <i>inputStream</i> cannot be
     *     parsed
     * @throws NotAuthorizedException  if the caller isn't authorized to create
     *     the {@link Item}
     */
    T create(InputStream inputStream) throws IOException, ItemFormatException,
       NotAuthorizedException;

    /** {@inheritDoc} */
    String getType();

    /**
     * Used to convert instances of {@link Item} to their particular concrete
     * subtype (for example, <code>org.apromore.bpmn_item.BPMNItem</code>).
     *
     * @param file  an existing file of the format managed by this plugin
     * @return the corresponding instance of a {@link File} format
     * @throws FileTypeException  if the <i>file</i> is not of the format
     *     managed by this plugin
     */
    T toConcreteFile(File file) throws FileTypeException;
}
