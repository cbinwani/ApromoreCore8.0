package org.apromore.folder.jpa.eclipselink;

import org.apromore.folder.jpa.FolderDAO;
import org.apromore.folder.jpa.FolderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Factory service for {@link FolderDAO}s.
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
    public void add(final FolderDAO dao) {
        entityManager.persist(dao);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<FolderDAO> findByParentId(final Long parentId) {
        return entityManager.createQuery(
            "SELECT i FROM FolderDAO i WHERE i.parent.id=:parentId",
            FolderDAO.class).getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public FolderDAO get(final Long id) {
        //return entityManager.find(FolderDAO.class, id);

        TypedQuery<FolderDAO> query = entityManager.createQuery(
            "SELECT i FROM FolderDAO i WHERE i.id=:id", FolderDAO.class);
        query.setParameter("id", id);
        FolderDAO dao = null;
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
        FolderDAO dao = get(id);
        entityManager.remove(dao);
    }
}
