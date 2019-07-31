package org.apromore.ui.impl;

/*-
 * #%L
 * Apromore :: ui
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Define an ordering by supplying an explicit list of elements defining the
 * desired order.
 *
 * For example <code>ExplicitComparator("one,two,three,four,five")</code>
 * would sort the set <code>{ "three", "six", "one" }</code> into the order
 * <code>"six", "one", "three"</code>.
 *
 * Items not in the explicit list precede ones that are, and are ordered
 * lexicography amongst themselves.
 */
class ExplicitComparator implements Comparator<String> {

    /** The explicit order. */
    private List<String> examples;


    // Constructor

    /**
     * @param csv  Comma-separated list of strings in the desired order
     */
    ExplicitComparator(final String csv) {
        examples = Arrays.asList(csv.split(","));
    }


    // Implementation of the Comparator interface

    @Override
    public int compare(final String o1, final String o2) {
        if (examples.contains(o1) || examples.contains(o2)) {
            return examples.indexOf(o1) - examples.indexOf(o2);
        } else {
            return o1.compareTo(o2);
        }
    }
}
