package org.apromore.item;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Thrown to indicate that serialized data cannot be interpreted as an expected
 * {@link Item} subtype.
 *
 * Best effort should be made to interpret the data, even in the presence of
 * validation errors.
 * When this exception is thrown we're giving up on helping the user fix their
 * data.
 */
public final class ItemFormatException extends Exception {

    /**
     * The type identifiers for the formats which failed to match the serialized
     * data.
     */
    private Set<String> formats = new HashSet<>();

    /**
     * @param newFormats  identifiers of the expected {@link Item} subtypes.
     *     This can be an empty list, but not <code>null</code>.
     * @param cause  see {@link Throwable#Throwable(Throwable cause)}
     */
    public ItemFormatException(final Collection<String> newFormats,
                               final Throwable cause) {
        super(cause);
        this.formats.addAll(newFormats);
    }

    /**
     * @param newFormats  identifiers of the expected {@link Item} subtypes.
     *     This can be an empty list, but not <code>null</code>.
     */
    public ItemFormatException(final Collection<String> newFormats) {
        this.formats.addAll(newFormats);
    }

    /**
     * @param format  identifier of the expected {@link Item} subtype
     * @param cause  see {@link Throwable#Throwable(Throwable cause)}
     */
    public ItemFormatException(final String format, final Throwable cause) {
        super(cause);
        this.formats.add(format);
    }

    /**
     * @param format  identifier of the expected {@link Item} subtype
     */
    public ItemFormatException(final String format) {
        this.formats.add(format);
    }

    @Override
    public String getMessage() {
        if (formats.isEmpty()) {
            return "Unable to interpret the input because the system currently "
                + " has no supported item formats.";
        }

        String message = "Unable to interpret the input as any of the "
            + "following formats";
        String separator = ": ";
        for (String format: formats) {
            message += separator;
            message += format;
            separator = ", ";
        }

        return message;
    }
}
