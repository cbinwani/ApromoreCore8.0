package org.apromore.folder.impl;

/*-
 * #%L
 * Apromore :: folder
 * %%
 * Copyright (C) 2018 - 2019 The Apromore Initiative
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

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

    @Override
    public String toString() {
        return super.toString() + " id=" + getId();
    }
}
