package hibernate.dao;

// default package

import hibernate.model.RegistroAcciones;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * RegistroAcciones entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .RegistroAcciones
 * @author MyEclipse Persistence Tools
 */
public class RegistroAccionesDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(RegistroAccionesDAO.class);
	// property constants
	public static final String DETALLES = "detalles";

	public void save(RegistroAcciones transientInstance) {
		log.debug("saving RegistroAcciones instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RegistroAcciones persistentInstance) {
		log.debug("deleting RegistroAcciones instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RegistroAcciones findById(java.lang.Integer id) {
		log.debug("getting RegistroAcciones instance with id: " + id);
		try {
			RegistroAcciones instance = (RegistroAcciones) getSession().get(
					"hibernate.model.RegistroAcciones", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RegistroAcciones instance) {
		log.debug("finding RegistroAcciones instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.RegistroAcciones")
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
		log.debug("finding RegistroAcciones instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RegistroAcciones as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDetalles(Object detalles) {
		return findByProperty(DETALLES, detalles);
	}

	public List findAll() {
		log.debug("finding all RegistroAcciones instances");
		try {
			String queryString = "from RegistroAcciones";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RegistroAcciones merge(RegistroAcciones detachedInstance) {
		log.debug("merging RegistroAcciones instance");
		try {
			RegistroAcciones result = (RegistroAcciones) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RegistroAcciones instance) {
		log.debug("attaching dirty RegistroAcciones instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RegistroAcciones instance) {
		log.debug("attaching clean RegistroAcciones instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}