package org.apromore.item.impl;

import org.apromore.item.Item;
import org.apromore.item.jpa.ItemDao;

/**
 * Implementation of {@link Item}.
 */
class ItemImpl implements Item {

    private Long id;

    private String type;

    ItemImpl(final ItemDao dao) {
        this.id = dao.getId();
        this.type = dao.getType();
    }


    // Accessors

    /** @return primary key */
    public Long getId() {
        return id;
    }

    /** @return a MIME- or UTI-like identifier that associates this Item with an appropriate ItemPlugin inplementation */
    public String getType() {
        return type;
    }
}
