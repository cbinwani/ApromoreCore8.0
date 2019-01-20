package org.apromore.item;

/**
 * An item is any entity stored within the Apromore repository, such as an event
 * log or a process model.
 *
 * Conceptually, items are abstract; a separate SPI
 * {@link org.apromore.item.spi.ItemPlugin} provides concrete subtypes of
 * items, e.g. BPMN models.
 *
 * Items are persistent.
 *
 * Items have access controls based on JAAS Principals.
 */
public interface Item {

    /**
     * Every item has a unique primary key.
     *
     * Furthermore, each item ought to have some concrete subtype instance with
     * the same key, e.g. a {@link org.apromore.bpmn_item.BPMNItem}.
     * "Ought", because if the subtype {@link org.apromore.item.spi.ItemPlugin}
     * is uninstalled, an item might be orphaned until the plugin is
     * reinstalled.
     *
     * @return primary key
     */
    Long getId();

    /**
     * Identifies the subtype of the item.
     *
     * This is the case even if the service provider is unavailable, and is used
     * to associate existing items with a newly added service provider.
     *
     * @return MIME- or UTI-style identifier for the content of this item, e.g
     *     <code>org.omg.bpmn</code> for a BPMN process model
     * @see org.apromore.bpmn_item.BPMNItem#TYPE
     * @see org.apromore.xes_item.XESItem#TYPE
     */
    String getType();
}
