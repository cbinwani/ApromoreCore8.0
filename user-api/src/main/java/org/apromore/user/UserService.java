package org.apromore.user;

/**
 * User account services.
 */
public interface UserService {

    /**
     * Prompt the user to log in.
     *
     * @param reason  explanation to the user of why what they want to do
     *     requires then to authenticate
     * @param success  executed after the user successfully logs in
     * @param failure  executed after the login is cancelled
     */
    void authenticate(String reason, Runnable success, Runnable failure);

    /**
     * Verify that a given permission is granted to the current user session.
     *
     * @param permission  a named permission, e.g. <code>"create"</code>
     *     authorizing item storage
     * @throws NotAuthorizedException if the <i>permission</i> is not
     *     granted to the current user session
     */
    void authorize(String permission) throws NotAuthorizedException;

    /**
     * @return the authenticated user, or <code>null</code> if the user hasn't
     *     logged in
     */
    User getUser();
}
