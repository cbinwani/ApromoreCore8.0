package org.apromore.ui.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Define an ordering by supplying an explicit list of elements in the desired order.
 */
class ExplicitComparator implements Comparator<String> {

    protected List<String> examples;
    

    // Constructor

    /**
     * @param csv  Comma-separated list of strings in the desired order
     */
    ExplicitComparator(String csv) {
        examples = Arrays.asList(csv.split(","));
    }


    // Implementation of the Comparator interface

    @Override
    public int compare(String o1, String o2) {
        return examples.indexOf(o1) - examples.indexOf(o2);
    }
}
