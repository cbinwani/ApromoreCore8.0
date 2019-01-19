package org.apromore.bpmn_item.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Data access object for {@link org.apromore.bpmn_item.BPMNItem}.
 */
@Entity
@Table(name = "BPMN_ITEM")
public class BPMNItemDAO {

    /**
     * Primary key.
     *
     * Note that this isn't automatically @Generated.
     * The id must be obtained from this XES item's corresponding {@link org.apromore.item.jpa.ItemDAO}.
     */
    @Id  // Note: not @Generated; the id must be supplied by an ItemDAO
    private Long id;

    /**
     * The BPMN 2.0 XML document text.
     *
     * This is stored as bytes rather than characters because technically XML is binary and contains its
     * own character encoding information.
     */
    @Column(name = "XML_SERIALIZATION", unique = false, nullable = false)
    private byte[] xmlSerialization;

    /** @return primary key */
    public final Long getId() {
        return id;
    }

    /** @param newId  primary key */
    public final void setId(final Long newId) {
        this.id = newId;
    }

    /** @return BPMN 2.0 document text */
    public final byte[] getXmlSerialization() {
        return xmlSerialization;
    }

    /** @param newXmlSerialization  BPMN 2.0 document text */
    public final void setXmlSerialization(final byte[] newXmlSerialization) {
        this.xmlSerialization = newXmlSerialization;
    }
}
