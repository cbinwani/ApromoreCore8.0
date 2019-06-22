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

import org.apromore.file.File;
import org.apromore.item.NotAuthorizedException;

/**
 * Provided to SPI clients.
 */
public interface FilePluginContext {

    /**
     * This method should only be used by service providers, who are responsible
     * for creating a file format of the given <i>type</i>.
     *
     * @param type  used to initialize the created {@link File}
     * @return an incomplete {@link Item}; the caller is responsible for
     *     completing the concrete subtype
     * @throws NotAuthorizedException if a lack of authorization prevents the
     *     file from being created
     */
    File create(String type) throws NotAuthorizedException;

    /**
     * @param id  the primary key identifier of an existing {@link File}, never
     *     <code>null</code>
     * @return either the unique {@link File} with the given <i>id</i>, or
     *     <code>null</code> if no such item exists
     * @throws NotAuthorizedException if a lack of authorization prevents the
     *     file from being created
     */
    File getById(Long id) throws NotAuthorizedException;
}
