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
import org.apromore.item.jpa.ItemDao;
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

//@Component(service = {ItemPluginContext.class, ItemService.class})
public class ItemServiceImpl implements ItemPluginContext, ItemService {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(ItemServiceImpl.class);

    //@Reference(bind = "onBind", cardinality = MULTIPLE, fieldOption = UPDATE,
    //    policy = DYNAMIC, unbind = "onUnbind", updated = "onUpdated")
    private List<ItemPlugin> itemPlugins;

    //@Reference
    private UIService        uiService;

    //@Reference
    private ItemRepository   itemRepository;

    public ItemServiceImpl(final List<ItemPlugin> newItemPlugins,
                           final UIService newUIService) {
        this.itemPlugins   = newItemPlugins;
        this.uiService     = newUIService;
    }

    public void setItemRepository(final ItemRepository newRepository) {
        this.itemRepository = newRepository;
    }


    // ItemPluginContext implementation

    @Transactional(Transactional.TxType.REQUIRED)
    public Item create(final String type) throws NotAuthorizedException {

        uiService.authorize("create");
        //User user = uiService.getUser();

        ItemDao dao = new ItemDao();
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

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Item> getAll() {
        return itemRepository
            .list()
            .stream()
            .map(itemDao -> toConcreteSubtype(new ItemImpl(itemDao)))
            .collect(Collectors.toList());
    }

    public Item toConcreteSubtype(final Item item) {
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
