package org.apromore.xes_item.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Data access object for {@link org.apromore.xes_item.XESItem}.
 */
@Entity
@Table(name = "XES_ITEM")
public final class XESItemDAO {

    /**
     * Primary key.
     *
     * Note that this isn't automatically @Generated.
     * The id must be obtained from this XES item's corresponding
     * {@link org.apromore.item.jpa.ItemDAO}.
     */
    @Id
    private Long id;

    /**
     * The XES 1.0 XML document text.
     *
     * This is stored as bytes rather than characters because technically XML is
     * binary and contains its own character encoding information.
     */
    @Column(name = "XML_SERIALIZATION", unique = false, nullable = false)
    private byte[] xmlSerialization;

    /** @return primary key */
    public Long getId() {
        return id;
    }

    /** @param newId  primary key */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /** @return XES 1.0 document text */
    public byte[] getXmlSerialization() {
        return xmlSerialization;
    }

    /** @param newXmlSerialization  XES 1.0 document text */
    public void setXmlSerialization(final byte[] newXmlSerialization) {
        this.xmlSerialization = newXmlSerialization;
    }
}
