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

import java.util.Objects;

/**
 * Required a particular subtype of {@link org.apromore.item.Item}, but received
 * another.
 */
public final class ItemTypeException extends Exception {

    /** The unexpected type identifier that was encountered. */
    private final String actualType;

    /** The required type identifier. */
    private final String expectedType;

    /**
     * @param newExpectedType  the required type identifier
     * @param newActualType  the unexpected type identifier that was
     *     encountered, which must differ from <i>newExpectedType</i>
     * @throws Error if <i>newExpectedType</i> equals <i>newActualType</i>
     */
    public ItemTypeException(final String newExpectedType,
                             final String newActualType) {
        this.actualType   = newActualType;
        this.expectedType = newExpectedType;

        if (Objects.equals(actualType, expectedType)) {
            throw new Error("Expected and actual types can't both be: "
                + actualType);
        }
    }

    /** @return the unexpected type identifier that was encountered */
    public String getActualType() {
        return actualType;
    }

    /** @return the required type identifier */
    public String getExpectedType() {
        return expectedType;
    }

    @Override
    public String getMessage() {
        return "Required item type " + expectedType + " but instead received "
            + actualType;
    }
}
