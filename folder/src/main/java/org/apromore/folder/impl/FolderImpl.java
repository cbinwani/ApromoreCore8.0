package org.apromore.folder.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apromore.folder.Folder;
import org.apromore.folder.jpa.FolderRepository;
import org.apromore.folder.jpa.PathDAO;
import org.apromore.item.Item;
import org.apromore.item.spi.ItemTypeException;
import org.apromore.item.spi.ItemWrapper;

/**
 * A folder as a concrete {@link Item}.
 *
 */
class FolderImpl extends ItemWrapper implements Folder {

    /** Used to manage paths. */
    private FolderRepository folderRepository;

    /** Full paths of any content. */
    private List<String> paths = new ArrayList<>();

    /**
     * @param item  a partially-constructed item from
     *     {@link org.apromore.item.jpa.ItemRepository}
     * @param newFolderRepository  never <code>null</code>
     * @throws ItemTypeException if the <i>item</i> isn't a folder
     */
    FolderImpl(final Item item, final FolderRepository newFolderRepository)
        throws ItemTypeException {

        super(item);

        if (!Folder.TYPE.equals(item.getType())) {
            throw new ItemTypeException(Folder.TYPE, item.getType());
        }

        this.folderRepository = newFolderRepository;
    }

    /**
     * @param name  leaf name of the new path
     * @param content  content at the new path
     */
    public void append(final String name, final Item content) {
        PathDAO dao = new PathDAO();
        dao.setItemId(content.getId());
        dao.setName(name);
        dao.setParent(folderRepository.findPathByItemId(getId()));
        folderRepository.addPath(dao);
    }

    /**
     * @return the ordered list of full paths contained by this folder
     */
    public List<String> getPaths() {
        PathDAO pathDAO = folderRepository.findPathByItemId(getId());
        List<PathDAO> children = folderRepository.findPathsByParent(pathDAO);
        return children.stream()
                       .map(path -> path.getName())
                       .collect(Collectors.toList());
    }
}
