package org.apromore.item;

/*-
 * #%L
 * Apromore :: item-api
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

import java.util.HashSet;
import java.util.Set;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

/**
 * The {@link Item}s selected for actions in user interface.
 */
public abstract class Selection {

    /** @return selection */
    public static Set<Item> getSelection() {
        Set<Item> result = (Set<Item>)
            Sessions.getCurrent().getAttribute("selection");
        if (result == null) {
            result = new HashSet<Item>();
        }
        return result;
    }

    /** @param newSelection */
    public static void setSelection(final Set<Item> newSelection) {
        Sessions.getCurrent().setAttribute("selection", newSelection);
        EventQueues.lookup("q", Sessions.getCurrent(), true)
                   .publish(new Event("onSelect"));
    }
}
