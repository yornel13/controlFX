package migrar;

// default package

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * WebTblCliente entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .WebTblCliente
 * @author MyEclipse Persistence Tools
 */
public class WebTblClienteDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(WebTblClienteDAO.class);
	// property constants
	public static final String RAZON_SOCIAL = "razonSocial";
	public static final String SIGLAS = "siglas";

	public void save(WebTblCliente transientInstance) {
		log.debug("saving WebTblCliente instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WebTblCliente persistentInstance) {
		log.debug("deleting WebTblCliente instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WebTblCliente findById(java.lang.Integer id) {
		log.debug("getting WebTblCliente instance with id: " + id);
		try {
			WebTblCliente instance = (WebTblCliente) getSession().get(
					"WebTblCliente", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WebTblCliente instance) {
		log.debug("finding WebTblCliente instance by example");
		try {
			List results = getSession().createCriteria("WebTblCliente")
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
		log.debug("finding WebTblCliente instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from WebTblCliente as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRazonSocial(Object razonSocial) {
		return findByProperty(RAZON_SOCIAL, razonSocial);
	}

	public List findBySiglas(Object siglas) {
		return findByProperty(SIGLAS, siglas);
	}

	public List findAll() {
		log.debug("finding all WebTblCliente instances");
		try {
			String queryString = "from WebTblCliente";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WebTblCliente merge(WebTblCliente detachedInstance) {
		log.debug("merging WebTblCliente instance");
		try {
			WebTblCliente result = (WebTblCliente) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WebTblCliente instance) {
		log.debug("attaching dirty WebTblCliente instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WebTblCliente instance) {
		log.debug("attaching clean WebTblCliente instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}