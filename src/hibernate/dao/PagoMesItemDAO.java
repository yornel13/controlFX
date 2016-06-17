package hibernate.dao;

// default package

import hibernate.model.PagoMesItem;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * PagoMesItem entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .PagoMesItem
 * @author MyEclipse Persistence Tools
 */
public class PagoMesItemDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(PagoMesItemDAO.class);
	// property constants
	public static final String DESCRIPCION = "descripcion";
	public static final String DIAS = "dias";
	public static final String HORAS = "horas";
	public static final String INGRESO = "ingreso";
	public static final String DEDUCCION = "deduccion";

	public void save(PagoMesItem transientInstance) {
		log.debug("saving PagoMesItem instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PagoMesItem persistentInstance) {
		log.debug("deleting PagoMesItem instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PagoMesItem findById(java.lang.Integer id) {
		log.debug("getting PagoMesItem instance with id: " + id);
		try {
			PagoMesItem instance = (PagoMesItem) getSession().get(
					"hibernate.model.PagoMesItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<PagoMesItem> findByPagoMesId(Integer pagoMesId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_mes_item where pago_mes_id = :pago_mes_id")
                    .addEntity(PagoMesItem.class)
                    .setParameter("pago_mes_id", pagoMesId);
            Object result = query.list();
            return (List<PagoMesItem>) result;
        }
        
        public List<PagoMesItem> findByEmpleadoIdAndClave(Integer empleadoId, String clave) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_mes_item JOIN pago_mes "
                            + "ON pago_mes.id = pago_mes_item.pago_mes_id "
                            + "where usuario_id = :usuario_id  and clave = :clave")
                    .addEntity(PagoMesItem.class)
                    .setParameter("usuario_id", empleadoId)
                    .setParameter("clave", clave);
            Object result = query.list();
            return (List<PagoMesItem>) result;
        }
        
        public List<PagoMesItem> findByEmpleadoIdAndClaveAndFecha(Integer empleadoId, String clave, Timestamp fin) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago_mes_item "
                            + "JOIN pago_mes "
                            + "ON pago_mes.id = pago_mes_item.pago_mes_id "
                            + "where usuario_id = :usuario_id "
                            + "and fin_mes = :fin_mes "
                            + "and clave = :clave")
                    .addEntity(PagoMesItem.class)
                    .setParameter("usuario_id", empleadoId)
                    .setParameter("fin_mes", fin)
                    .setParameter("clave", clave);
            Object result = query.list();
            return (List<PagoMesItem>) result;
        }

	public List findByExample(PagoMesItem instance) {
		log.debug("finding PagoMesItem instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.PagoMesItem")
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
		log.debug("finding PagoMesItem instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from PagoMesItem as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDescripcion(Object descripcion) {
		return findByProperty(DESCRIPCION, descripcion);
	}

	public List findByDias(Object dias) {
		return findByProperty(DIAS, dias);
	}

	public List findByHoras(Object horas) {
		return findByProperty(HORAS, horas);
	}

	public List findByIngreso(Object ingreso) {
		return findByProperty(INGRESO, ingreso);
	}

	public List findByDeduccion(Object deduccion) {
		return findByProperty(DEDUCCION, deduccion);
	}

	public List findAll() {
		log.debug("finding all PagoMesItem instances");
		try {
			String queryString = "from PagoMesItem";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PagoMesItem merge(PagoMesItem detachedInstance) {
		log.debug("merging PagoMesItem instance");
		try {
			PagoMesItem result = (PagoMesItem) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PagoMesItem instance) {
		log.debug("attaching dirty PagoMesItem instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PagoMesItem instance) {
		log.debug("attaching clean PagoMesItem instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}