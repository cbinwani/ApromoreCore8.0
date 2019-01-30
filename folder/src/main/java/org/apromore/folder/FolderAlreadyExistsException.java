package org.apromore.folder;

/**
 * Reports a folder namespace collision.
 */
public class FolderAlreadyExistsException extends Exception {

    /** The pre-existing folder. */
    private Folder folder;

    /** @param existingFolder  the pre-existing folder */
    public FolderAlreadyExistsException(final Folder existingFolder) {
        super("Folder already exists: " + existingFolder);

        this.folder = existingFolder;
    }

    /** @return the pre-existing folder */
    public Folder getFolder() {
        return folder;
    }
}
