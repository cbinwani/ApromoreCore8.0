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
import java.io.IOException;
import org.apromore.item.NotAuthorizedException;

/**
 * Factory service for {@link File} instances.
 */
public interface FileService {

    /**
     * @param inputStream  a stream to populate the {@link File}, or
     *     <code>null</code> to create an empty document
     * @return an instance representing the stored document
     * @throws IOException if <code>inputStream</code> can't be read
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     file creation
     */
    File createFile(InputStream inputStream) throws IOException,
        NotAuthorizedException;

    /**
     * @param id  primary key
     * @return the corresponding {@link File} if one exists, <code>null</code>
     *     otherwise (which includes the case of a non-{@link File} item
     *     existing with that id)
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the existing item
     */
    File getById(Long id) throws NotAuthorizedException;
}
