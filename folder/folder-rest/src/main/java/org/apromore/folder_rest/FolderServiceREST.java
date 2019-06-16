package org.apromore.folder_rest;

/*-
 * #%L
 * Apromore :: folder :: REST
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
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apromore.folder.FolderService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;;

/**
 * REST endpoint for the {@link FolderService}.
 */
@Path("/folder")
@Component(service = FolderServiceREST.class,
           property = { "osgi.jaxrs.resource=true" })
public class FolderServiceREST /* implements FolderService */ {

    /** Connection to the business logic. */
    @Reference
    private FolderService folderService;

    /** @return the paths contained by the root folder */
    //@Override
    @Path("/")
    @Produces("application/json")
    @GET
    public List<String> getRootFolderPaths() {
        return folderService.getRootFolderPaths();
    }
}
