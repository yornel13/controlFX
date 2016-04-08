package hibernate.dao;

import hibernate.model.DetallesEmpleado;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * DetallesEmpleado entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see hibernate.model.DetallesEmpleado
 * @author MyEclipse Persistence Tools
 */
public class DetallesEmpleadoDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(DetallesEmpleadoDAO.class);
	// property constants
	public static final String SUELDO = "sueldo";
	public static final String NRO_CUENTA = "nroCuenta";
	public static final String EXTRA = "extra";

	public void save(DetallesEmpleado transientInstance) {
		log.debug("saving DetallesEmpleado instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DetallesEmpleado persistentInstance) {
		log.debug("deleting DetallesEmpleado instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetallesEmpleado findById(java.lang.Integer id) {
		log.debug("getting DetallesEmpleado instance with id: " + id);
		try {
			DetallesEmpleado instance = (DetallesEmpleado) getSession().get(
					"hibernate.model.DetallesEmpleado", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(DetallesEmpleado instance) {
		log.debug("finding DetallesEmpleado instance by example");
		try {
			List results = getSession()
					.createCriteria("hibernate.model.DetallesEmpleado")
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
		log.debug("finding DetallesEmpleado instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from DetallesEmpleado as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySueldo(Object sueldo) {
		return findByProperty(SUELDO, sueldo);
	}

	public List findByNroCuenta(Object nroCuenta) {
		return findByProperty(NRO_CUENTA, nroCuenta);
	}

	public List findByExtra(Object extra) {
		return findByProperty(EXTRA, extra);
	}

	public List findAll() {
		log.debug("finding all DetallesEmpleado instances");
		try {
			String queryString = "from DetallesEmpleado";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DetallesEmpleado merge(DetallesEmpleado detachedInstance) {
		log.debug("merging DetallesEmpleado instance");
		try {
			DetallesEmpleado result = (DetallesEmpleado) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DetallesEmpleado instance) {
		log.debug("attaching dirty DetallesEmpleado instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetallesEmpleado instance) {
		log.debug("attaching clean DetallesEmpleado instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}