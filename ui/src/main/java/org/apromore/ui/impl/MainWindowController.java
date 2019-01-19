package org.apromore.ui.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apromore.item.Item;
import org.apromore.item.User;
import org.apromore.ui.spi.UIPlugin;
import org.apromore.ui.spi.UIPluginContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.lang.Library;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
//import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class MainWindowController extends SelectorComposer<Component>
    implements EventListener<Event> {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MainWindowController.class);

    @Wire
    public Menubar menubar;

    @Wire("#parent")
    public Component parent;

    @Wire("#status")
    public Label status;

    /**
     * Keeps track of the current selection provided to {@link UIPlugin}s via
     * {@link UIPluginContext#getSelection}.
     *
     * Initially empty.
     */
    private final Set<Item> selection = new HashSet<>();

    @Override
    public void doAfterCompose(Component component) throws Exception {
        super.doAfterCompose(component);

        Library.setProperty("org.zkoss.theme.preferred",
            (String) blueprintContainer().getComponentInstance("uiTheme"));

        // Listen for changes to the UIPlugin list
        UIPluginListener uiPluginListener = (UIPluginListener)
            blueprintContainer().getComponentInstance("uiPluginListener");
        uiPluginListener.addListener(component);
        EventQueues.lookup("q", component.getDesktop().getSession(), true)
                   .subscribe(this);

        // Replace the menubar in the ZUL with a dynamically-generated one based
        // on the UIPlugin list
        generateMenubar(menubar, parent, selection, getUIPluginContext());
    }

    private BlueprintContainer blueprintContainer() {
        BundleContext bundleContext = (BundleContext) this
            .getSelf()
            .getDesktop()
            .getWebApp()
            .getServletContext()
            .getAttribute("osgi-bundlecontext");
        for (ServiceReference serviceReference
            : bundleContext.getBundle().getRegisteredServices()) {

            Object service = bundleContext.getService(serviceReference);
            if (service instanceof BlueprintContainer) {
                return (BlueprintContainer) service;
            }
        }

        throw new Error("No blueprint context");
    }

    public void onEvent(Event event) {
        switch (event.getName()) {
        case "onBind":    // new UIPlugin appeared
        case "onLogin":   // User property changed
        case "onUnbind":  // existing UIPlugin went away
            generateMenubar(menubar, parent, selection, getUIPluginContext());
            break;
        default:
            LOGGER.warn("Main window ignoring unrecognized event: "
                + event.getName());
        }
    }

    private UIPluginContext getUIPluginContext() {

        return new UIPluginContext() {

            public Component createComponent(ClassLoader bundleClassLoader,
                                             String      uri,
                                             Map<?, ?>   arguments) {
                try {
                    InputStream in = bundleClassLoader.getResourceAsStream(uri);
                    if (in == null) {
                        throw new IOException("No resource " + uri
                            + " found in bundle classpath");
                    }

                    return Executions.createComponentsDirectly(
                        new InputStreamReader(in, "UTF-8"),
                        "zul",
                        parent,
                        arguments);

                } catch (IOException e) {
                    throw new Error("ZUL resource " + uri
                        + " could not be created as as ZK component", e);
                }
            }

            public Component getParentComponent() {
                return parent;
            }

            public void setComponent(Component component) {
                parent.getChildren().clear();
                parent.getChildren().add(component);
            }

            public Set<Item> getSelection() {
                return selection;  // TODO: wrap for immutability
            }

            public void setSelection(Set<Item> newSelection) {
                LOGGER.debug("Setting selection to " + newSelection);
                selection.clear();
                selection.addAll(newSelection);
                generateMenubar(menubar, parent, selection,
                    getUIPluginContext());
            }

            public User getUser() {
                return (User) Sessions.getCurrent(
                   ).getAttribute(UIServiceImpl.ZK_SESSION_USER_ATTRIBUTE);
            }

            public void setUser(User newUser) {
                LOGGER.debug(newUser == null
                    ? "User logged out"
                    : "User logged in: " + newUser.getId());
                Sessions.getCurrent()
                        .setAttribute(UIServiceImpl.ZK_SESSION_USER_ATTRIBUTE,
                                      newUser);
                generateMenubar(menubar, parent, selection,
                    getUIPluginContext());
            }
        };
    }

    private void generateMenubar(Menubar newMenubar, Component newParent,
        Set<Item> newSelection, UIPluginContext uiPluginContext) {

        // If present, this comparator expresses the preferred ordering for
        // menus along the the menu bar
        Comparator<String> ordering = new ExplicitComparator(
            (String) blueprintContainer().getComponentInstance("uiMenuOrder")
        );

        List<UIPlugin> uiPlugins = (List<UIPlugin>) blueprintContainer()
            .getComponentInstance("uiPlugins");
        LOGGER.debug("uiPlugins = " + uiPlugins);

        SortedMap<String, Menu> menuMap = new TreeMap<>(ordering);

        for (final UIPlugin plugin: uiPlugins) {
            String menuName = plugin.getGroupLabel();

            // Create a new menu if this is the first menu item within it
            if (!menuMap.containsKey(menuName)) {
                Menu menu = new Menu(menuName);
                menu.appendChild(new Menupopup());
                menuMap.put(menuName, menu);
            }
            assert menuMap.containsKey(menuName);

            // Create the menu item
            Menu menu = menuMap.get(menuName);
            Menuitem menuitem = new Menuitem();
            menuitem.setImageContent(plugin.getIcon());
            menuitem.setLabel(plugin.getLabel());
            menuitem.setDisabled(!plugin.isEnabled(uiPluginContext));

            // Insert the menu item alphabetically into the menu
            Menuitem precedingMenuitem = null;
            List<Menuitem> existingMenuitems =
                menu.getMenupopup().getChildren();
            for (Menuitem existingMenuitem: existingMenuitems) {
                if (menuitem.getLabel()
                            .compareTo(existingMenuitem.getLabel()) <= 0) {
                    precedingMenuitem = existingMenuitem;
                    break;
                }
            }
            menu.getMenupopup().insertBefore(menuitem, precedingMenuitem);

            menuitem.addEventListener("onClick", new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    plugin.execute(uiPluginContext);
                }
            });
        }

        // Replace the existing menubar contents
        newMenubar.getChildren().clear();
        newMenubar.getChildren().addAll(menuMap.values());
    }
}
