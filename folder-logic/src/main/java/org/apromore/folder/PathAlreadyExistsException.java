package org.apromore.folder;

/*-
 * #%L
 * Apromore :: folder
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

/**
 * Reports a path namespace collision.
 */
public class PathAlreadyExistsException extends Exception {

    /** The pre-existing path. */
    private String path;

    /** @param existingPath  the pre-existing path */
    public PathAlreadyExistsException(final String existingPath) {
        super("Folder already exists: " + existingPath);

        this.path = existingPath;
    }

    /** @return the pre-existing path */
    public String getPath() {
        return path;
    }
}
