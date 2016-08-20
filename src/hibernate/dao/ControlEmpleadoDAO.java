package hibernate.dao;

// default package

import hibernate.model.ControlEmpleado;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ControlEmpleado entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .ControlEmpleado
 * @author MyEclipse Persistence Tools
 */
public class ControlEmpleadoDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ControlEmpleadoDAO.class);
	// property constants
        public static final String NORMALES = "normales";
	public static final String SOBRETIEMPO = "sobretiempo";
	public static final String RECARGO = "recargo";

	public void save(ControlEmpleado transientInstance) {
		log.debug("saving ControlEmpleado instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ControlEmpleado persistentInstance) {
		log.debug("deleting ControlEmpleado instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ControlEmpleado findById(java.lang.Integer id) {
		log.debug("getting ControlEmpleado instance with id: " + id);
		try {
			ControlEmpleado instance = (ControlEmpleado) getSession().get(
					"hibernate.model.ControlEmpleado", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<ControlEmpleado> findAllByEmpleadoId(Integer usuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_empleado WHERE usuario_id = :usuario_id")
                    .addEntity(ControlEmpleado.class)
                    .setParameter("usuario_id", usuarioId);
                Object result = query.list();
                return (List<ControlEmpleado>) result;
	}
        
        public List<ControlEmpleado> findAllByEmpleadoIdInDeterminateTime(Integer usuarioId, Timestamp inicio, Timestamp fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_empleado WHERE usuario_id = :usuario_id "
                            + "and fecha >= :inicio and fecha <= :fin order by fecha")
                    .addEntity(ControlEmpleado.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlEmpleado>) result;
	}
        
        public List<ControlEmpleado> findAllByClienteId(Integer clienteId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_empleado WHERE cliente_id = :cliente_id")
                    .addEntity(ControlEmpleado.class)
                    .setParameter("cliente_id", clienteId);
                Object result = query.list();
                return (List<ControlEmpleado>) result;
	}
        
        public List<ControlEmpleado> findAllByEmpleadoIdClienteIdInDeterminateTime(
                Integer usuarioId, Integer clienteId, Timestamp inicio, Timestamp fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_empleado WHERE usuario_id = :usuario_id and cliente_id = :cliente_id "
                            + "and fecha >= :inicio and fecha <= :fin order by fecha")
                    .addEntity(ControlEmpleado.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlEmpleado>) result;
	}
        
        public List<ControlEmpleado> findAllByClienteIdInDeterminateTime(
                Integer clienteId, Timestamp inicio, Timestamp fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_empleado WHERE cliente_id = :cliente_id "
                            + "and fecha >= :inicio and fecha <= :fin order by fecha")
                    .addEntity(ControlEmpleado.class)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlEmpleado>) result;
	}
        
        public List<ControlEmpleado> findAllByEmpleadoIdSinClienteInDeterminateTime(
                Integer usuarioId, Timestamp inicio, Timestamp fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_empleado WHERE usuario_id = :usuario_id and cliente_id is null "
                            + "and fecha >= :inicio and fecha <= :fin order by fecha")
                    .addEntity(ControlEmpleado.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlEmpleado>) result;
	}
        
        public List<ControlEmpleado> findAllBySinClienteInDeterminateTime(
                Timestamp inicio, Timestamp fin) {
            Query query = getSession().
                createSQLQuery("SELECT * FROM control_empleado WHERE cliente_id is null "
                        + "and fecha >= :inicio and fecha <= :fin order by fecha")
                .addEntity(ControlEmpleado.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin);
            Object result = query.list();
            return (List<ControlEmpleado>) result;
	}
        
        public ControlEmpleado findByFecha(Timestamp fecha, Integer usuarioId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM control_empleado where fecha = :fecha "
                            + "and usuario_id = :usuario_id")
                    .addEntity(ControlEmpleado.class)
                    .setParameter("fecha", fecha)
                    .setParameter("usuario_id", usuarioId);
            Object result = query.uniqueResult();
            return (ControlEmpleado) result;
        }
        
        public List<ControlEmpleado> findAllByFechaAndEmpresaId(Timestamp fecha, 
                Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM control_empleado "
                            + "JOIN usuario "
                            + "ON usuario.id = control_empleado.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where fecha = :fecha "
                            + "and empresa_id = :empresa_id")
                    .addEntity(ControlEmpleado.class)
                    .setParameter("fecha", fecha)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<ControlEmpleado>) result;
        }
        
        public List<ControlEmpleado> findAllByUltimosRegistros() {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM "
                            + "(SELECT distinct * FROM control_empleado ORDER BY id DESC) "
                            + "AS t1 group by usuario_id")
                    .addEntity(ControlEmpleado.class);
            Object result = query.list();
            return (List<ControlEmpleado>) result;
        }

	public List findByExample(ControlEmpleado instance) {
		log.debug("finding ControlEmpleado instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.ControlEmpleado")
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
		log.debug("finding ControlEmpleado instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ControlEmpleado as model where model."
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
		log.debug("finding all ControlEmpleado instances");
		try {
			String queryString = "from ControlEmpleado";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ControlEmpleado merge(ControlEmpleado detachedInstance) {
		log.debug("merging ControlEmpleado instance");
		try {
			ControlEmpleado result = (ControlEmpleado) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ControlEmpleado instance) {
		log.debug("attaching dirty ControlEmpleado instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ControlEmpleado instance) {
		log.debug("attaching clean ControlEmpleado instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}