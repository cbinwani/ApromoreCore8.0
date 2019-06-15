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

import java.util.List;
import org.apromore.item.Item;

/**
 * A folder as a concrete {@link Item}.
 *
 * Unlike most {@link Item}s which are immutable, a folder has a mutable
 * ordered list of paths, its contents.
 * Use {@link FolderService#findItemByPath} to convert paths to {@link Item}s.
 */
public interface Folder extends Item {

    /**
     * Type identifier for folders.
     */
    String TYPE = "Folder";

    /**
     * Append an item to the contents of this folder.
     *
     * @param name  the leaf name of the new item
     * @param content  the content of the new document
     */
    void append(String name, Item content);

    /**
     * @return the ordered list of full paths contained by this folder
     */
    List<String> getPaths();
}
