package hibernate.dao;

// default package

import hibernate.model.CuotaDeuda;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * CuotaDeuda entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .CuotaDeuda
 * @author MyEclipse Persistence Tools
 */
public class CuotaDeudaDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CuotaDeudaDAO.class);
	// property constants
	public static final String MONTO = "monto";

	public void save(CuotaDeuda transientInstance) {
		log.debug("saving CuotaDeuda instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
        
        public void deleteByDeudaId(Integer deudaId) {
            try {
                    
                Query query = getSession()
                        .createSQLQuery("DELETE FROM cuota_deuda where "
                                + "deuda_id = :deuda_id")
                        .setParameter("deuda_id", deudaId);
                query.executeUpdate();
            } catch (RuntimeException re) {
                    log.error("delete failed", re);
                    throw re;
            }
        }

	public void delete(CuotaDeuda persistentInstance) {
		log.debug("deleting CuotaDeuda instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CuotaDeuda findById(java.lang.Integer id) {
		log.debug("getting CuotaDeuda instance with id: " + id);
		try {
			CuotaDeuda instance = (CuotaDeuda) getSession().get("hibernate.model.CuotaDeuda",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<CuotaDeuda> findAllByPagoId(int pagoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM cuota_deuda where pago_id = :pago_id")
                    .addEntity(CuotaDeuda.class)
                    .setParameter("pago_id", pagoId);
            Object result = query.list();
            return (List<CuotaDeuda>) result;
        }
        
        public List<CuotaDeuda> findAllByDeudaId(int deudaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM cuota_deuda where deuda_id = :deuda_id")
                    .addEntity(CuotaDeuda.class)
                    .setParameter("deuda_id", deudaId);
            Object result = query.list();
            return (List<CuotaDeuda>) result;
        }
        
        public List<CuotaDeuda> findAllByFecha(String fecha) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM cuota_deuda where fecha = :fecha")
                    .addEntity(CuotaDeuda.class)
                    .setParameter("fecha", fecha);
            Object result = query.list();
            return (List<CuotaDeuda>) result;
        }
        
        public List<CuotaDeuda> findAllByFechaAndEmpleadoId(Integer empleadoId, String fecha) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM cuota_deuda JOIN deuda "
                            + "ON deuda.id = cuota_deuda.deuda_id "
                            + "where usuario_id = :usuario_id  and fecha = :fecha")
                    .addEntity(CuotaDeuda.class)
                    .setParameter("usuario_id", empleadoId)
                    .setParameter("fecha", fecha);
            Object result = query.list();
            return (List<CuotaDeuda>) result;
        }

	public List findByExample(CuotaDeuda instance) {
		log.debug("finding CuotaDeuda instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.CuotaDeuda")
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
		log.debug("finding CuotaDeuda instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from CuotaDeuda as model where model."
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
		log.debug("finding all CuotaDeuda instances");
		try {
			String queryString = "from CuotaDeuda";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CuotaDeuda merge(CuotaDeuda detachedInstance) {
		log.debug("merging CuotaDeuda instance");
		try {
			CuotaDeuda result = (CuotaDeuda) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CuotaDeuda instance) {
		log.debug("attaching dirty CuotaDeuda instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CuotaDeuda instance) {
		log.debug("attaching clean CuotaDeuda instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}