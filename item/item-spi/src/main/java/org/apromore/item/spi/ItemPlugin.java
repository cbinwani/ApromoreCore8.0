package org.apromore.item.spi;

/*-
 * #%L
 * Apromore :: item-spi
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

import java.io.InputStream;
import java.io.IOException;
import org.apromore.Caller;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.NotAuthorizedException;

/**
 * Services required to provide a concrete subtype of {@link Item}.
 *
 * @param <T>  the interface of the concrete subtype
 */
public interface ItemPlugin<T extends Item> {

    /**
     * @param inputStream  no guarantee is made as to whether the stream will be
     *     closed after this method returns
     * @param caller  authorization to create the item
     * @return a concrete subclass of {@link Item}
     * @throws IOException  if the <i>inputStream</i> cannot be read
     * @throws ItemFormatException  if the <i>inputStream</i> cannot be
     *     parsed
     * @throws NotAuthorizedException  if the <i>caller</i> isn't authorized to
     *     create the {@link Item}
     */
    T create(InputStream inputStream, Caller caller)
       throws IOException, ItemFormatException, NotAuthorizedException;

    /** {@inheritDoc} */
    String getType();

    /**
     * Used to convert instances of {@link Item} to their particular concrete
     * subtype (for example, <code>org.apromore.bpmn_item.BPMNItem</code>).
     *
     * @param item  an existing item of the subtype managed by this plugin
     * @return the corresponding instance of a concrete subclass of {@link Item}
     * @throws ItemTypeException  if the <i>item</i> is not an instance of
     *     the subtype managed by this plugin
     */
    T toConcreteItem(Item item) throws ItemTypeException;
}
