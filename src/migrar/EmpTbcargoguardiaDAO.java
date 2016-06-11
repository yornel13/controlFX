package migrar;

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * EmpTbcargoguardia entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .EmpTbcargoguardia
 * @author MyEclipse Persistence Tools
 */
public class EmpTbcargoguardiaDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(EmpTbcargoguardiaDAO.class);
	// property constants
	public static final String NOMB_CARGOGUARDIA = "nombCargoguardia";
	public static final String STAT_CARGOGUARDIA = "statCargoguardia";

	public void save(EmpTbcargoguardia transientInstance) {
		log.debug("saving EmpTbcargoguardia instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EmpTbcargoguardia persistentInstance) {
		log.debug("deleting EmpTbcargoguardia instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EmpTbcargoguardia findById(java.lang.Integer id) {
		log.debug("getting EmpTbcargoguardia instance with id: " + id);
		try {
			EmpTbcargoguardia instance = (EmpTbcargoguardia) getSession().get(
					"EmpTbcargoguardia", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EmpTbcargoguardia instance) {
		log.debug("finding EmpTbcargoguardia instance by example");
		try {
			List results = getSession().createCriteria("EmpTbcargoguardia")
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
		log.debug("finding EmpTbcargoguardia instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EmpTbcargoguardia as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNombCargoguardia(Object nombCargoguardia) {
		return findByProperty(NOMB_CARGOGUARDIA, nombCargoguardia);
	}

	public List findByStatCargoguardia(Object statCargoguardia) {
		return findByProperty(STAT_CARGOGUARDIA, statCargoguardia);
	}

	public List findAll() {
		log.debug("finding all EmpTbcargoguardia instances");
		try {
			String queryString = "from EmpTbcargoguardia";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EmpTbcargoguardia merge(EmpTbcargoguardia detachedInstance) {
		log.debug("merging EmpTbcargoguardia instance");
		try {
			EmpTbcargoguardia result = (EmpTbcargoguardia) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EmpTbcargoguardia instance) {
		log.debug("attaching dirty EmpTbcargoguardia instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EmpTbcargoguardia instance) {
		log.debug("attaching clean EmpTbcargoguardia instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}