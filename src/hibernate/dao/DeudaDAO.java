package hibernate.dao;

// default package

import hibernate.model.Deuda;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for Deuda
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see .Deuda
 * @author MyEclipse Persistence Tools
 */
public class DeudaDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(DeudaDAO.class);
	// property constants
	public static final String DETALLES = "detalles";
	public static final String MONTO = "monto";
	public static final String RESTANTE = "restante";
	public static final String CUOTAS = "cuotas";
	public static final String PAGADA = "pagada";

	public void save(Deuda transientInstance) {
		log.debug("saving Deuda instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Deuda persistentInstance) {
		log.debug("deleting Deuda instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Deuda findById(java.lang.Integer id) {
		log.debug("getting Deuda instance with id: " + id);
		try {
			Deuda instance = (Deuda) getSession().get("hibernate.model.Deuda", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Deuda instance) {
		log.debug("finding Deuda instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Deuda")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
        
        public List<Deuda> findAllByEmpleadoId(Integer UsuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM deuda WHERE usuario_id = :usuario_id order by creacion DESC")
                    .addEntity(Deuda.class)
                    .setParameter("usuario_id", UsuarioId);
            Object result = query.list();
            return (List<Deuda>) result;
	}
        
        public List<Deuda> findAllByUsuarioIdNoPagadaSinAplazar(Integer UsuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM deuda WHERE "
                            + "usuario_id = :usuario_id and "
                            + "pagada = 0 and "
                            + "aplazar = 0 "
                            + "order by creacion DESC")
                    .addEntity(Deuda.class)
                    .setParameter("usuario_id", UsuarioId);
            Object result = query.list();
            return (List<Deuda>) result;
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Deuda instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Deuda as model where model."
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

	public List findByMonto(Object monto) {
		return findByProperty(MONTO, monto);
	}

	public List findByRestante(Object restante) {
		return findByProperty(RESTANTE, restante);
	}

	public List findByCuotas(Object cuotas) {
		return findByProperty(CUOTAS, cuotas);
	}

	public List findByPagada(Object pagada) {
		return findByProperty(PAGADA, pagada);
	}

	public List findAll() {
		log.debug("finding all Deuda instances");
		try {
			String queryString = "from Deuda";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Deuda merge(Deuda detachedInstance) {
		log.debug("merging Deuda instance");
		try {
			Deuda result = (Deuda) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Deuda instance) {
		log.debug("attaching dirty Deuda instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Deuda instance) {
		log.debug("attaching clean Deuda instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}