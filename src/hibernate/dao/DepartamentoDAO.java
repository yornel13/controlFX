package hibernate.dao;

// default package

import hibernate.model.Departamento;
import java.util.List;
import java.util.Set;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Departamento entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Departamento
 * @author MyEclipse Persistence Tools
 */
public class DepartamentoDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(DepartamentoDAO.class);
	// property constants
	public static final String NOMBRE = "nombre";
	public static final String ACTIVO = "activo";

	public void save(Departamento transientInstance) {
		log.debug("saving Departamento instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Departamento persistentInstance) {
		log.debug("deleting Departamento instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Departamento findById(java.lang.Integer id) {
		log.debug("getting Departamento instance with id: " + id);
		try {
			Departamento instance = (Departamento) getSession().get(
					"hibernate.model.Departamento", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Departamento instance) {
		log.debug("finding Departamento instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Departamento")
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
		log.debug("finding Departamento instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Departamento as model where model."
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
		log.debug("finding all Departamento instances");
		try {
			String queryString = "from Departamento";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Departamento merge(Departamento detachedInstance) {
		log.debug("merging Departamento instance");
		try {
			Departamento result = (Departamento) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Departamento instance) {
		log.debug("attaching dirty Departamento instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Departamento instance) {
		log.debug("attaching clean Departamento instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}