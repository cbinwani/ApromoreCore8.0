package org.apromore.folder.jpa.eclipselink;

/*-
 * #%L
 * Apromore :: folder-jpa-eclipselink
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

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import org.apromore.folder.jpa.FolderRepository;
import org.apromore.folder.jpa.PathDAO;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory service for {@link PathDAO}s.
 */
@Transactional
public final class FolderRepositoryImpl implements FolderRepository {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(FolderRepositoryImpl.class);

    /**
     * Entity manager for the <code>folder-eclipselink</code> persistence
     * unit.
     */
    @PersistenceContext(unitName = "folder-eclipselink")
    @SuppressWarnings("nullness")
    private EntityManager entityManager;

    /**
     * @param newEntityManager  entity manager for the
     *     <code>folder-eclipselink</code> persistence unit
     */
    public void setEntityManager(final EntityManager newEntityManager) {
        this.entityManager = newEntityManager;
    }

    // FolderRepository implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void addPath(final PathDAO dao) {
        entityManager.persist(dao);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    @Nullable
    public Long findItemIdByParentAndName(final @Nullable PathDAO parent,
                                          final String  name) {
        TypedQuery<Long> query;
        if (parent == null) {
            query = entityManager.createQuery(
                "SELECT i.itemId FROM PathDAO i"
                + " WHERE i.parent IS NULL AND i.name=:name",
                Long.class);
        } else {
            query = entityManager.createQuery(
                "SELECT i.itemId FROM PathDAO i"
                + " WHERE i.parent=:parent AND i.name=:name",
                Long.class);
            query.setParameter("parent", parent);
        }
        query.setParameter("name", name);

        Long itemId = null;
        try {
            itemId = query.getSingleResult();
        } catch (NoResultException e) {
            // nothing to do
        }
        return itemId;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    @Nullable
    public PathDAO findPathByItemId(final Long itemId) {
        TypedQuery<PathDAO> query = entityManager.createQuery(
            "SELECT i FROM PathDAO i WHERE i.itemId=:itemId",
            PathDAO.class);
        query.setParameter("itemId", itemId);
        PathDAO dao = null;
        try {
            dao = query.getSingleResult();
        } catch (NoResultException e) {
            // nothing to do
        }
        return dao;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<PathDAO> findPathsByParent(@Nullable final PathDAO parent) {
        TypedQuery<PathDAO> query;
        if (parent == null) {
            query = entityManager.createQuery(
                "SELECT i FROM PathDAO i WHERE i.parent IS NULL",
                PathDAO.class);
        } else {
            query = entityManager.createQuery(
                "SELECT i FROM PathDAO i WHERE i.parent=:parent",
                PathDAO.class);
            query.setParameter("parent", parent);
        }
        return query.getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    @Nullable
    public PathDAO findPath(final Long id) {
        //return entityManager.find(PathDAO.class, id);

        TypedQuery<PathDAO> query = entityManager.createQuery(
            "SELECT i FROM PathDAO i WHERE i.id=:id", PathDAO.class);
        query.setParameter("id", id);
        PathDAO dao = null;
        try {
            dao = query.getSingleResult();
        } catch (NoResultException e) {
            // nothing to do
        }
        return dao;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void removePath(final Long id) {
        PathDAO dao = findPath(id);
        if (dao == null) {
            throw new EntityNotFoundException(id + " is not a path");
        }
        entityManager.remove(dao);
    }
}
