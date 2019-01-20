package org.apromore.ui.spi;

import java.util.Map;
import java.util.Set;
import org.apromore.item.Item;
import org.apromore.item.User;
import org.zkoss.zk.ui.Component;

/**
 * Passed to {@link UIPlugin#execute} and {@link UIPlugin#isEnabled}.
 *
 * This provides information such as the selection and view controller hosting.
 */
public interface UIPluginContext {

    /**
     * @param bundleClassLoader  the classloader for the bundle containing the
     *     ZUL markup
     * @param uri  location within the <i>bundleClassLoader</i> of ZUL markup
     *     to create this component
     * @param arguments  see {@link org.zkoss.zk.ui.Component}'s
     *     <code>createComponent</code> methods
     * @return a ZK component corresponding to the ZUL markup
     * @throws Error  if the ZUL markup isn't available; this is considered a
     *     programming error (rather than an I/O exception) since ZUL access is
     *     a linking issue
     */
    Component createComponent(ClassLoader bundleClassLoader,
                              String      uri,
                              Map<?, ?>   arguments);

    /**
     * @return the ZK component representing the canvas area of the main
     *     window
     */
    Component getParentComponent();

    /**
     * @param component  the ZK component representing the canvas area of the
     *     main window
     */
    void setComponent(Component component);

    /**
     * @return selected elements, possibly empty but never <code>null</code>
     */
    Set<Item> getSelection();

    /**
     * @param newSelection  selected elements, possibly empty but never
     *     <code>null</code>
     */
    void setSelection(Set<Item> newSelection);

    /**
     * @return the currently authenticated user, or <code>NULL</code> for an
     *     anonymous user session
     */
    User getUser();

    /**
     * @param newUser  the new authenticated user, or <code>NULL</code> to
     *     de-authenticate the current user session (i.e. log out).
     */
    void setUser(User newUser);
}
