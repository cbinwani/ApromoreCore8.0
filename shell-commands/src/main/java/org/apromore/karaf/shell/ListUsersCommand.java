package org.apromore.karaf.shell;

/*-
 * #%L
 * Apromore :: shell-commands
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
import org.osgi.service.useradmin.UserAdmin;

/**
 * Command <code>apromore:list-users</code> for the Karaf shell.
 */
@Service
@Command(scope       = "apromore",
         name        = "list-users",
         description = "List apromore users")
public class ListUsersCommand implements Action {

    /** User administration service. */
    @Reference
    private UserAdmin userAdmin;

    /** {@inheritDoc} */
    @Override
    public Object execute() throws Exception {
        ShellTable table = new ShellTable();
        table.column("User ID");
        table.addRow().addContent("Dummy");
        table.print(System.out);
        return null;
    }
}
