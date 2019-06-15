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

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.support.table.ShellTable;
import org.apromore.folder.FolderService;

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
    private FolderService folderService;

    /** {@inheritDoc} */
    @Override
    public Object execute() throws Exception {
        ShellTable table = new ShellTable();
        table.column("Name");
        table.addRow().addContent("Dummy");
        table.print(System.out);
        return null;
    }
}
