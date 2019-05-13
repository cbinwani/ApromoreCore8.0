package org.apromore.karaf.shell;

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
