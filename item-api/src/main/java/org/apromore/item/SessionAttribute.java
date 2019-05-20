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

import org.zkoss.zk.ui.Sessions;

/**
 * Accessor for a property on a ZK session.
 *
 * @param <T>  the attribute type
 */
public abstract class SessionAttribute<T> {

    /** Concrete classes should initialize this in a static block. */
    @SuppressWarnings("checkstyle:VisibilityModifier")
    protected static SessionAttribute<?> instance;

    /** The attribute name. */
    private final String key;

    /** @return the attribute name */
    protected String getKey() {
        return key;
    }

    /** @param newKey  the attribute name */
    protected SessionAttribute(final String newKey) {
        this.key = newKey;
    }

    /** @return attribute value on the current ZK session */
    public T get() {
        return (T) Sessions.getCurrent().getAttribute(instance.key);
    }

    /** @param newValue  new attribute value for the current ZK session */
    public void set(final T newValue) {
        Sessions.getCurrent().setAttribute(instance.key, newValue);
    }
}
