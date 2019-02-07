package org.apromore.folder;

/**
 * Reports a path namespace collision.
 */
public class PathAlreadyExistsException extends Exception {

    /** The pre-existing path. */
    private String path;

    /** @param existingPath  the pre-existing path */
    public PathAlreadyExistsException(final String existingPath) {
        super("Folder already exists: " + existingPath);

        this.path = existingPath;
    }

    /** @return the pre-existing path */
    public String getPath() {
        return path;
    }
}
