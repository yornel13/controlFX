package hibernate.dao;

// default package

import hibernate.model.ControlExtras;
import java.sql.Date;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ControlExtras entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .ControlExtras
 * @author MyEclipse Persistence Tools
 */
public class ControlExtrasDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ControlExtrasDAO.class);
	// property constants
        public static final String NORMALES = "normales";
	public static final String SOBRETIEMPO = "sobretiempo";
	public static final String RECARGO = "recargo";

	public void save(ControlExtras transientInstance) {
		log.debug("saving ControlExtras instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ControlExtras persistentInstance) {
		log.debug("deleting ControlExtras instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ControlExtras findById(java.lang.Integer id) {
		log.debug("getting ControlExtras instance with id: " + id);
		try {
			ControlExtras instance = (ControlExtras) getSession().get(
					"hibernate.model.ControlExtras", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<ControlExtras> findAllByEmpleadoId(Integer usuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_extras "
                            + "WHERE usuario_id = :usuario_id")
                    .addEntity(ControlExtras.class)
                    .setParameter("usuario_id", usuarioId);
                Object result = query.list();
                return (List<ControlExtras>) result;
	}
        
        public List<ControlExtras> findAllByEmpleadoIdInDeterminateTime(
                Integer usuarioId, Date inicio, Date fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_extras "
                            + "WHERE usuario_id = :usuario_id "
                            + "and fecha >= :inicio "
                            + "and fecha <= :fin "
                            + "order by fecha")
                    .addEntity(ControlExtras.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlExtras>) result;
	}
        
        public List<ControlExtras> findAllByClienteId(Integer clienteId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_extras "
                            + "WHERE cliente_id = :cliente_id")
                    .addEntity(ControlExtras.class)
                    .setParameter("cliente_id", clienteId);
                Object result = query.list();
                return (List<ControlExtras>) result;
	}
        
        public List<ControlExtras> findAllByEmpleadoIdClienteIdInDeterminateTime(
                Integer usuarioId, Integer clienteId, Date inicio, Date fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_extras "
                            + "WHERE usuario_id = :usuario_id "
                            + "and cliente_id = :cliente_id "
                            + "and fecha >= :inicio "
                            + "and fecha <= :fin "
                            + "order by fecha")
                    .addEntity(ControlExtras.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlExtras>) result;
	}
        
        public List<ControlExtras> findAllByClienteIdInDeterminateTime(
                Integer clienteId, Date inicio, Date fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_extras "
                            + "WHERE cliente_id = :cliente_id "
                            + "and fecha >= :inicio "
                            + "and fecha <= :fin "
                            + "order by fecha")
                    .addEntity(ControlExtras.class)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlExtras>) result;
	}
        
        public List<ControlExtras> findAllBySinClienteInDeterminateTime(
                Date inicio, Date fin) {
            Query query = getSession().
                createSQLQuery("SELECT * FROM control_extras "
                        + "WHERE cliente_id is null "
                        + "and fecha >= :inicio "
                        + "and fecha <= :fin "
                        + "order by fecha")
                .addEntity(ControlExtras.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin);
            Object result = query.list();
            return (List<ControlExtras>) result;
	}
        
        public List<ControlExtras> findAllByEmpleadoIdSinClienteInDeterminateTime(
                Integer usuarioId, Date inicio, Date fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_extras "
                            + "WHERE usuario_id = :usuario_id "
                            + "and cliente_id is null "
                            + "and fecha >= :inicio "
                            + "and fecha <= :fin "
                            + "order by fecha")
                    .addEntity(ControlExtras.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlExtras>) result;
	}
        
        public ControlExtras findByFecha(Date fecha, Integer usuarioId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM control_extras "
                            + "where fecha = :fecha "
                            + "and usuario_id = :usuario_id")
                    .addEntity(ControlExtras.class)
                    .setParameter("fecha", fecha)
                    .setParameter("usuario_id", usuarioId);
            Object result = query.uniqueResult();
            return (ControlExtras) result;
        }
        
        public List<ControlExtras> findAllByFechaAndEmpresaId(Date fecha, 
                Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM control_extras "
                            + "JOIN usuario "
                            + "ON usuario.id = control_extras.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where fecha = :fecha "
                            + "and empresa_id = :empresa_id")
                    .addEntity(ControlExtras.class)
                    .setParameter("fecha", fecha)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<ControlExtras>) result;
        }
        
        public List<ControlExtras> findAllByUltimosRegistros() {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM "
                            + "(SELECT distinct * FROM control_extras ORDER BY id DESC) "
                            + "AS t1 group by usuario_id")
                    .addEntity(ControlExtras.class);
            Object result = query.list();
            return (List<ControlExtras>) result;
        }

	public List findByExample(ControlExtras instance) {
		log.debug("finding ControlExtras instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.ControlExtras")
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
		log.debug("finding ControlExtras instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ControlExtras as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRecargo(Object recargo) {
		return findByProperty(RECARGO, recargo);
	}

	public List findBySobretiempo(Object sobretiempo) {
		return findByProperty(SOBRETIEMPO, sobretiempo);
	}

	public List findAll() {
		log.debug("finding all ControlExtras instances");
		try {
			String queryString = "from ControlExtras";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ControlExtras merge(ControlExtras detachedInstance) {
		log.debug("merging ControlExtras instance");
		try {
			ControlExtras result = (ControlExtras) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ControlExtras instance) {
		log.debug("attaching dirty ControlExtras instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ControlExtras instance) {
		log.debug("attaching clean ControlExtras instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}