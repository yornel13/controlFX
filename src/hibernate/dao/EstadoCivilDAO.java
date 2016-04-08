package hibernate.dao;

import hibernate.model.EstadoCivil;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * EstadoCivil entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see hibernate.model.EstadoCivil
 * @author MyEclipse Persistence Tools
 */
public class EstadoCivilDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(EstadoCivilDAO.class);
	// property constants
	public static final String NOMBRE = "nombre";
	public static final String ACTIVO = "activo";

	public void save(EstadoCivil transientInstance) {
		log.debug("saving EstadoCivil instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EstadoCivil persistentInstance) {
		log.debug("deleting EstadoCivil instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EstadoCivil findById(java.lang.Integer id) {
		log.debug("getting EstadoCivil instance with id: " + id);
		try {
			EstadoCivil instance = (EstadoCivil) getSession().get(
					"hibernate.model.EstadoCivil", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EstadoCivil instance) {
		log.debug("finding EstadoCivil instance by example");
		try {
			List results = getSession()
					.createCriteria("hibernate.model.EstadoCivil")
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
		log.debug("finding EstadoCivil instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from EstadoCivil as model where model."
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

	public List findByActivo(Object activo) {
		return findByProperty(ACTIVO, activo);
	}

	public List findAll() {
		log.debug("finding all EstadoCivil instances");
		try {
			String queryString = "from EstadoCivil";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EstadoCivil merge(EstadoCivil detachedInstance) {
		log.debug("merging EstadoCivil instance");
		try {
			EstadoCivil result = (EstadoCivil) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EstadoCivil instance) {
		log.debug("attaching dirty EstadoCivil instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EstadoCivil instance) {
		log.debug("attaching clean EstadoCivil instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}