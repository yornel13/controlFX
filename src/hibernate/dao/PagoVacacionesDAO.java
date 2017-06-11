package hibernate.dao;

// default package

import hibernate.model.PagoVacaciones;
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
 * PagoVacaciones entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .PagoVacaciones
 * @author MyEclipse Persistence Tools
 */
public class PagoVacacionesDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(PagoVacacionesDAO.class);
	// property constants
	public static final String MONTO = "monto";

	public void save(PagoVacaciones transientInstance) {
		log.debug("saving PagoVacaciones instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PagoVacaciones persistentInstance) {
		log.debug("deleting PagoVacaciones instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PagoVacaciones findById(java.lang.Integer id) {
		log.debug("getting PagoVacaciones instance with id: " + id);
		try {
			PagoVacaciones instance = (PagoVacaciones) getSession().get(
					"hibernate.model.PagoVacaciones", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PagoVacaciones instance) {
		log.debug("finding PagoVacaciones instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.PagoVacaciones")
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
		log.debug("finding PagoVacaciones instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from PagoVacaciones as model where model."
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
        
        public List<PagoVacaciones> findAllInDeterminateTime(Timestamp fecha) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_vacaciones "
                            + "WHERE inicio_mes = :fecha")
                    .addEntity(PagoVacaciones.class)
                    .setParameter("fecha", fecha);
                Object result = query.list();
                return (List<PagoVacaciones>) result;
	}
        
        public List<PagoVacaciones> findAllInDeterminateTime(String fecha) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_vacaciones "
                            + "WHERE inicio_mes = :fecha")
                    .addEntity(PagoVacaciones.class)
                    .setParameter("fecha", fecha);
                Object result = query.list();
                return (List<PagoVacaciones>) result;
	}
        
        public PagoVacaciones findInDeterminateTimeByUsuarioId(String inicio, Integer usuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_vacaciones "
                            + "WHERE usuario_id = :usuario_id "
                            + "and inicio = :fecha")
                    .addEntity(PagoVacaciones.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("fecha", inicio);
                Object result = query.uniqueResult();
                return (PagoVacaciones) result;
	}
        
        public List<PagoVacaciones> findAllByUsuarioId(Integer usuarioId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_vacaciones "
                            + "where usuario_id = :usuario_id")
                    .addEntity(PagoVacaciones.class)
                    .setParameter("usuario_id", usuarioId);
            Object result = query.list();
            return (List<PagoVacaciones>) result;
        }

	public List findAll() {
		log.debug("finding all PagoVacaciones instances");
		try {
			String queryString = "from PagoVacaciones";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PagoVacaciones merge(PagoVacaciones detachedInstance) {
		log.debug("merging PagoVacaciones instance");
		try {
			PagoVacaciones result = (PagoVacaciones) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PagoVacaciones instance) {
		log.debug("attaching dirty PagoVacaciones instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PagoVacaciones instance) {
		log.debug("attaching clean PagoVacaciones instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}