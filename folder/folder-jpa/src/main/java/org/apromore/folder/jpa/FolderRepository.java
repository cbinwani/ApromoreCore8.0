package org.apromore.folder.jpa;

/*-
 * #%L
 * Apromore :: folder-jpa
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
import javax.persistence.EntityNotFoundException;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Factory service for {@link PathDAO}.
 */
public interface FolderRepository {

    /** @param newDAO  new folder fields to be persisted */
    void addPath(PathDAO newDAO);

    /**
     * @param parent  may by <code>null</code>
     * @param name  never <code>null</code>
     * @return primary key of the <code>org.apromore.item.Item</code> content if
     *     it exists, <code>null</code> otherwise
     */
    @Nullable
    Long findItemIdByParentAndName(@Nullable PathDAO parent, String name);

    /**
     * @param parent  may by <code>null</code>
     * @return all paths with the given <i>parent</i>
     */
    List<PathDAO> findPathsByParent(@Nullable PathDAO parent);

    /**
     * @param id  primary key
     * @return the corresponding path
     */
    @Nullable
    PathDAO findPath(Long id);

    /**
     * Look up a path by its content.
     *
     * Note that more than one path may have the same content; this occurs
     * whenever a generic <code>org.apromore.item.Item</code> is copied, for
     * instance.
     * <code>org.apromore.folder.Folder</code>s are the exception, and never
     * have multiple paths.
     *
     * @param itemId  primary key of an <code>org.apromore.item.Item</code>
     * @return the corresponding path, or <code>null</code> if the number of
     *      paths with that item isn't exactly one
     */
    @Nullable
    PathDAO findPathByItemId(Long itemId);

    /**
     * @param id  primary key
     * @throws EntityNotFoundException if <i>id</i> isn't the primary key of
     *     an extant path
     */
    void removePath(Long id) throws EntityNotFoundException;
}
