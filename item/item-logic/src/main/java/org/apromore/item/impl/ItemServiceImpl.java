package org.apromore.item.impl;

/*-
 * #%L
 * Apromore :: item
 * %%
 * Copyright (C) 2018 - 2019 The Apromore Initiative
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.google.common.io.ByteStreams;
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
import org.apromore.item.jpa.ItemDAO;
import org.apromore.item.jpa.ItemRepository;
import org.apromore.item.spi.ItemPlugin;
import org.apromore.item.spi.ItemPluginContext;
import org.apromore.item.spi.ItemTypeException;
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

    /** Used to persist the item's fields. */
    //@Reference
    private ItemRepository itemRepository;

    /**
     * @param newItemPlugins  the dynamically populated list of item plugins
     */
    public ItemServiceImpl(final List<ItemPlugin> newItemPlugins) {
        this.itemPlugins = newItemPlugins;
    }

    /** @param newRepository  used to persist the item's fields */
    public void setItemRepository(final ItemRepository newRepository) {
        this.itemRepository = newRepository;
    }


    // ItemPluginContext implementation

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Item create(final String type) throws NotAuthorizedException {

        /*
        try {
            userService.authorize("create");

        } catch (org.apromore.user.NotAuthorizedException e) {
            throw new NotAuthorizedException("Nope");
        }
        */
        //User user = userService.getUser();

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
        //byte[] buffer = inputStream.readAllBytes();
        byte[] buffer = ByteStreams.toByteArray(inputStream);  // JDK 1.8
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

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Item getById(final Long id) {
        return toConcreteSubtype(new ItemImpl(itemRepository.get(id)));
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void remove(final Item item) {
        itemRepository.remove(item.getId());
    }


    // Internal methods

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
