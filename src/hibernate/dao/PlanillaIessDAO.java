package hibernate.dao;

// default package

import hibernate.model.PlanillaIess;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * PlanillaIess entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .PlanillaIess
 * @author MyEclipse Persistence Tools
 */
public class PlanillaIessDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(PlanillaIessDAO.class);
	// property constants
	public static final String MONTO = "monto";

	public void save(PlanillaIess transientInstance) {
		log.debug("saving PlanillaIess instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PlanillaIess persistentInstance) {
		log.debug("deleting PlanillaIess instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PlanillaIess findById(java.lang.Integer id) {
		log.debug("getting PlanillaIess instance with id: " + id);
		try {
			PlanillaIess instance = (PlanillaIess) getSession().get("hibernate.model.PlanillaIess", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<PlanillaIess> findByEmpleadoId(Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM planilla_iess "
                            + "where usuario_id = :usuario_id")
                    .addEntity(PlanillaIess.class)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.list();
            return (List<PlanillaIess>) result;
        }
        
        public PlanillaIess findInDeterminateTimeByUsuarioId(Timestamp fecha, Integer usuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM planilla_iess "
                            + "WHERE usuario_id = :usuario_id "
                            + "and inicio_mes = :fecha")
                    .addEntity(PlanillaIess.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("fecha", fecha);
                Object result = query.uniqueResult();
                return (PlanillaIess) result;
	}
        
        public List<PlanillaIess> findAllByFechaAndEmpresaId(Timestamp fecha, Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM planilla_iess "
                            + "JOIN usuario "
                            + "ON usuario.id = planilla_iess.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where inicio_mes = :fecha "
                            + "and empresa_id = :empresa_id")
                    .addEntity(PlanillaIess.class)
                    .setParameter("fecha", fecha)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<PlanillaIess>) result;
        }

	public List findByExample(PlanillaIess instance) {
		log.debug("finding PlanillaIess instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.PlanillaIess")
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
		log.debug("finding PlanillaIess instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from PlanillaIess as model where model."
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
		log.debug("finding all PlanillaIess instances");
		try {
			String queryString = "from PlanillaIess";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PlanillaIess merge(PlanillaIess detachedInstance) {
		log.debug("merging PlanillaIess instance");
		try {
			PlanillaIess result = (PlanillaIess) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PlanillaIess instance) {
		log.debug("attaching dirty PlanillaIess instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PlanillaIess instance) {
		log.debug("attaching clean PlanillaIess instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}