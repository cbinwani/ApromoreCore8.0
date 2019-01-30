package org.apromore.folder.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Data access object for {@link org.apromore.folder.Folder}.
 */
@Entity
@Table(name = "FOLDER")
public final class FolderDAO {

    /**
     * Primary key.
     *
     * Note that this isn't automatically @Generated.
     * The id must be obtained from this folder's corresponding
     * {@link org.apromore.item.jpa.ItemDAO}.
     */
    @Id
    private Long id;

    /** Folder name, exclusive of path. */
    @Column(name = "NAME", unique = false, nullable = false)
    private String name;

    /** Parent folder, possibly <code>null</code>. */
    @ManyToOne
    @JoinColumn(name = "PARENT_FOLDER", unique = false, nullable = true)
    private FolderDAO parent;

    /** @return primary key */
    public Long getId() {
        return id;
    }

    /** @param newId  primary key */
    public void setId(final Long newId) {
        this.id = newId;
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
    public FolderDAO getParent() {
        return parent;
    }

    /** @param newParent  new parent folder, possibly <code>null</code> */
    public void setParent(final FolderDAO newParent) {
        this.parent = newParent;
    }
}
