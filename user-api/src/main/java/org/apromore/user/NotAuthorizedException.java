package org.apromore.user;

/**
 * The caller lacks authorization to perform the throwing function.
 *
 * In the case where the caller is unauthenticated, this exception
 * should prompt the user to authenticate and reattempt the function.
 */
public class NotAuthorizedException extends Exception {

    /**
     * @param newPermission  the permission sought and denied
     */
    public NotAuthorizedException(final String newPermission) {
        super("Missing permission: " + newPermission);
    }
}
