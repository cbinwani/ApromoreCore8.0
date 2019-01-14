package org.apromore.item_ui;

import java.security.Principal;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apromore.item.User;
import org.apromore.ui.UIService;
import org.apromore.ui.spi.AbstractUIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class LoginAccountUIPlugin extends AbstractUIPlugin {

    private final UIService uiService;

    public LoginAccountUIPlugin(UIService uiService) {
        this.uiService = uiService;

        this.groupLabel = "Account";
        this.label = "Login";
    }


    // Implementation overriding AbstractUIPlugin

    /** @return whether the plugin is applicable to the given selection */
    @Override
    public boolean isEnabled(UIPluginContext context) {
        return context.getUser() == null;
    }

    /** Invoked when the menu item is selected */
    @Override
    public void execute(UIPluginContext context) {
        uiService.authenticate(null, null, null);
    }
}
