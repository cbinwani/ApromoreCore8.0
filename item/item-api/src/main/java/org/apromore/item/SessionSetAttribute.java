package org.apromore.item;

/*-
 * #%L
 * Apromore :: item-api
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

import java.util.HashSet;
import java.util.Set;

/**
 * Accessor for a {@link Set}-valued attribute on a ZK session.
 *
 * @param <T> the element type
 */
public abstract class SessionSetAttribute<T>
    extends SessionAttribute<Set<T>> {

    /** @param key  the attribute name */
    protected SessionSetAttribute(final String key) {
        super(key);
        set(new HashSet<T>());
    }

    /** @return attribute value on the current ZK session */
    public Set<T> get() {
        return (Set<T>) super.get();
    }

    /**
     * @param newValue  new attribute value for the current ZK session
     * @throws IllegalArgumentException if <i>newValue</i> is <code>null</code>
     */
    public void set(final Set<T> newValue) {
        if (newValue == null) {
            throw new IllegalArgumentException("Set can be empty, not null");
        }
        super.set(newValue);
    }
}
