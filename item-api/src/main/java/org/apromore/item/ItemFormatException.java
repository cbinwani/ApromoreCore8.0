package org.apromore.item;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Thrown to indicate that serialized data cannot be interpreted as an expected {@link Item} subtype.
 *
 * Best effort should be made to interpret the data, even in the presence of validation errors.
 * When this exception is thrown we're giving up on helping the user fix their data.
 */
public class ItemFormatException extends Exception {

    private Set<String> formats = new HashSet<>();

    public ItemFormatException(Collection<String> formats, Throwable cause) {
        super(cause);
        this.formats.addAll(formats);
    }

    public ItemFormatException(Collection<String> formats) {
        this.formats.addAll(formats);
    }

    public ItemFormatException(String format, Throwable cause) {
        super(cause);
        this.formats.add(format);
    }

    public ItemFormatException(String format) {
        this.formats.add(format);
    }

    public String getMessage() {
        String message = "Unable to interpret the input as any of the following formats";
        String separator = ": ";
        for (String format: formats) {
            message += separator;
            message += format;
            separator = ", ";
        }

        return message;
    }
}
