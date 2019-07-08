package org.apromore.bpmn_item.jpa.eclipselink;

/*-
 * #%L
 * Apromore :: bpmn-item-jpa-eclipselink
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

import org.apromore.bpmn_item.jpa.BPMNItemDAO;
import org.apromore.bpmn_item.jpa.BPMNItemRepository;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Factory service for {@link BPMNItemDAO}s.
 */
@Transactional
public final class BPMNItemRepositoryImpl implements BPMNItemRepository {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(BPMNItemRepositoryImpl.class);

    /**
     * Entity manager for the <code>bpmn-item-eclipselink</code> persistence
     * unit.
     */
    @PersistenceContext(unitName = "bpmn-item-eclipselink")
    @SuppressWarnings("nullness")
    private EntityManager entityManager;

    /**
     * @param newEntityManager  entity manager for the
     *     <code>bpmn-item-eclipselink</code> persistence unit
     */
    public void setEntityManager(final EntityManager newEntityManager) {
        this.entityManager = newEntityManager;
    }


    // BpmnItemRepository implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void add(final BPMNItemDAO dao) {
        entityManager.persist(dao);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<BPMNItemDAO> list() {
        return entityManager.createQuery("SELECT i FROM BPMNItemDAO i",
            BPMNItemDAO.class).getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    @Nullable
    public BPMNItemDAO get(final Long id) {
        //return entityManager.find(BPMNItemDAO.class, id);

        TypedQuery<BPMNItemDAO> query = entityManager.createQuery(
            "SELECT i FROM BPMNItemDAO i WHERE i.id=:id", BPMNItemDAO.class);
        query.setParameter("id", id);
        BPMNItemDAO dao = null;
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
        BPMNItemDAO dao = get(id);
        if (dao == null) {
            throw new EntityNotFoundException("No BPMN item with id " + id);
        }
        entityManager.remove(dao);
    }
}
