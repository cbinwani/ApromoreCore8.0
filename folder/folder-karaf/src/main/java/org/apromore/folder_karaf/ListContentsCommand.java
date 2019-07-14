package org.apromore.folder_karaf;

/*-
 * #%L
 * Apromore :: folder :: karaf
 * %%
 * Copyright (C) 2019 The Apromore Initiative
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

import java.util.List;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.support.table.Row;
import org.apache.karaf.shell.support.table.ShellTable;
import org.apromore.Caller;
import org.apromore.folder.FolderService;
import org.apromore.item.Item;

/**
 * Command <code>apromore:list-contents</code> for the Karaf shell.
 */
@Service
@Command(scope       = "apromore",
         name        = "list-contents",
         description = "List folder contents")
public class ListContentsCommand implements Action {

    /** Folder service. */
    @Reference
    @SuppressWarnings("nullness")
    private FolderService folderService;

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    public Object execute() throws Exception {
        Caller caller = new org.apromore.AbstractCaller();
        ShellTable table = new ShellTable();
        List<String> paths = folderService.getRootFolderPaths(caller);
        table.column("ID");
        table.column("Path");
        table.column("Type");
        for (String path: paths) {
            Item item = folderService.findItemByPath(path, caller);
            Row row = table.addRow();
            row.addContent(item == null ? "-" : item.getId());
            row.addContent(path);
            row.addContent(item == null ? "-" : item.getType());
        }
        table.print(System.out);
        return String.format("Displayed %d result(s)", paths.size());
    }
}
