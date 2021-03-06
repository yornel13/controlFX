package hibernate.dao;

// default package

import aplicacion.control.util.Const;
import hibernate.model.PagoDecimo;
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
public class PagoDecimoDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(PagoDecimoDAO.class);
	// property constants
	public static final String MONTO = "monto";

	public void save(PagoDecimo transientInstance) {
		log.debug("saving PagoDecimo instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PagoDecimo persistentInstance) {
		log.debug("deleting PagoMes instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PagoDecimo findById(java.lang.Integer id) {
		log.debug("getting PagoMes instance with id: " + id);
		try {
			PagoDecimo instance = (PagoDecimo) getSession().get("hibernate.model.PagoDecimo", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<PagoDecimo> findByEmpleadoId(Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_decimo where usuario_id = :usuario_id")
                    .addEntity(PagoDecimo.class)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.list();
            return (List<PagoDecimo>) result;
        }
        
        public List<PagoDecimo> findByEmpleadoIdAndDecimo(Integer empleadoId, String decimo) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_decimo where "
                            + "usuario_id = :usuario_id and "
                            + "decimo = :decimo")
                    .addEntity(PagoDecimo.class)
                    .setParameter("usuario_id", empleadoId)
                    .setParameter("decimo", decimo);
            Object result = query.list();
            return (List<PagoDecimo>) result;
        }
        
        public PagoDecimo getLastPagoStart3(Integer year) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_decimo where "
                            + "monto = :monto and "
                            + "decimo = :decimo")
                    .addEntity(PagoDecimo.class)
                    .setParameter("decimo", Const.DECIMO_TERCERO_START)
                    .setParameter("monto", year.doubleValue());
            Object result = query.uniqueResult();
            return (PagoDecimo) result;
        }
        
        public PagoDecimo getLastPagoEnd3(Integer year) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_decimo where "
                            + "monto = :monto and "
                            + "decimo = :decimo")
                    .addEntity(PagoDecimo.class)
                    .setParameter("decimo", Const.DECIMO_TERCERO_END)
                    .setParameter("monto", year.doubleValue());
            Object result = query.uniqueResult();
            return (PagoDecimo) result;
        }
        
        public PagoDecimo getLastPagoStart4(Integer year) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_decimo where "
                            + "monto = :monto and "
                            + "decimo = :decimo")
                    .addEntity(PagoDecimo.class)
                    .setParameter("decimo", Const.DECIMO_CUARTO_START)
                    .setParameter("monto", year.doubleValue());
            Object result = query.uniqueResult();
            return (PagoDecimo) result;
        }
        
        public PagoDecimo getLastPagoEnd4(Integer year) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_decimo where "
                            + "monto = :monto and "
                            + "decimo = :decimo")
                    .addEntity(PagoDecimo.class)
                    .setParameter("decimo", Const.DECIMO_CUARTO_END)
                    .setParameter("monto", year.doubleValue());
            Object result = query.uniqueResult();
            return (PagoDecimo) result;
        }
        
        public List<PagoDecimo> findByDecimoAndFecha(String decimo, Timestamp fecha) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_decimo where "
                            + "fecha = :fecha and "
                            + "decimo = :decimo")
                    .addEntity(PagoDecimo.class)
                    .setParameter("fecha", fecha)
                    .setParameter("decimo", decimo);
            Object result = query.list();
            return (List<PagoDecimo>) result;
        }

	public List findByExample(PagoDecimo instance) {
		log.debug("finding PagoDecimo instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.PagoDecimo")
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
		log.debug("finding PagoDecimo instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from PagoDecimo as model where model."
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
		log.debug("finding all PagoDecimo instances");
		try {
			String queryString = "from PagoMes";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PagoDecimo merge(PagoDecimo detachedInstance) {
		log.debug("merging PagoDecimo instance");
		try {
			PagoDecimo result = (PagoDecimo) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PagoDecimo instance) {
		log.debug("attaching dirty PagoDecimo instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PagoDecimo instance) {
		log.debug("attaching clean PagoDecimo instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}