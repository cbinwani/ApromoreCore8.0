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

    /** The type identifiers for the formats which failed to match the serialized data. */
    private Set<String> formats = new HashSet<>();

    public ItemFormatException(final Collection<String> newFormats, final Throwable cause) {
        super(cause);
        this.formats.addAll(newFormats);
    }

    public ItemFormatException(final Collection<String> newFormats) {
        this.formats.addAll(newFormats);
    }

    public ItemFormatException(final String format, final Throwable cause) {
        super(cause);
        this.formats.add(format);
    }

    public ItemFormatException(final String format) {
        this.formats.add(format);
    }

    public String getMessage() {
        if (formats.isEmpty()) {
            return "Unable to interpret the input because the system currently has no supported item formats.";
        }

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
