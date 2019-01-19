package org.apromore.ui.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Define an ordering by supplying an explicit list of elements defining the
 * desired order.
 *
 * Items not in the explicit list precede ones that are.  For example,
 * <code>ExplicitComparator("one,two,three,four,five")</code>
 * would sort the set <code>{ "three", "six", "one" }<code> into the order
 * <code>"six", "one", "three"</code>.
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
        return examples.indexOf(o1) - examples.indexOf(o2);
    }
}
