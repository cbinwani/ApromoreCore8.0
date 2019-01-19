package org.apromore.item;

/**
 * An item is any entity stored within the Apromore repository, such as an event log or a process model.
 *
 * Conceptually, items are abstract; a separate SPI provides concrete subtypes of items, e.g. BPMN models.
 * The <var>type</var> property of an item identifies the subtype of item even if the service provider is unavailable,
 * and is used to associate existing items with a newly added service provider.
 *
 * Items are persistent and are backed by JPA storage.
 *
 * Items have access controls based on JAAS Principals.
 */
public interface Item {

    /**
     * @return the unique primary key identifying this item
     */
    Long getId();

    /**
     * @return MIME- or UTI-style identifier for the content of this item, e.g <pre>org.omg.bpmn</pre> for a BPMN process model
     */
    String getType();
}
