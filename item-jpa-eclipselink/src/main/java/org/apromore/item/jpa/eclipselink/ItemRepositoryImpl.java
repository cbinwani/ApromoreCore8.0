package org.apromore.item.jpa.eclipselink;

import org.apromore.item.jpa.ItemDAO;
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
 * Factory service for {@link ItemDAO}s.
 */
@Transactional
public class ItemRepositoryImpl implements ItemRepository {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(ItemRepositoryImpl.class);

    @PersistenceContext(unitName = "item-eclipselink")
    private EntityManager entityManager;

    public void setEntityManager(final EntityManager newEntityManager) {
        this.entityManager = newEntityManager;
    }


    // ItemRepository implementation

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void add(final ItemDAO newItemDAO) {
        LOGGER.debug("EclipseLink provider adding item with type "
            + newItemDAO.getType() + "; entity manager is " + entityManager);
        entityManager.persist(newItemDAO);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<ItemDAO> list() {
        LOGGER.debug("EclipseLink provider list all items; entity manager is "
            + entityManager);
        return entityManager
            .createQuery("SELECT i FROM ItemDAO i", ItemDAO.class)
            .getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public ItemDAO get(final Long id) {
        LOGGER.debug("EclipseLink provider getting item with id " + id
            + "; entity manager is " + entityManager);
        //return entityManager.find(ItemDAO.class, id);

        TypedQuery<ItemDAO> query = entityManager.createQuery(
            "SELECT i FROM ItemDAO i WHERE i.id=:id", ItemDAO.class);
        query.setParameter("id", id);
        ItemDAO dao = null;
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
        ItemDAO dao = get(id);
        entityManager.remove(dao);
    }
}
