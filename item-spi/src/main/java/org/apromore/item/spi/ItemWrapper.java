package org.apromore.item.spi;

import org.apromore.item.Item;

/**
 * Class that wraps an {@link Item}.
 *
 * This is a convenience for plugin providers needing to implement concrete
 * subtypes of {@link Item}.
 */
public class ItemWrapper implements Item {

    /** The wrapped instance. */
    private Item item;

    /** @param wrappedItem  the wrapped instance */
    public ItemWrapper(final Item wrappedItem) {
        this.item = wrappedItem;
    }


    // Implementation of Item

    /** @return {@inheritDoc}  Returns the wrapped instance's id. */
    public Long getId() {
        return item.getId();
    }

    /** @return {@inheritDoc}  Returns the wrapped instance's type. */
    public String getType() {
        return item.getType();
    }
}
