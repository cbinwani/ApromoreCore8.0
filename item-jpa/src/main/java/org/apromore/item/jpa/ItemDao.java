package org.apromore.item.jpa;

import javax.persistence.Entity;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Data access object for common fields of {@link org.apromore.item.Item}
 * instances.
 *
 * The fields specific to any particular concrete subtype are stored in
 * their own supplementary DAOs.
 */
@Entity
@Table(name = "ITEM")
public final class ItemDAO {

    /** Primary key. */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** The type identifier of the concrete subclass. */
    private String type;

    /** @return primary key */
    public Long getId() {
        return id;
    }

    /** @param newId  the type identifier of the concrete subclass */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /** @return the type identifier of the concrete subclass */
    public String getType() {
        return type;
    }

    /** @param newType  the type identifier of the concrete subclass */
    public void setType(final String newType) {
        this.type = newType;
    }
}
