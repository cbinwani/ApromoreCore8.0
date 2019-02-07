package org.apromore.folder.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Data access object for {@link org.apromore.folder.Folder}.
 */
@Entity
@Table(name = "PATH")
public final class PathDAO {

    /** Primary key. */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Contained item identifier. */
    @Column(name = "ITEM_ID", unique = false, nullable = false)
    private Long itemId;

    /** Item name, exclusive of path. */
    @Column(name = "NAME", unique = false, nullable = false)
    private String name;

    /** Parent folder, possibly <code>null</code>. */
    @ManyToOne
    @JoinColumn(name = "PARENT_PATH", unique = false, nullable = true)
    private PathDAO parent;

    /** @return primary key */
    public Long getId() {
        return id;
    }

    /** @param newId  primary key */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /** @return item content */
    public Long getItemId() {
        return itemId;
    }

    /** @param newItemId  new content */
    public void setItemId(final Long newItemId) {
        this.itemId = newItemId;
    }

    /** @return non-empty text */
    public String getName() {
        return name;
    }

    /** @param newName  non-empty text */
    public void setName(final String newName) {
        this.name = newName;
    }

    /** @return parent folder, possibly <code>null</code> */
    public PathDAO getParent() {
        return parent;
    }

    /** @param newParent  new parent folder, possibly <code>null</code> */
    public void setParent(final PathDAO newParent) {
        this.parent = newParent;
    }
}
