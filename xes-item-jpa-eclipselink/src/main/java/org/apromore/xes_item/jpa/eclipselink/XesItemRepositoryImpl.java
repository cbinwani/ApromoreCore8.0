/*














 */
package org.apromore.xes_item.jpa.eclipselink;

import org.apromore.xes_item.jpa.XesItemDao;
import org.apromore.xes_item.jpa.XesItemRepository;
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
public class XesItemRepositoryImpl implements XesItemRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(XesItemRepositoryImpl.class);

    @PersistenceContext(unitName = "xes-item-eclipselink")
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    

    // XesItemRepository implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void add(XesItemDao dao) {
        entityManager.persist(dao);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<XesItemDao> list() {
        return entityManager.createQuery("SELECT i FROM XesItemDao i", XesItemDao.class).getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public XesItemDao get(Long id) {
        //return entityManager.find(XesItemDao.class, id);

        TypedQuery<XesItemDao> query = entityManager.createQuery("SELECT i FROM XesItemDao i WHERE i.id=:id", XesItemDao.class);
        query.setParameter("id", id);
        XesItemDao dao = null;
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
        XesItemDao dao = get(id);
        entityManager.remove(dao);
    }
}
