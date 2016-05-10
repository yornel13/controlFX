package hibernate.dao;

import hibernate.model.DeudaTipo;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * DeudaTipo entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see hibernate.model.DeudaTipo
 * @author MyEclipse Persistence Tools
 */
public class DeudaTipoDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(DeudaTipoDAO.class);
	// property constants
	public static final String NOMBRE = "nombre";
	public static final String CUOTAS = "cuotas";
	public static final String ACTIVO = "activo";

	public void save(DeudaTipo transientInstance) {
		log.debug("saving DeudaTipo instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DeudaTipo persistentInstance) {
		log.debug("deleting DeudaTipo instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DeudaTipo findById(java.lang.Integer id) {
		log.debug("getting DeudaTipo instance with id: " + id);
		try {
			DeudaTipo instance = (DeudaTipo) getSession().get(
					"hibernate.model.DeudaTipo", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(DeudaTipo instance) {
		log.debug("finding DeudaTipo instance by example");
		try {
			List results = getSession()
					.createCriteria("hibernate.model.DeudaTipo")
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
		log.debug("finding DeudaTipo instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from DeudaTipo as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNombre(Object nombre) {
		return findByProperty(NOMBRE, nombre);
	}

	public List findByCuotas(Object cuotas) {
		return findByProperty(CUOTAS, cuotas);
	}

	public List findByActivo(Object activo) {
		return findByProperty(ACTIVO, activo);
	}

	public List findAll() {
		log.debug("finding all DeudaTipo instances");
		try {
			String queryString = "from DeudaTipo";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DeudaTipo merge(DeudaTipo detachedInstance) {
		log.debug("merging DeudaTipo instance");
		try {
			DeudaTipo result = (DeudaTipo) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DeudaTipo instance) {
		log.debug("attaching dirty DeudaTipo instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DeudaTipo instance) {
		log.debug("attaching clean DeudaTipo instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}