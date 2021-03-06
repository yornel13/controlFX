package hibernate.dao;

// default package

import hibernate.model.PagoQuincena;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * PagoQuincena entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .PagoQuincena
 * @author MyEclipse Persistence Tools
 */
public class PagoQuincenaDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(PagoQuincenaDAO.class);
	// property constants
	public static final String MONTO = "monto";

	public void save(PagoQuincena transientInstance) {
		log.debug("saving PagoQuincena instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PagoQuincena persistentInstance) {
		log.debug("deleting PagoQuincena instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PagoQuincena findById(java.lang.Integer id) {
		log.debug("getting PagoQuincena instance with id: " + id);
		try {
			PagoQuincena instance = (PagoQuincena) getSession().get(
					"hibernate.model.PagoQuincena", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PagoQuincena instance) {
		log.debug("finding PagoQuincena instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.PagoQuincena")
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
		log.debug("finding PagoQuincena instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from PagoQuincena as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByMonto(Object monto) {
		return findByProperty(MONTO, monto);
	}
        
        public List<PagoQuincena> findAllInDeterminateTime(Timestamp fecha) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_quincena "
                            + "WHERE inicio_mes = :fecha")
                    .addEntity(PagoQuincena.class)
                    .setParameter("fecha", fecha);
                Object result = query.list();
                return (List<PagoQuincena>) result;
	}
        
        public List<PagoQuincena> findAllInDeterminateTime(String fecha) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_quincena "
                            + "WHERE inicio_mes = :fecha")
                    .addEntity(PagoQuincena.class)
                    .setParameter("fecha", fecha);
                Object result = query.list();
                return (List<PagoQuincena>) result;
	}
        
        public PagoQuincena findInDeterminateTimeByUsuarioId(Timestamp fecha, Integer usuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_quincena "
                            + "WHERE usuario_id = :usuario_id "
                            + "and inicio_mes = :fecha")
                    .addEntity(PagoQuincena.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("fecha", fecha);
                Object result = query.uniqueResult();
                return (PagoQuincena) result;
	}
        
        public PagoQuincena findInDeterminateTimeByUsuarioId(String fecha, Integer usuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_quincena "
                            + "WHERE usuario_id = :usuario_id "
                            + "and inicio_mes = :fecha")
                    .addEntity(PagoQuincena.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("fecha", fecha);
                Object result = query.uniqueResult();
                return (PagoQuincena) result;
	}
        
        public List<PagoQuincena> findAllByUsuarioId(Integer usuarioId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_quincena "
                            + "where usuario_id = :usuario_id")
                    .addEntity(PagoQuincena.class)
                    .setParameter("usuario_id", usuarioId);
            Object result = query.list();
            return (List<PagoQuincena>) result;
        }

	public List findAll() {
		log.debug("finding all PagoQuincena instances");
		try {
			String queryString = "from PagoQuincena";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PagoQuincena merge(PagoQuincena detachedInstance) {
		log.debug("merging PagoQuincena instance");
		try {
			PagoQuincena result = (PagoQuincena) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PagoQuincena instance) {
		log.debug("attaching dirty PagoQuincena instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PagoQuincena instance) {
		log.debug("attaching clean PagoQuincena instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}