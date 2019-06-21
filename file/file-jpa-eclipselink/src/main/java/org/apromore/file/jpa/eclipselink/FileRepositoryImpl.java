package org.apromore.file.jpa.eclipselink;

/*-
 * #%L
 * Apromore :: file :: file-jpa-eclipselink
 * %%
 * Copyright (C) 2019 The Apromore Initiative
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

import org.apromore.file.jpa.FileDAO;
import org.apromore.file.jpa.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Factory service for {@link FileDAO}s.
 */
@Transactional
public final class FileRepositoryImpl implements FileRepository {

    /** Logger.  Named after the class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(FileRepositoryImpl.class);

    /**
     * Entity manager for the <code>file-eclipselink</code> persistence
     * unit.
     */
    @PersistenceContext(unitName = "file-eclipselink")
    private EntityManager entityManager;

    /**
     * @param newEntityManager  entity manager for the
     *     <code>bpmn-item-eclipselink</code> persistence unit
     */
    public void setEntityManager(final EntityManager newEntityManager) {
        this.entityManager = newEntityManager;
    }


    // FileRepository implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void add(final FileDAO dao) {
        entityManager.persist(dao);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<FileDAO> list() {
        return entityManager.createQuery("SELECT i FROM FileDAO i",
            FileDAO.class).getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public FileDAO get(final Long id) {
        //return entityManager.find(FileDAO.class, id);

        TypedQuery<FileDAO> query = entityManager.createQuery(
            "SELECT i FROM FileDAO i WHERE i.id=:id", FileDAO.class);
        query.setParameter("id", id);
        FileDAO dao = null;
        try {
            dao = query.getSingleResult();
        } catch (NoResultException e) {
            // nothing to do
        }
        return dao;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void remove(final Long id) {
        FileDAO dao = get(id);
        entityManager.remove(dao);
    }
}
