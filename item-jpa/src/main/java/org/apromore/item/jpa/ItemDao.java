package org.apromore.item.jpa;

import javax.persistence.Entity;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ITEM")
public class ItemDao {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(final Long newId) {
        this.id = newId;
    }

    public String getType() {
        return type;
    }

    public void setType(final String newType) {
        this.type = newType;
    }
}
