package org.apromore.ui.spi;

import javax.security.auth.Subject;
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
     * @param bundleClassLoader  the classloader for the bundle containing the ZUL markup
     * @param uri  location within the <var>bundleClassLoader</var> of ZUL markup to create this component
     * @param arguments  -
     * @return a ZK component corresponding to the ZUL markup
     * @throws Error  if the ZUL markup isn't available; this is considered programming error (rather than an I/O exception) since ZUL access is a linking issue
     */
    public Component createComponent(ClassLoader bundleClassLoader, String uri, Map<?, ?> arguments);

    public Component getParentComponent();

    public void setComponent(Component component);

    public Set<Item> getSelection();

    public void setSelection(Set<Item> newSelection);

    /**
     * @return the currently authenticated user, or <code>NULL</code> for an anonymous user session
     */
    public User getUser();

    /**
     * @param newUser  the new authenticated user, or <code>NULL</code> to de-authenticate the current user session (i.e. log out).
     */
    public void setUser(User newUser);
}
