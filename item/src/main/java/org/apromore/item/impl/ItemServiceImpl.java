package org.apromore.item.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apromore.item.Item;
import org.apromore.item.ItemFormatException;
import org.apromore.item.ItemService;
import org.apromore.item.NotAuthorizedException;
//import org.apromore.item.User;
import org.apromore.item.jpa.ItemDAO;
import org.apromore.item.jpa.ItemRepository;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
import org.apromore.ui.UIService;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//import static org.osgi.service.component.annotations.FieldOption.UPDATE;
//import static
//    org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE;
//import static org.osgi.service.component.annotations.ReferencePolicy.DYNAMIC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** {@inheritDoc}
 *
 * This implementation uses JPA persistence.
 * It manages the list of {@link ItemPlugin}s present in the system.
 */
//@Component(service = {ItemPluginContext.class, ItemService.class})
public final class ItemServiceImpl implements ItemPluginContext, ItemService {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(ItemServiceImpl.class);

    /** The dynamically populated list of item plugins. */
    //@Reference(bind = "onBind", cardinality = MULTIPLE, fieldOption = UPDATE,
    //    policy = DYNAMIC, unbind = "onUnbind", updated = "onUpdated")
    private List<ItemPlugin> itemPlugins;

    /** Service used to check the caller's credentials for item creation. */
    //@Reference
    private UIService uiService;

    /** Used to persist the item's fields. */
    //@Reference
    private ItemRepository itemRepository;

    /**
     * @param newItemPlugins  the dynamically populated list of item plugins
     * @param newUIService  used to check the caller's credentials for item
     *     creation
     */
    public ItemServiceImpl(final List<ItemPlugin> newItemPlugins,
                           final UIService newUIService) {
        this.itemPlugins   = newItemPlugins;
        this.uiService     = newUIService;
    }

    /** @param newRepository  used to persist the item's fields */
    public void setItemRepository(final ItemRepository newRepository) {
        this.itemRepository = newRepository;
    }


    // ItemPluginContext implementation

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Item create(final String type) throws NotAuthorizedException {

        uiService.authorize("create");
        //User user = uiService.getUser();

        ItemDAO dao = new ItemDAO();
        dao.setType(type);
        itemRepository.add(dao);
        return new ItemImpl(dao);

        //Item item = createNakedItem(type);

        //entityManager.getTransaction().begin();

        //PrincipalItemPermissionImpl permission =
        //    new PrincipalItemPermissionImpl();
        //permission.setItemId(item.getId());
        //permission.setPrincipalName(user.getId());
        //permission.setPrincipalClassname("dummy classname");
        //permission.setPermission("owner");
        //entityManager.persist(permission);

        //entityManager.getTransaction().commit();
    }


    // ItemService implementation

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Item create(final InputStream inputStream) throws IOException,
        ItemFormatException, NotAuthorizedException {

        LOGGER.debug("Attempting to create item using " + itemPlugins);

        // Materialize the inputStream into memory, since we may need to read it
        // several times
        byte[] buffer = inputStream.readAllBytes();
        inputStream.close();

        // Try each available ItemPlugin in turn in order to construct a
        // concrete subtype
        for (ItemPlugin itemPlugin: itemPlugins) {
            try {
                LOGGER.debug("Attempting to create " + itemPlugin.getType());
                Item item = itemPlugin.create(new ByteArrayInputStream(buffer));

                // TODO: notify observers

                return item;

            } catch (ItemFormatException e) {
                LOGGER.warn("Unable to parse " + itemPlugin.getType());
                continue;  // This wasn't the correct type, so skip to trying
                           // the next type
            }
        }

        // None of the ItemPlugins could interpret this input
        List<String> formatNames = itemPlugins
            .stream()
            .map(itemPlugin -> itemPlugin.getType())
            .collect(Collectors.toList());
        throw new ItemFormatException(formatNames);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Item> getAll() {
        return itemRepository
            .list()
            .stream()
            .map(itemDAO -> toConcreteSubtype(new ItemImpl(itemDAO)))
            .collect(Collectors.toList());
    }

    /**
     * @param item  any {@list Item} instance, not <code>null</code>
     * @return a subclass of {@list Item} corresponding to the <i>item</i>
     */
    private Item toConcreteSubtype(final Item item) {
        for (ItemPlugin itemPlugin: itemPlugins) {
            if (itemPlugin.getType().equals(item.getType())) {
                try {
                    return itemPlugin.toConcreteItem(item);

                } catch (ItemTypeException e) {
                    e.printStackTrace();
                    return item;
                }
            }
        }

        LOGGER.warn("Encountered item type " + item.getType()
            + " without a corresponding item plugin.");
        return item;
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Item getById(final Long id) {
        return toConcreteSubtype(new ItemImpl(itemRepository.get(id)));
    }


    /*
    // ItemPlugins reference list listener implementation

    public void onBind(ItemPlugin itemPlugin, Map properties) {
        LOGGER.info("Bind item plugin " + itemPlugin + " with properties " +
                    properties);
    }

    public void onUnbind(ItemPlugin itemPlugin, Map properties) {
        LOGGER.info("Unbind item plugin " + itemPlugin + " with properties " +
                    properties);
    }

    public void onUpdated(ItemPlugin itemPlugin, Map properties) {
        LOGGER.info("Updated item plugin " + itemPlugin + " with properties " +
                    properties);
    }
    */
}
