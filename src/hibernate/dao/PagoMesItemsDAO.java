package hibernate.dao;

// default package

import hibernate.model.PagoMesItems;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * PagoMesItems entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .PagoMesItems
 * @author MyEclipse Persistence Tools
 */
public class PagoMesItemsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(PagoMesItemsDAO.class);
	// property constants
	public static final String DESCRIPCION = "descripcion";
	public static final String DIAS = "dias";
	public static final String HORAS = "horas";
	public static final String INGRESO = "ingreso";
	public static final String DEDUCCION = "deduccion";

	public void save(PagoMesItems transientInstance) {
		log.debug("saving PagoMesItems instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PagoMesItems persistentInstance) {
		log.debug("deleting PagoMesItems instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PagoMesItems findById(java.lang.Integer id) {
		log.debug("getting PagoMesItems instance with id: " + id);
		try {
			PagoMesItems instance = (PagoMesItems) getSession().get(
					"hibernate.model.PagoMesItems", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PagoMesItems instance) {
		log.debug("finding PagoMesItems instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.PagoMesItems")
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
		log.debug("finding PagoMesItems instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from PagoMesItems as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDescripcion(Object descripcion) {
		return findByProperty(DESCRIPCION, descripcion);
	}

	public List findByDias(Object dias) {
		return findByProperty(DIAS, dias);
	}

	public List findByHoras(Object horas) {
		return findByProperty(HORAS, horas);
	}

	public List findByIngreso(Object ingreso) {
		return findByProperty(INGRESO, ingreso);
	}

	public List findByDeduccion(Object deduccion) {
		return findByProperty(DEDUCCION, deduccion);
	}

	public List findAll() {
		log.debug("finding all PagoMesItems instances");
		try {
			String queryString = "from PagoMesItems";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PagoMesItems merge(PagoMesItems detachedInstance) {
		log.debug("merging PagoMesItems instance");
		try {
			PagoMesItems result = (PagoMesItems) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PagoMesItems instance) {
		log.debug("attaching dirty PagoMesItems instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PagoMesItems instance) {
		log.debug("attaching clean PagoMesItems instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}