package org.apromore.bpmn_item;

/*-
 * #%L
 * Apromore :: bpmn-item
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

import javax.xml.transform.Source;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Factory service for {@link BPMNItem} instances.
 */
public interface BPMNItemService {

    /**
     * @param source  a valid XML serialization of a BPMN 2.0 document, or
     *     <code>null</code> to create an empty document.
     * @return an instance representing the stored document
     * @throws ItemFormatException if the <i>source</i> can't be interpreted as
     *     BPMN 2.0
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     item creation
     */
    BPMNItem createBPMNItem(Source source) throws ItemFormatException,
        NotAuthorizedException;

    /**
     * @param id  primary key
     * @return the corresponding BPMN item if one exists, <code>null</code>
     *     otherwise (which includes the case of a non-BPMN item existing with
     *     that id)
     * @throws NotAuthorizedException if the caller's credentials do not permit
     *     reading the existing item
     */
    @Nullable
    BPMNItem getById(Long id) throws NotAuthorizedException;
}
