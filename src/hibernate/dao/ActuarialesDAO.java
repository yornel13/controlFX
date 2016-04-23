package hibernate.dao;

// default package

import hibernate.model.Actuariales;
import hibernate.model.Usuario;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Actuariales entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Actuariales
 * @author MyEclipse Persistence Tools
 */
public class ActuarialesDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ActuarialesDAO.class);
	// property constants
	public static final String PRIMARIO = "primario";
	public static final String SECUNDARIO = "secundario";
	public static final String ACTIVO = "activo";

	public void save(Actuariales transientInstance) {
		log.debug("saving Actuariales instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Actuariales persistentInstance) {
		log.debug("deleting Actuariales instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Actuariales findById(java.lang.Integer id) {
		log.debug("getting Actuariales instance with id: " + id);
		try {
			Actuariales instance = (Actuariales) getSession().get(
					"hibernate.model.Actuariales", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Actuariales instance) {
		log.debug("finding Actuariales instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Actuariales")
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
		log.debug("finding Actuariales instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Actuariales as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPrimario(Object primario) {
		return findByProperty(PRIMARIO, primario);
	}

	public List findBySecundario(Object secundario) {
		return findByProperty(SECUNDARIO, secundario);
	}

	public List findByActivo(Object activo) {
		return findByProperty(ACTIVO, activo);
	}

	public List findAll() {
		log.debug("finding all Actuariales instances");
		try {
			String queryString = "from Actuariales";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
        
        public Actuariales findByEmpleadoId(Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM actuariales where usuario_id = :empleado_id and activo = true")
                    .addEntity(Actuariales.class)
                    .setParameter("empleado_id", empleadoId);
            Object result = query.uniqueResult();
            return (Actuariales) result;
        }

	public Actuariales merge(Actuariales detachedInstance) {
		log.debug("merging Actuariales instance");
		try {
			Actuariales result = (Actuariales) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Actuariales instance) {
		log.debug("attaching dirty Actuariales instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Actuariales instance) {
		log.debug("attaching clean Actuariales instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}