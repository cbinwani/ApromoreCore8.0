package org.apromore.ui.impl;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.security.Principal;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.apromore.item.NotAuthorizedException;
import org.apromore.item.User;
import org.apromore.ui.UIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class UIServiceImpl implements UIService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UIServiceImpl.class);

    /** Attribute key on the ZK {@link org.zkoss.zk.ui.Session} containing the authenticated user as a username {@link String}. */
    public static final String ZK_SESSION_USER_ATTRIBUTE = "user";

    /** @see {@link javax.security.auth.login.Configuration} */
    private String loginConfigurationName;

    /** Which {@link Principal} class identifies a user?
     *
     * These classes are typically proprietary to the login module.
     * There may be more than one reasonable choice, e.g.
     * <code>com.sun.security.auth.UserPrincipal</code> with name <code>jsmith</code> versus
     * <code>com.sun.security.auth.LdapPrincipal</code> with name <code>uid=jsmith,ou=staff,o=acme</code>.
     */
    private Class userPrincipalClass;


    // Property accessors

    public void setLoginConfigurationName(String newLoginConfigurationName) {
        this.loginConfigurationName = newLoginConfigurationName;
    }

    public void setUserPrincipalClass(Class newUserPrincipalClass) {
        this.userPrincipalClass = newUserPrincipalClass;
    }


    // Implementation of UIService

    public void authenticate(String reason, Runnable success, Runnable failure) {
        Window window;

        if (getUser() != null) {
            if (success != null) {
                success.run();
            }
            return;
        }

        try {
            Reader reader = new InputStreamReader(UIServiceImpl.class.getClassLoader().getResourceAsStream("zul/login.zul"), "UTF-8");
            window = (Window) Executions.createComponentsDirectly(reader, "zul", null, null);

        } catch (IOException e) {
            throw new Error("ZUL resource login.zul could not be created as as ZK component", e);
        }
        assert window != null;

        ((Label) window.getFellow("reasonLabel")).setValue(reason);

        window.getFellow("loginButton").addEventListener("onClick", new EventListener<Event>() {
            public void onEvent(Event event) throws LoginException {
                String username = ((Textbox) window.getFellow("username")).getValue();
                String password = ((Textbox) window.getFellow("password")).getValue();
                LOGGER.debug("Login user " + username);

                LoginContext loginContext = new LoginContext(loginConfigurationName, new CallbackHandler() {
                    public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
                        for (Callback callback: callbacks) {
                            if (callback instanceof NameCallback) {
                                ((NameCallback) callback).setName(username);
                            }
                            else if (callback instanceof PasswordCallback) {
                                ((PasswordCallback) callback).setPassword(password.toCharArray());
                            }
                            else {
                                throw new UnsupportedCallbackException(callback, "Unimplemented callback");
                            }
                        }
                    }
                });

                loginContext.login();
                try {
                    for (Principal principal: loginContext.getSubject().getPrincipals()) {
                        LOGGER.debug("Principal: " + principal + "  class: " + principal.getClass());
                    }

                    final String user = loginContext
                        .getSubject()
                        .getPrincipals()
                        .stream()
                        .filter(principal -> userPrincipalClass.isAssignableFrom(principal.getClass()))
                        .findAny()  // TODO: validate that there's a unique result
                        .get()
                        .getName();
                    window.detach();
                    setUser(user);

                    // TODO: failure isn't invoked in the case of a LoginException

                } finally {
                    loginContext.logout();
                }

                if (success != null) {
                    success.run();
                }
            }
        });

        window.getFellow("cancelButton").addEventListener("onClick", new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                window.detach();
                if (failure != null) {
                    failure.run();
                }
            }
        });

        window.doModal();
    }

    public void authorize(String permission) throws NotAuthorizedException {
        if (getUser() == null) {
            throw new NotAuthorizedException();  //"Missing permission: " + permission);
        }
    }

    public User getUser() {
        return (User) Sessions.getCurrent().getAttribute(ZK_SESSION_USER_ATTRIBUTE);
    }

    private void setUser(String userId) {
        Sessions.getCurrent().setAttribute(ZK_SESSION_USER_ATTRIBUTE, new User() {
            public String getId() { return userId; }
        });
        EventQueues.lookup("q", Sessions.getCurrent(), true)
                   .publish(new Event("onLogin"));
    }
}
