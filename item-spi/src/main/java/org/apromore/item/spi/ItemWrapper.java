package org.apromore.item.spi;

import java.security.Principal;
import org.apromore.item.Item;

/**
 * Class that wraps an {@link Item}.
 *
 * This is a convenience for plugin providers needing to implement concrete subtypes of {@link Item}.
 */
public class ItemWrapper implements Item {

    public Item item;


    // Implementation of Item

    public Long getId() {
        return item.getId();
    }

    public String getType() {
        return item.getType();
    }
}
