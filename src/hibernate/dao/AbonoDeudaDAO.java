package hibernate.dao;

// default package

import hibernate.model.AbonoDeuda;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * AbonoDeuda entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .AbonoDeuda
 * @author MyEclipse Persistence Tools
 */
public class AbonoDeudaDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(AbonoDeudaDAO.class);
	// property constants
	public static final String MONTO = "monto";

	public void save(AbonoDeuda transientInstance) {
		log.debug("saving AbonoDeuda instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(AbonoDeuda persistentInstance) {
		log.debug("deleting AbonoDeuda instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AbonoDeuda findById(java.lang.Integer id) {
		log.debug("getting AbonoDeuda instance with id: " + id);
		try {
			AbonoDeuda instance = (AbonoDeuda) getSession().get("hibernate.model.AbonoDeuda",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<AbonoDeuda> findAllByPagoId(int pagoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM abono_deuda where pago_id = :pago_id")
                    .addEntity(AbonoDeuda.class)
                    .setParameter("pago_id", pagoId);
            Object result = query.list();
            return (List<AbonoDeuda>) result;
        }
        
        public List<AbonoDeuda> findAllByDeudaId(int deudaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM abono_deuda where deuda_id = :deuda_id")
                    .addEntity(AbonoDeuda.class)
                    .setParameter("deuda_id", deudaId);
            Object result = query.list();
            return (List<AbonoDeuda>) result;
        }

	public List findByExample(AbonoDeuda instance) {
		log.debug("finding AbonoDeuda instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.AbonoDeuda")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding AbonoDeuda instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from AbonoDeuda as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByMonto(Object monto) {
		return findByProperty(MONTO, monto);
	}

	public List findAll() {
		log.debug("finding all AbonoDeuda instances");
		try {
			String queryString = "from AbonoDeuda";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public AbonoDeuda merge(AbonoDeuda detachedInstance) {
		log.debug("merging AbonoDeuda instance");
		try {
			AbonoDeuda result = (AbonoDeuda) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(AbonoDeuda instance) {
		log.debug("attaching dirty AbonoDeuda instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AbonoDeuda instance) {
		log.debug("attaching clean AbonoDeuda instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}