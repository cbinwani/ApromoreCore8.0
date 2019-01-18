package org.apromore.xes_item.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "XES_ITEM")
public class XESItemDAO {

    @Id  // Note that this isn't @Generated; the id must be supplied by an ItemDAO
    private Long id;

    @Column(name="XML_SERIALIZATION", unique = false, nullable = false)
    private byte[] xmlSerialization;

    public Long getId() {
        return id;
    }

    public void setId(Long newId) {
        this.id = newId;
    }

    public byte[] getXmlSerialization() {
        return xmlSerialization;
    }

    public void setXmlSerialization(byte[] newXmlSerialization) {
        this.xmlSerialization = newXmlSerialization;
    }
}
