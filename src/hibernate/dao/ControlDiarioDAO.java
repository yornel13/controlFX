package hibernate.dao;

// default package

import hibernate.model.ControlDiario;
import java.sql.Date;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ControlDiario entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .ControlDiario
 * @author MyEclipse Persistence Tools
 */
public class ControlDiarioDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ControlDiarioDAO.class);
	// property constants
        public static final String NORMALES = "normales";
	public static final String SOBRETIEMPO = "sobretiempo";
	public static final String RECARGO = "recargo";

	public void save(ControlDiario transientInstance) {
		log.debug("saving ControlDiario instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ControlDiario persistentInstance) {
		log.debug("deleting ControlDiario instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ControlDiario findById(java.lang.Long id) {
		log.debug("getting ControlDiario instance with id: " + id);
		try {
			ControlDiario instance = (ControlDiario) getSession().get(
					"hibernate.model.ControlDiario", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<ControlDiario> findAllByEmpleadoId(Integer usuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_diario "
                            + "WHERE usuario_id = :usuario_id")
                    .addEntity(ControlDiario.class)
                    .setParameter("usuario_id", usuarioId);
                Object result = query.list();
                return (List<ControlDiario>) result;
	}
        
        public List<ControlDiario> findAllByEmpleadoIdInDeterminateTime(
                Integer usuarioId, String inicio, String fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_diario "
                            + "WHERE usuario_id = :usuario_id "
                            + "and fecha >= :inicio "
                            + "and fecha <= :fin "
                            + "order by fecha")
                    .addEntity(ControlDiario.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlDiario>) result;
	}
        
        public List<ControlDiario> findAllByClienteId(Integer clienteId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_diario "
                            + "WHERE cliente_id = :cliente_id")
                    .addEntity(ControlDiario.class)
                    .setParameter("cliente_id", clienteId);
                Object result = query.list();
                return (List<ControlDiario>) result;
	}
        
        public List<ControlDiario> findAllByEmpleadoIdClienteIdInDeterminateTime(
                Integer usuarioId, Integer clienteId, String inicio, String fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_diario "
                            + "WHERE usuario_id = :usuario_id "
                            + "and cliente_id = :cliente_id "
                            + "and fecha >= :inicio "
                            + "and fecha <= :fin "
                            + "order by fecha")
                    .addEntity(ControlDiario.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlDiario>) result;
	}
        
        public List<ControlDiario> findAllByClienteIdInDeterminateTime(
                Integer clienteId, String inicio, String fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_diario "
                            + "WHERE cliente_id = :cliente_id "
                            + "and fecha >= :inicio "
                            + "and fecha <= :fin "
                            + "order by fecha")
                    .addEntity(ControlDiario.class)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlDiario>) result;
	}
        
        public List<ControlDiario> findAllBySinClienteInDeterminateTime(
                String inicio, String fin) {
            Query query = getSession().
                createSQLQuery("SELECT * FROM control_diario "
                        + "WHERE cliente_id is null "
                        + "and fecha >= :inicio "
                        + "and fecha <= :fin "
                        + "order by fecha")
                .addEntity(ControlDiario.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin);
            Object result = query.list();
            return (List<ControlDiario>) result;
	}
        
        public List<ControlDiario> findAllByEmpleadoIdSinClienteInDeterminateTime(
                Integer usuarioId, String inicio, String fin) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM control_diario "
                            + "WHERE usuario_id = :usuario_id "
                            + "and cliente_id is null "
                            + "and fecha >= :inicio "
                            + "and fecha <= :fin "
                            + "order by fecha")
                    .addEntity(ControlDiario.class)
                    .setParameter("usuario_id", usuarioId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin);
                Object result = query.list();
                return (List<ControlDiario>) result;
	}
        
        public ControlDiario findByFecha(String fecha, Integer usuarioId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM control_diario "
                            + "where fecha = :fecha "
                            + "and usuario_id = :usuario_id")
                    .addEntity(ControlDiario.class)
                    .setParameter("fecha", fecha)
                    .setParameter("usuario_id", usuarioId);
            Object result = query.uniqueResult();
            return (ControlDiario) result;
        }
        
        public List<ControlDiario> findAllByFechaAndEmpresaId(String fecha, 
                Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM control_diario "
                            + "JOIN usuario "
                            + "ON usuario.id = control_diario.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where fecha = :fecha "
                            + "and empresa_id = :empresa_id")
                    .addEntity(ControlDiario.class)
                    .setParameter("fecha", fecha)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<ControlDiario>) result;
        }
        
        public List<ControlDiario> findAllByUltimosRegistros() {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM "
                            + "(SELECT distinct * FROM control_diario ORDER BY id DESC) "
                            + "AS t1 group by usuario_id")
                    .addEntity(ControlDiario.class);
            Object result = query.list();
            return (List<ControlDiario>) result;
        }

	public List findByExample(ControlDiario instance) {
		log.debug("finding ControlDiario instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.ControlDiario")
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
		log.debug("finding ControlDiario instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ControlDiario as model where model."
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
		log.debug("finding all ControlDiario instances");
		try {
			String queryString = "from ControlDiario";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ControlDiario merge(ControlDiario detachedInstance) {
		log.debug("merging ControlDiario instance");
		try {
			ControlDiario result = (ControlDiario) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ControlDiario instance) {
		log.debug("attaching dirty ControlDiario instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ControlDiario instance) {
		log.debug("attaching clean ControlDiario instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}