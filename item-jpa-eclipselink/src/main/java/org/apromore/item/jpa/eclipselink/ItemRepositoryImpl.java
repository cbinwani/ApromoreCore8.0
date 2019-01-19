package org.apromore.item.jpa.eclipselink;

import org.apromore.item.jpa.ItemDao;
import org.apromore.item.jpa.ItemRepository;
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
public class ItemRepositoryImpl implements ItemRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    @PersistenceContext(unitName = "item-eclipselink")
    private EntityManager entityManager;

    public void setEntityManager(EntityManager newEntityManager) {
        this.entityManager = newEntityManager;
    }


    // ItemDaoService implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void add(final ItemDao newItemDao) {
        LOGGER.debug("EclipseLink provider adding item with type " + newItemDao.getType() + "; entity manager is " + entityManager);
        entityManager.persist(newItemDao);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<ItemDao> list() {
        LOGGER.debug("EclipseLink provider list all items; entity manager is " + entityManager);
        return entityManager.createQuery("SELECT i FROM ItemDao i", ItemDao.class).getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public ItemDao get(final Long id) {
        LOGGER.debug("EclipseLink provider getting item with id " + id + "; entity manager is " + entityManager);
        //return entityManager.find(ItemDao.class, id);

        TypedQuery<ItemDao> query = entityManager.createQuery("SELECT i FROM ItemDao i WHERE i.id=:id", ItemDao.class);
        query.setParameter("id", id);
        ItemDao dao = null;
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
        ItemDao dao = get(id);
        entityManager.remove(dao);
    }
}
