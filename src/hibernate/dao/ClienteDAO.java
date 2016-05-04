package hibernate.dao;

// default package

import hibernate.model.Cliente;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Cliente entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Cliente
 * @author MyEclipse Persistence Tools
 */
public class ClienteDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(ClienteDAO.class);
	// property constants
	public static final String NOMBRE = "nombre";
	public static final String DETALLES = "detalles";
	public static final String RUC = "ruc";
	public static final String DIRECCION = "direccion";
	public static final String TELEFONO = "telefono";
	public static final String ACTIVO = "activo";

	public void save(Cliente transientInstance) {
		log.debug("saving Cliente instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Cliente persistentInstance) {
		log.debug("deleting Cliente instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Cliente findById(java.lang.Integer id) {
		log.debug("getting Cliente instance with id: " + id);
		try {
			Cliente instance = (Cliente) getSession().get("hibernate.model.Cliente", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Cliente instance) {
		log.debug("finding Cliente instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Cliente")
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
		log.debug("finding Cliente instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Cliente as model where model."
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

	public List findByDetalles(Object detalles) {
		return findByProperty(DETALLES, detalles);
	}

	public List findByRuc(Object ruc) {
		return findByProperty(RUC, ruc);
	}

	public List findByDireccion(Object direccion) {
		return findByProperty(DIRECCION, direccion);
	}

	public List findByTelefono(Object telefono) {
		return findByProperty(TELEFONO, telefono);
	}

	public List findByActivo(Object activo) {
		return findByProperty(ACTIVO, activo);
	}

	public List<Cliente> findAllActivo() {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM cliente where activo = true")
                    .addEntity(Cliente.class);
            Object result = query.list();
            return (List<Cliente>)result;
	}
        
        public List findAll() {
		log.debug("finding all Cliente instances");
		try {
			String queryString = "from Cliente";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Cliente merge(Cliente detachedInstance) {
		log.debug("merging Cliente instance");
		try {
			Cliente result = (Cliente) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Cliente instance) {
		log.debug("attaching dirty Cliente instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Cliente instance) {
		log.debug("attaching clean Cliente instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}