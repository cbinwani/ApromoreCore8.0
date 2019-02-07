package org.apromore.item.spi;

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
