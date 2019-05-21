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
import java.util.Collections;
import java.util.Set;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

/**
 * The {@link Item}s selected for actions in the user interface.
 */
public abstract class Selection {

    /**
     * Key of the selection attribute on the ZK session.
     */
    private static final String ATTRIBUTE = "selection";

    /**
     * @return the currently selected {@link Item}s; this is always
     *     an immutable set and never <code>null</code>, but might be
     *     empty
     */
    public static Set<Item> getSelection() {
        Set<Item> result = (Set<Item>)
            Sessions.getCurrent().getAttribute(ATTRIBUTE);
        if (result == null) {
            result = Collections.emptySet();
        }
        return result;
    }

    /**
     * Change the selection.
     *
     * The {@link org.zkoss.zk.ui.event.EventQueue} "q" will receive an
     * "onSelect" {@link Event}.
     *
     * @param newSelection  the new set of selected {@link Item}s
     * @throws IllegalArgumentException if <i>newSelection</i> is
     *     <code>null</code>
     */
    public static void setSelection(final Set<Item> newSelection) {
        Sessions.getCurrent().setAttribute(
            ATTRIBUTE,
            Collections.unmodifiableSet(newSelection)
        );
        EventQueues.lookup("q", Sessions.getCurrent(), true)
                   .publish(new Event("onSelect"));
    }
}
