package org.apromore.file.jpa;

/*-
 * #%L
 * Apromore :: file :: file-jpa
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

import java.util.List;

/**
 * Factory service for {@link FileDAO}.
 */
public interface FileRepository {

    /** @return all files */
    List<FileDAO> list();

    /**
     * @param id  private key
     * @return the corresponding file fields
     */
    FileDAO get(Long id);

    /** @param newDAO  new file fields to be persisted */
    void add(FileDAO newDAO);

    /** @param id  primary key */
    void remove(Long id);
}
