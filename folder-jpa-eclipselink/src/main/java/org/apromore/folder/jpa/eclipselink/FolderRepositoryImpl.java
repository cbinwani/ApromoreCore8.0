package org.apromore.folder.jpa.eclipselink;

import org.apromore.folder.jpa.FolderRepository;
import org.apromore.folder.jpa.PathDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

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
    public Long findItemIdByPath(final String path) {
        TypedQuery<Long> query;
        if (true) {
            query = entityManager.createQuery(
                "SELECT i.itemId FROM PathDAO i"
                + " WHERE i.parent IS NULL AND i.name=:name",
                Long.class);
        } else {
            query = entityManager.createQuery(
                "SELECT i.itemId FROM PathDAO i"
                + " WHERE i.parent=:parent AND i.name=:name",
                Long.class);
            query.setParameter("parent", null);
        }
        query.setParameter("name", path);

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
    public List<PathDAO> findPathsByParent(final PathDAO parent) {
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
        entityManager.remove(dao);
    }
}
