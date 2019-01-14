/*














 */
package org.apromore.bpmn_item.jpa.eclipselink;

import org.apromore.bpmn_item.jpa.BpmnItemDao;
import org.apromore.bpmn_item.jpa.BpmnItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**

 */
@Transactional
public class BpmnItemRepositoryImpl implements BpmnItemRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BpmnItemRepositoryImpl.class);

    @PersistenceContext(unitName = "bpmn-item-eclipselink")
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    

    // BpmnItemRepository implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void add(BpmnItemDao dao) {
        entityManager.persist(dao);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<BpmnItemDao> list() {
        return entityManager.createQuery("SELECT i FROM BpmnItemDao i", BpmnItemDao.class).getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public BpmnItemDao get(Long id) {
        //return entityManager.find(BpmnItemDao.class, id);

        TypedQuery<BpmnItemDao> query = entityManager.createQuery("SELECT i FROM BpmnItemDao i WHERE i.id=:id", BpmnItemDao.class);
        query.setParameter("id", id);
        BpmnItemDao dao = null;
        try {
            dao = query.getSingleResult();
        } catch (NoResultException e) {
            // nothing to do
        }
        return dao;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void remove(Long id) {
        BpmnItemDao dao = get(id);
        entityManager.remove(dao);
    }
}
