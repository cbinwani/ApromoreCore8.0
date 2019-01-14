package org.apromore.item.spi;

public class ItemTypeException extends Exception {

    private final String actualType, expectedType;

    public ItemTypeException(String expectedType, String actualType) {
        this.actualType   = actualType;
        this.expectedType = expectedType;
    }

    public String getActualType() { return actualType; }

    public String getExpectedType() { return expectedType; }

    @Override
    public String getMessage() { return "Required item type " + expectedType + " but instead received " + actualType; }
}
