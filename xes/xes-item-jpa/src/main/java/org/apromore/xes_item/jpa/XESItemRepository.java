package org.apromore.xes_item.jpa;

/*-
 * #%L
 * Apromore :: xes-item-jpa
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
 * Factory service for {@link XESItemDAO}.
 */
public interface XESItemRepository {

    /** @return all XES items */
    List<XESItemDAO> list();

    /**
     * @param id  private key
     * @return the corresponding XES item fields
     */
    @Nullable
    XESItemDAO get(Long id);

    /** @param newDAO  new XES item fields to be persisted */
    void add(XESItemDAO newDAO);

    /**
     * @param id  primary key
     * @throws EntityNotFoundException if <i>id</i> isn't an
     *     existing XES item
     */
    void remove(Long id) throws EntityNotFoundException;
}
