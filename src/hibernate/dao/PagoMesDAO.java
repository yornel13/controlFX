package hibernate.dao;

// default package

import hibernate.model.PagoMes;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * PagoMes entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .PagoMes
 * @author MyEclipse Persistence Tools
 */
public class PagoMesDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(PagoMesDAO.class);
	// property constants
	public static final String MONTO = "monto";

	public void save(PagoMes transientInstance) {
		log.debug("saving PagoMes instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PagoMes persistentInstance) {
		log.debug("deleting PagoMes instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PagoMes findById(java.lang.Integer id) {
		log.debug("getting PagoMes instance with id: " + id);
		try {
			PagoMes instance = (PagoMes) getSession().get("hibernate.model.PagoMes", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<PagoMes> findByEmpleadoId(Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_mes where usuario_id = :usuario_id")
                    .addEntity(PagoMes.class)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.list();
            return (List<PagoMes>) result;
        }
        
        public PagoMes findByRolIndividual(Integer rolIndividualId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_mes "
                            + "where rol_individual_id = :rol_individual_id")
                    .addEntity(PagoMes.class)
                    .setParameter("rol_individual_id", rolIndividualId);
            Object result = query.uniqueResult();
            return (PagoMes) result;
        }
        
        public PagoMes findInDeterminateTimeByUsuarioId(Timestamp fin, Integer usuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_mes WHERE "
                            + "usuario_id = :usuario_id and "
                            + "fin_mes = :fin")
                    .addEntity(PagoMes.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("fin", fin);
                Object result = query.uniqueResult();
                return (PagoMes) result;
	}

	public List findByExample(PagoMes instance) {
		log.debug("finding PagoMes instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.PagoMes")
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
		log.debug("finding PagoMes instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from PagoMes as model where model."
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

	public List findAll() {
		log.debug("finding all PagoMes instances");
		try {
			String queryString = "from PagoMes";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PagoMes merge(PagoMes detachedInstance) {
		log.debug("merging PagoMes instance");
		try {
			PagoMes result = (PagoMes) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PagoMes instance) {
		log.debug("attaching dirty PagoMes instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PagoMes instance) {
		log.debug("attaching clean PagoMes instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}