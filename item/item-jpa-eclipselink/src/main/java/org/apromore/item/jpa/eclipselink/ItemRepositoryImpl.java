package org.apromore.item.jpa.eclipselink;

/*-
 * #%L
 * Apromore :: item-jpa-eclipselink
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

import org.apromore.item.jpa.ItemDAO;
import org.apromore.item.jpa.ItemRepository;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

/**
 * Factory service for {@link ItemDAO}s.
 */
@Transactional
public final class ItemRepositoryImpl implements ItemRepository {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(ItemRepositoryImpl.class);

    /**
     * Entity manager for the <code>"item-eclipselink"</code> persistence
     * unit.
     */
    @PersistenceContext(unitName = "item-eclipselink")
    @SuppressWarnings("nullness")
    private EntityManager entityManager;

    /**
     * @param newEntityManager entity manager for the
     *     <code>"item-eclipselink"</code> persistence unit.
     */
    public void setEntityManager(final EntityManager newEntityManager) {
        this.entityManager = newEntityManager;
    }


    // ItemRepository implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void add(final ItemDAO newItemDAO) {
        LOGGER.debug("EclipseLink provider adding item with type "
            + newItemDAO.getType() + "; entity manager is " + entityManager);
        entityManager.persist(newItemDAO);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<ItemDAO> list() {
        LOGGER.debug("EclipseLink provider list all items; entity manager is "
            + entityManager);
        return entityManager
            .createQuery("SELECT i FROM ItemDAO i", ItemDAO.class)
            .getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    @Nullable
    public ItemDAO get(final Long id) {
        LOGGER.debug("EclipseLink provider getting item with id " + id
            + "; entity manager is " + entityManager);
        //return entityManager.find(ItemDAO.class, id);

        TypedQuery<ItemDAO> query = entityManager.createQuery(
            "SELECT i FROM ItemDAO i WHERE i.id=:id", ItemDAO.class);
        query.setParameter("id", id);
        ItemDAO dao = null;
        try {
            dao = query.getSingleResult();
        } catch (NoResultException e) {
            // nothing to do
        }
        return dao;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void remove(final Long id) throws EntityNotFoundException {
        ItemDAO dao = get(id);
        if (dao == null) {
            throw new EntityNotFoundException("No item with id " + id);
        }
        entityManager.remove(dao);
    }
}
