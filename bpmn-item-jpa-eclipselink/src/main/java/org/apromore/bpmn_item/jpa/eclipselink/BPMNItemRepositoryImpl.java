package org.apromore.bpmn_item.jpa.eclipselink;

import org.apromore.bpmn_item.jpa.BPMNItemDAO;
import org.apromore.bpmn_item.jpa.BPMNItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Factory service for {@link BPMNItemDAO}s.
 */
@Transactional
public class BPMNItemRepositoryImpl implements BPMNItemRepository {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(BPMNItemRepositoryImpl.class);

    @PersistenceContext(unitName = "bpmn-item-eclipselink")
    private EntityManager entityManager;

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
    public void remove(final Long id) {
        BPMNItemDAO dao = get(id);
        entityManager.remove(dao);
    }
}
