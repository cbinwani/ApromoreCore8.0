package org.apromore.xes_item.jpa.eclipselink;

import org.apromore.xes_item.jpa.XESItemDAO;
import org.apromore.xes_item.jpa.XESItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Factory service for {@link XESItemDAO}s.
 */
@Transactional
public final class XESItemRepositoryImpl implements XESItemRepository {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(XESItemRepositoryImpl.class);

    @PersistenceContext(unitName = "xes-item-eclipselink")
    private EntityManager entityManager;

    public void setEntityManager(final EntityManager newEntityManager) {
        this.entityManager = newEntityManager;
    }


    // XESItemRepository implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void add(final XESItemDAO dao) {
        entityManager.persist(dao);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<XESItemDAO> list() {
        return entityManager.createQuery("SELECT i FROM XESItemDAO i",
            XESItemDAO.class).getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public XESItemDAO get(final Long id) {
        //return entityManager.find(XESItemDAO.class, id);

        TypedQuery<XESItemDAO> query = entityManager.createQuery(
            "SELECT i FROM XESItemDAO i WHERE i.id=:id", XESItemDAO.class);
        query.setParameter("id", id);
        XESItemDAO dao = null;
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
        XESItemDAO dao = get(id);
        entityManager.remove(dao);
    }
}
